import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * ATTRIBUTES:
 *   -members      : List<Member>
 *   -trainers     : List<Trainer>
 *   -admins       : List<Admin>
 *   -equipments   : List<Equipment>
 *   -classSessions: List<ClassSession>
 *   -shopCatalog  : ShopCatalog
 *   -auditLog     : AuditLog
 *
 * OPERATIONS:
 *   +registerMember(name,age,phone,gender,password) : Member
 *   +requestOTP(memberId)  : String
 *   +verifyOTP(memberId,otp) : boolean
 *   +login(role,idOrName,password) : Account
 *   +logout(account) : void
 *
 * DATA FILES (all in DATA/ folder):
 *   member.txt   - one member per line (CSV, detailed format)
 *   trainer.txt  - one trainer per line
 *   equipment.txt
 *   class.txt    - class sessions
 *   product.txt  - shop products
 *   order.txt    - all orders (pipe-delimited)
 *   audit.txt    - audit trail
 */
public class FitnessClubSystem {

    // -- FILE PATHS ---------------------------------------------------
    private static final String DATA_FOLDER    = "DATA";
    private static final String MEMBER_FILE    = DATA_FOLDER + File.separator + "member.txt";
    private static final String TRAINER_FILE   = DATA_FOLDER + File.separator + "trainer.txt";
    private static final String EQUIPMENT_FILE = DATA_FOLDER + File.separator + "equipment.txt";
    private static final String CLASS_FILE     = DATA_FOLDER + File.separator + "class.txt";
    private static final String PRODUCT_FILE   = DATA_FOLDER + File.separator + "product.txt";
    private static final String ORDER_FILE     = DATA_FOLDER + File.separator + "order.txt";
    private static final String AUDIT_FILE     = DATA_FOLDER + File.separator + "audit.txt";

    private static final DateTimeFormatter DATE_FMT =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");

    // -- ATTRIBUTES ----------------------------------------------
    private List<Member>       members       = new ArrayList<>();
    private List<Trainer>      trainers      = new ArrayList<>();
    private List<Admin>        admins        = new ArrayList<>();
    private List<Equipment>    equipments    = new ArrayList<>();
    private List<ClassSession> classSessions = new ArrayList<>();
    private ShopCatalog        shopCatalog;
    private AuditLog           auditLog;

    // OTP registration flow maps
    private Map<String, Member> pendingMember = new HashMap<>();
    private Map<String, String> pendingOtp    = new HashMap<>();

    // -- CONSTRUCTOR --------------------------------------------------
    public FitnessClubSystem() {
        new File(DATA_FOLDER).mkdirs();
        auditLog    = new AuditLog(AUDIT_FILE);
        shopCatalog = new ShopCatalog(loadProducts());
        seedDefaultData();
    }

    // -- OPERATIONS -----------------------------------------------

// FUNCTION : Checks whether the member name already exists
// INPUT    : name = member name entered during registration
// OUTPUT   : true = already exists, false = not found
   public boolean memberNameExists(String name) {
    String target = name == null ? "" : name.trim();
    if (target.isEmpty()) return false;

    // 1. check already registered members in file
    try (BufferedReader br = new BufferedReader(new FileReader(MEMBER_FILE))) {
        String line;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;

            String[] p = line.split("\\|", -1);
            if (p.length < 4) continue;

            String fileName = p[3].trim();
            if (fileName.equalsIgnoreCase(target)) {
                return true;
            }
        }
    } catch (IOException e) {
        // member file may not exist yet, ignore
    }

    // 2. check pending members who registered but not confirmed OTP yet
    for (Member m : pendingMember.values()) {
        if (m != null &&
            m.getProfile() != null &&
            m.getProfile().getName() != null &&
            m.getProfile().getName().trim().equalsIgnoreCase(target)) {
            return true;
        }
    }

    return false;
}

// FUNCTION : Checks whether the phone number already exists
// INPUT    : phone = member phone entered during registration
// OUTPUT   : true = already exists, false = not found
public boolean memberPhoneExists(String phone) {
    String target = phone == null ? "" : phone.trim();
    if (target.isEmpty()) return false;

    // 1. check saved members
    try (BufferedReader br = new BufferedReader(new FileReader(MEMBER_FILE))) {
        String line;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;

            String[] p = line.split("\\|", -1);
            if (p.length < 6) continue;

            String filePhone = p[5].trim();
            if (filePhone.equalsIgnoreCase(target)) {
                return true;
            }
        }
    } catch (IOException e) {
        // file may not exist yet
    }

    // 2. check pending members
    for (Member m : pendingMember.values()) {
        if (m != null &&
            m.getProfile() != null &&
            m.getProfile().getPhone() != null &&
            m.getProfile().getPhone().trim().equalsIgnoreCase(target)) {
            return true;
        }
    }

    return false;
}      

 // FUNCTION : Registers a new member into pending registration
// INPUT    : name, age, phone, gender, passwordPlain
// OUTPUT   : Returns a pending Member object
// RULE     : Member name and phone must be unique
public Member registerMember(String name, int age, String phone, String gender, String passwordPlain) {

    if (memberNameExists(name)) {
        throw new IllegalArgumentException("This name is already registered.");
    }

    if (memberPhoneExists(phone)) {
        throw new IllegalArgumentException("This phone number is already registered.");
    }

    String newId   = generateNextId(MEMBER_FILE, "M");
    String pwdHash = Account.md5(passwordPlain);
    MyProfile profile = new MyProfile(name.trim(), age, phone.trim(), gender.trim());
    Member m = new Member(newId, pwdHash, "ACTIVE", profile);
    pendingMember.put(newId, m);
    return m;
}

    /** +requestOTP(memberId) : String  - Step 2 */
    public String requestOTP(String memberId) {
        String otp = String.valueOf(100000 + new Random().nextInt(900000));
        pendingOtp.put(memberId, otp);
        return otp;
    }

    /** +verifyOTP(memberId, otp) : boolean  - Step 3 */
    public boolean verifyOTP(String memberId, String inputOtp) {
        String stored = pendingOtp.get(memberId);
        return stored != null && stored.equals(inputOtp);
    }

    /** Confirms registration and saves member to file. */
    // FUNCTION : Confirms OTP registration and saves member into file
   // INPUT    : memberId = pending member ID
   // OUTPUT   : No return
   // RULE     : Removes pending member and OTP after saving
    public void confirmRegistration(String memberId) throws IOException {
        Member m = pendingMember.get(memberId);
        if (m == null) throw new IOException("Pending member not found.");
        saveMemberToFile(m);
        pendingMember.remove(memberId);
        pendingOtp.remove(memberId);
    }

    /**
     * +login(role,idOrName,password) : Account
     */
    public Account login(String role, String idOrName, String password) {
        switch (role.toUpperCase()) {
            case "MEMBER":  return loginMember(idOrName, password);
            case "TRAINER": return loginTrainer(idOrName, password);
            default:        return null;
        }
    }

    /** +logout(account) : void */
    public void logout(Account user) {
        if (user != null) user.logout();
    }

    // -- MEMBER LOGIN -------------------------------------------------

    /**
     * Looks up member by name (case-insensitive) in the file.
     * File format (24 fields, pipe and comma mixed - see saveMemberToFile):
     *  0:id, 1:hash, 2:status, 3:name, 4:age, 5:phone, 6:gender, 7:tier,
     *  8:weight, 9:height, 10:target, 11:bmi, 12:goal, 13:histEnc,
     *  14:lpPoints, 15:lpHistEnc, 16:membershipTier, 17:mFee, 18:mExpiry,
     *  19:eqBookings, 20:classBookings, 21:appointments, 22:orders,
     *  23:nextEquip, 24:nextClass, 25:nextAppt, 26:nextOrder
     */
    public Member loginMember(String nameOrId, String passwordPlain) {
        String inputHash = Account.md5(passwordPlain);
        try (BufferedReader br = new BufferedReader(new FileReader(MEMBER_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] p = line.split("\\|", -1);
                if (p.length < 8) continue;

                String fileId   = p[0].trim();
                String fileHash = p[1].trim();
                String fileStatus = p[2].trim();
                String fileName = p[3].trim();

               String input = nameOrId.trim();
               boolean idMatch   = fileId.equalsIgnoreCase(input);
               boolean nameMatch = fileName.equals(input);
               
                if (!idMatch && !nameMatch) continue;

                if (!"ACTIVE".equalsIgnoreCase(fileStatus)) {
                    System.out.println("[!] Account is " + fileStatus + ". Login denied.");
                    return null;
                }
                if (!fileHash.equals(inputHash)) return null;

                return parseMemberFromParts(p);
            }
        } catch (IOException e) {
            // file does not exist yet
        }
        return null;
    }

    // -- TRAINER LOGIN ------------------------------------------------

    public Trainer loginTrainer(String trainerId, String passwordPlain) {
        String inputHash = Account.md5(passwordPlain);
        for (Trainer t : loadTrainers()) {
            if (t.getAccountId().equalsIgnoreCase(trainerId.trim())) {
                if (!"ACTIVE".equalsIgnoreCase(t.getStatus())) {
                    System.out.println("[!] Trainer account is " + t.getStatus());
                    return null;
                }
                if (t.getPasswordHash().equals(inputHash)) return t;
                return null;
            }
        }
        return null;
    }

    // -- MEMBER PERSISTENCE -------------------------------------------

    private void saveMemberToFile(Member m) throws IOException {
        // Build a single comma-separated line. Fields with variable data use ~~
        // for embedded newlines (historyTable, loyaltyHistory, bookings, orders).
        StringBuilder sb = new StringBuilder();
        sb.append(m.getAccountId()).append("|")
          .append(m.getPasswordHash()).append("|")
          .append(m.getStatus()).append("|")
          .append(m.getProfile().getName()).append("|")
          .append(m.getProfile().getAge()).append("|")
          .append(m.getProfile().getPhone()).append("|")
          .append(m.getProfile().getGender()).append("|")
          .append(m.getTier()).append("|");

        // Health profile (8 fields)
        HealthProfile hp = m.getHealthProfile();
        if (hp != null) {
            sb.append(hp.getCurrentWeightKg()).append("|")
              .append(hp.getHeightCm()).append("|")
              .append(hp.getTargetWeightKg()).append("|")
              .append(hp.getBMI()).append("|")
              .append(hp.getGoal()).append("|")
              .append(hp.encodeHistory()).append("|");
        } else {
           sb.append("0|0|0|0|NONE|NONE|");  // 6 empty placeholders
        }

        // Loyalty points
        LoyaltyPoints lp = m.getLoyaltyObj();
        lp.normalizeHistory();
        sb.append(lp.getPoints()).append("|")
        .append(lp.encodeHistory()).append("|");

        // Membership
        Membership ms = m.getMembership();
        if (ms != null) {
            sb.append(ms.getTier()).append("|")
              .append(ms.getMonthlyFee()).append("|")
              .append(ms.getExpiryDate()).append("|");
        } else {
            sb.append("BASIC|60.0|NONE|");
        }

        // Equipment bookings (semicolons between entries)
        sb.append(encodeBookingList(m.getEquipmentBookings())).append("|");

        // Class bookings
        sb.append(encodeClassBookingList(m.getClassBookings())).append("|");

        // Trainer appointments
        sb.append(encodeApptList(m.getAppointments())).append("|");

        // Orders
        sb.append(encodeOrderList(m.getOrders())).append("|");

        // Booking number counters - just write next-booking-no as part of saved state
        // (We re-derive them from list sizes on load, so store 0 as placeholder)
        sb.append("0|0|0|0");

        try (FileWriter fw = new FileWriter(MEMBER_FILE, true)) {
            fw.write(sb.toString() + System.lineSeparator());
        }
    }

    /**
     * Rewrites the entire member.txt with current data for a given member.
     * Used whenever a member's data changes (tier upgrade, status change, etc.).
     */
    public void rewriteMemberFile(Member updated) {
        List<String> lines = readAllLines(MEMBER_FILE);
        try (FileWriter fw = new FileWriter(MEMBER_FILE, false)) {
            for (String line : lines) {
                String[] p = line.split("\\|", -1);
                if (p.length >= 1 && p[0].trim().equalsIgnoreCase(updated.getAccountId())) {
                    // Build the updated line by saving the member object afresh
                    // We write updated member inline here
                    fw.write(buildMemberLine(updated) + System.lineSeparator());
                } else {
                    fw.write(line + System.lineSeparator());
                }
            }
        } catch (IOException e) {
            System.out.println("[!] Could not rewrite member file: " + e.getMessage());
        }
    }

    private String buildMemberLine(Member m) {
        StringBuilder sb = new StringBuilder();
        sb.append(m.getAccountId()).append("|")
          .append(m.getPasswordHash()).append("|")
          .append(m.getStatus()).append("|")
          .append(m.getProfile().getName()).append("|")
          .append(m.getProfile().getAge()).append("|")
          .append(m.getProfile().getPhone()).append("|")
          .append(m.getProfile().getGender()).append("|")
          .append(m.getTier()).append("|");

        HealthProfile hp = m.getHealthProfile();
        if (hp != null) {
            sb.append(hp.getCurrentWeightKg()).append("|")
              .append(hp.getHeightCm()).append("|")
              .append(hp.getTargetWeightKg()).append("|")
              .append(hp.getBMI()).append("|")
              .append(hp.getGoal()).append("|")
              .append(hp.encodeHistory()).append("|");
        } else {
            sb.append("0|0|0|0|NONE|NONE|");
        }

        LoyaltyPoints lp = m.getLoyaltyObj();
        lp.normalizeHistory();
        sb.append(lp.getPoints()).append("|")
        .append(lp.encodeHistory()).append("|");

        Membership ms = m.getMembership();
        if (ms != null) {
            sb.append(ms.getTier()).append("|")
              .append(ms.getMonthlyFee()).append("|")
              .append(ms.getExpiryDate()).append("|");
        } else {
            sb.append("BASIC|60.0|NONE|");
        }

        sb.append(encodeBookingList(m.getEquipmentBookings())).append("|");
        sb.append(encodeClassBookingList(m.getClassBookings())).append("|");
        sb.append(encodeApptList(m.getAppointments())).append("|");
        sb.append(encodeOrderList(m.getOrders())).append("|");
        sb.append("0|0|0|0");
        return sb.toString();
    }

    /** Parses a full member record from the split CSV parts array. */
    private Member parseMemberFromParts(String[] p) {
        try {
            String id     = p[0].trim();
            String hash   = p[1].trim();
            String status = p[2].trim();
            String name   = p[3].trim();
            int    age    = parseInt(p[4]);
            String phone  = p[5].trim();
            String gender = p[6].trim();
            String tier   = p[7].trim();

            MyProfile profile = new MyProfile(name, age, phone, gender);
            Member m = new Member(id, hash, status, profile);
            m.setTier(tier);

            // Health profile (fields 8-13)
            if (p.length > 13 && !p[8].trim().equals("0")) {
                double weight = parseDouble(p[8]);
                double height = parseDouble(p[9]);
                double target = parseDouble(p[10]);
                double bmi    = parseDouble(p[11]);
                String goal   = p[12].trim();
                String histEnc = p.length > 13 ? p[13].trim() : "";
                HealthProfile hp = new HealthProfile(
                        weight, height, target, goal, bmi,
                        HealthProfile.decodeHistory(histEnc));
                m.setHealthProfile(hp);
            }

            // Loyalty points (fields 14-15)
            if (p.length > 15) {
                int    pts     = parseInt(p[14]);
                String lpHist  = p[15].trim();
                m.setLoyaltyPoints(new LoyaltyPoints(pts, lpHist));
            }

            // Membership (fields 16-18)
            if (p.length > 18) {
                String msTier   = p[16].trim();
                double msFee    = parseDouble(p[17]);
                String msExpiry = p[18].trim();
                m.setMembership(new Membership(msTier, msFee, msExpiry));
            }

            // Equipment bookings (field 19)
            if (p.length > 19 && !p[19].trim().isEmpty()) {
                for (String entry : p[19].trim().split(";")) {
                    if (!entry.isEmpty()) {
                        EquipmentBooking eb = EquipmentBooking.fromFileLine(entry.replace("~c~", ","));
                        if (eb != null) m.getEquipmentBookings().add(eb);
                    }
                }
                m.setNextEquipBookingNo(m.getEquipmentBookings().size() + 1);
            }

            // Class bookings (field 20)
            if (p.length > 20 && !p[20].trim().isEmpty()) {
                for (String entry : p[20].trim().split(";")) {
                    if (!entry.isEmpty()) {
                        ClassBooking cb = ClassBooking.fromFileLine(entry.replace("~c~", ","));
                        if (cb != null) m.getClassBookings().add(cb);
                    }
                }
                m.setNextClassBookingNo(m.getClassBookings().size() + 1);
            }

            // Trainer appointments (field 21)
            if (p.length > 21 && !p[21].trim().isEmpty()) {
                for (String entry : p[21].trim().split(";")) {
                    if (!entry.isEmpty()) {
                        TrainerAppointment ta = TrainerAppointment.fromFileLine(entry.replace("~c~", ","));
                        if (ta != null) m.getAppointments().add(ta);
                    }
                }
                m.setNextApptNo(m.getAppointments().size() + 1);
            }

            // Orders (field 22)
            if (p.length > 22 && !p[22].trim().isEmpty()) {
                for (String entry : p[22].trim().split(";")) {
                    if (!entry.isEmpty()) {
                        Order o = Order.fromFileLine(entry.replace("~p~", "|"));
                        if (o != null) m.getOrders().add(o);
                    }
                }
               
            }

            return m;
        } catch (Exception e) {
            System.out.println("[!] Member parse error: " + e.getMessage());
            return null;
        }
    }

    // -- LOAD ALL MEMBERS (for Admin view) ---------------------------

    public List<Member> getAllMembers() {
        List<Member> list = new ArrayList<>();
        for (String line : readAllLines(MEMBER_FILE)) {
            if (line.trim().isEmpty()) continue;
            String[] p = line.split("\\|", -1);
            if (p.length < 8) continue;
            Member m = parseMemberFromParts(p);
            if (m != null) list.add(m);
        }
        return list;
    }

    // -- MEMBER UPDATE OPERATIONS (called by Admin) -------------------

    public boolean editMemberPersonal(String memberId, String name,
                                       String phone, int age, String newPwdHash) {
        List<Member> all = getAllMembers();
        for (Member m : all) {
            if (m.getAccountId().equalsIgnoreCase(memberId)) {
                if (name != null && !name.isEmpty())
                    m.getProfile().setName(name);
                if (phone != null && !phone.isEmpty())
                    m.getProfile().setPhone(phone);
                if (age > 0)
                    m.getProfile().setAge(age);
                if (newPwdHash != null && !newPwdHash.isEmpty())
                    m.setPasswordHash(newPwdHash);
                rewriteAllMembers(all);
                return true;
            }
        }
        return false;
    }

    public boolean editMemberPhysical(String memberId,
                                       double weightKg, double heightCm) {
        List<Member> all = getAllMembers();
        for (Member m : all) {
            if (m.getAccountId().equalsIgnoreCase(memberId)) {
                String today = LocalDate.now().format(DATE_FMT);
                if (m.getHealthProfile() == null) {
                    m.setHealthProfile(new HealthProfile(weightKg, heightCm, 0, "NONE", today));
                } else {
                    if (weightKg >= 20 && weightKg <= 500) {
    m.getHealthProfile().updateWeight(weightKg, today);
}

if (heightCm >= 80 && heightCm <= 250) {
    m.getHealthProfile().updateHeight(heightCm, today);
}
                }
                rewriteAllMembers(all);
                return true;
            }
        }
        return false;
    }

    public boolean changeMemberTier(String memberId, String tier) {
        List<Member> all = getAllMembers();
        for (Member m : all) {
            if (m.getAccountId().equalsIgnoreCase(memberId)) {
                m.setTier(tier);
                if (m.getMembership() != null) m.getMembership().setTier(tier);
                rewriteAllMembers(all);
                return true;
            }
        }
        return false;
    }

    public boolean setMemberStatus(String memberId, String status) {
        List<Member> all = getAllMembers();
        for (Member m : all) {
            if (m.getAccountId().equalsIgnoreCase(memberId)) {
                m.setStatus(status);
                rewriteAllMembers(all);
                return true;
            }
        }
        return false;
    }

    private void rewriteAllMembers(List<Member> members) {
        try (FileWriter fw = new FileWriter(MEMBER_FILE, false)) {
            for (Member m : members) {
                fw.write(buildMemberLine(m) + System.lineSeparator());
            }
        } catch (IOException e) {
            System.out.println("[!] Member file rewrite failed: " + e.getMessage());
        }
    }

    // -- TRAINER PERSISTENCE ------------------------------------------

    public List<Trainer> getAllTrainers() {
        return loadTrainers();
    }

   private List<Trainer> loadTrainers() {
    List<Trainer> list = new ArrayList<>();

    for (String line : readAllLines(TRAINER_FILE)) {
        line = line.trim();
        if (line.isEmpty()) continue;

        String[] p = line.split(",", -1);   // ??????
        if (p.length < 11) continue;

        try {
            MyProfile profile = new MyProfile(
                    p[3].trim(),
                    parseInt(p[4]),
                    p[5].trim(),
                    p[6].trim()
            );

            Trainer t = new Trainer(
            p[0].trim(),
            p[1].trim(),
            p[2].trim(),
            profile,
            p[7].trim(),
            parseInt(p[8]),
            parseDouble(p[9]),
            p[10].trim(),
             0
             );

            if (p.length > 11) {
                t.setRatingCount(parseInt(p[11]));
            }

            list.add(t);

        } catch (Exception e) {
            System.out.println("[!] Trainer parse error: " + e.getMessage());
        }
    }

    return list;
}

// ========================= VALIDATION METHOD =========================
// FUNCTION : Checks whether trainer name already exists
// INPUT    : name = trainer name
// OUTPUT   : true = exists, false = not found
    public boolean trainerNameExists(String name) {
    String target = name == null ? "" : name.trim();
    if (target.isEmpty()) return false;

    for (Trainer t : loadTrainers()) {
        if (t.getProfile() != null &&
            t.getProfile().getName() != null &&
            t.getProfile().getName().trim().equalsIgnoreCase(target)) {
            return true;
        }
    }
    return false;
}

    public boolean trainerPhoneExists(String phone) {
    String target = phone == null ? "" : phone.trim();
    if (target.isEmpty()) return false;

    for (Trainer t : loadTrainers()) {
        if (t.getProfile() != null &&
            t.getProfile().getPhone() != null &&
            t.getProfile().getPhone().trim().equalsIgnoreCase(target)) {
            return true;
        }
    }
    return false;
}

// ========================= SEARCH METHOD =========================
// FUNCTION : Finds trainer by trainer ID
// INPUT    : trainerId = target trainer ID
// OUTPUT   : Returns Trainer object if found, otherwise null
    public Trainer findTrainerById(String trainerId) {
        for (Trainer t : loadTrainers()) {
            if (t.getAccountId().equalsIgnoreCase(trainerId.trim())) return t;
        }
        return null;
    }

// ========================= DATA METHOD =========================
// FUNCTION : Registers a new trainer and saves it into file
// INPUT    : name, age, phone, gender, specialty, exp, passwordPlain
// OUTPUT   : Returns the created Trainer object
// RULE     : Trainer name and phone must be unique
   public Trainer registerTrainer(String name, int age, String phone, String gender, String specialty,int exp, String passwordPlain) {
    if (trainerNameExists(name)) {
        throw new IllegalArgumentException("This trainer name is already registered.");
    }

    if (trainerPhoneExists(phone)) {
        throw new IllegalArgumentException("This trainer phone number is already registered.");
    }

    String newId   = generateNextTrainerId();
    String pwdHash = Account.md5(passwordPlain);
    MyProfile profile = new MyProfile(name.trim(), age, phone.trim(), gender.trim());

    Trainer t = new Trainer(newId, pwdHash, "ACTIVE", profile,
            specialty.trim(), exp, 0.0, "-");

    try {
        saveTrainerToFile(t);
        return t;
    } catch (IOException e) {
        throw new RuntimeException("Failed to save trainer data.");
    }
}

// ========================= FILE METHOD =========================
// FUNCTION : Saves one trainer record into trainer file
// INPUT    : t = trainer object
// OUTPUT   : No return
    private void saveTrainerToFile(Trainer t) throws IOException {
        String row = trainerToLine(t);
        try (FileWriter fw = new FileWriter(TRAINER_FILE, true)) {
            fw.write(row + System.lineSeparator());
        }
    }
    
// ========================= FILE METHOD =========================
// FUNCTION : Converts trainer object into one line for file storage
// INPUT    : t = trainer object
// OUTPUT   : Returns a comma-separated string
    private String trainerToLine(Trainer t) {
        return t.getAccountId() + "," + t.getPasswordHash() + ","
             + t.getStatus() + "," + t.getProfile().getName() + ","
             + t.getProfile().getAge() + "," + t.getProfile().getPhone() + ","
             + t.getProfile().getGender() + "," + t.getSpecialty() + ","
             + t.getExperienceYears() + "," + t.getRatingAvg() + ","
             + t.getMotto() + "," + t.getRatingCount();
    }

    public boolean deleteTrainer(String trainerId) {
        List<Trainer> list = loadTrainers();
        boolean removed = list.removeIf(
                t -> t.getAccountId().equalsIgnoreCase(trainerId.trim()));
        if (!removed) return false;
        rewriteTrainers(list);
        return true;
    }

    public boolean setTrainerStatus(String trainerId, String status) {
        List<Trainer> list = loadTrainers();
        for (Trainer t : list) {
            if (t.getAccountId().equalsIgnoreCase(trainerId.trim())) {
                t.setStatus(status);
                t.setAvailabilityStatus(status);
                rewriteTrainers(list);
                return true;
            }
        }
        return false;
    }

    public boolean editTrainer(String trainerId, String name, String phone,String specialty, int expYears, String motto) {
        List<Trainer> list = loadTrainers();
        for (Trainer t : list) {
            if (t.getAccountId().equalsIgnoreCase(trainerId.trim())) {
                if (name      != null && !name.isEmpty())      t.getProfile().setName(name);
                if (phone     != null && !phone.isEmpty())     t.getProfile().setPhone(phone);
                if (specialty != null && !specialty.isEmpty()) t.setSpecialty(specialty);
                if (expYears  > 0)                             t.setExperienceYears(expYears);
                if (motto     != null && !motto.isEmpty())     t.editMotto(motto);
                rewriteTrainers(list);
                return true;
            }
        }
        return false;
    }

    public boolean updateTrainerRating(String trainerId, double newRating) {
        List<Trainer> list = loadTrainers();
        for (Trainer t : list) {
            if (t.getAccountId().equalsIgnoreCase(trainerId.trim())) {
                t.addRating(newRating);
                rewriteTrainers(list);
                return true;
            }
        }
        return false;
    }

// ========================= FILE METHOD =========================
// FUNCTION : Rewrites the entire trainer file with updated data
// INPUT    : list = latest trainer list
// OUTPUT   : No return
    private void rewriteTrainers(List<Trainer> list) {
        try (FileWriter fw = new FileWriter(TRAINER_FILE, false)) {
            for (Trainer t : list) fw.write(trainerToLine(t) + System.lineSeparator());
        } catch (IOException e) {
            System.out.println("[!] Trainer file rewrite failed: " + e.getMessage());
        }
    }

    // -- EQUIPMENT PERSISTENCE ----------------------------------------

    public List<Equipment> getAllEquipment() {
        equipments = loadEquipment();
        return equipments;
    }
    
// ========================= FILE METHOD =========================
// FUNCTION : Loads all equipment data from equipment file
// OUTPUT   : List of Equipment objects
    private List<Equipment> loadEquipment() {
        List<Equipment> list = new ArrayList<>();
        for (String line : readAllLines(EQUIPMENT_FILE)) {
            if (line.trim().isEmpty()) continue;
            String[] p = line.split(",", -1);
            if (p.length < 5) continue;
            list.add(new Equipment(p[0].trim(), p[1].trim(),
                    p[2].trim(), p[3].trim(), p[4].trim()));
        }
        return list;
    }

    public Equipment addEquipment(String name, String zone,String category, String status) {
        List<Equipment> list = getAllEquipment();
        String newId = String.format("E%03d", list.size() + 1);
        Equipment eq = new Equipment(newId, name, zone, category, status);
        list.add(eq);
        rewriteEquipment(list);
        return eq;
    }

    public boolean updateEquipmentStatus(String equipmentId, String status) {
        List<Equipment> list = getAllEquipment();
        for (Equipment eq : list) {
            if (eq.getEquipmentId().equalsIgnoreCase(equipmentId.trim())) {
                eq.setStatus(status);
                rewriteEquipment(list);
                return true;
            }
        }
        return false;
    }
    
// ========================= FILE METHOD =========================
// FUNCTION : Rewrites equipment file with latest equipment list
// INPUT    : list = updated equipment list
// OUTPUT   : No return
    private void rewriteEquipment(List<Equipment> list) {
        try (FileWriter fw = new FileWriter(EQUIPMENT_FILE, false)) {
            for (Equipment eq : list) {
                fw.write(eq.getEquipmentId() + "," + eq.getName() + ","
                       + eq.getZone() + "," + eq.getCategory() + ","
                       + eq.getStatus() + System.lineSeparator());
            }
        } catch (IOException e) {
            System.out.println("[!] Equipment file rewrite failed: " + e.getMessage());
        }
    }

    // -- CLASS SESSIONS -----------------------------------------------

    public List<ClassSession> getAllClassSessions() {
        classSessions = loadClassSessions();
        return classSessions;
    }
// ========================= FILE METHOD =========================
// FUNCTION : Loads all class sessions from class file
// OUTPUT   : List of ClassSession objects
    private List<ClassSession> loadClassSessions() {
        List<ClassSession> list = new ArrayList<>();
        for (String line : readAllLines(CLASS_FILE)) {
            if (line.trim().isEmpty()) continue;
            ClassSession cs = ClassSession.fromFileLine(line);
            if (cs != null) list.add(cs);
        }
        return list;
    }
    
// ========================= FILE METHOD =========================
// FUNCTION : Saves all class sessions into class file
// INPUT    : list = updated class session list
// OUTPUT   : No return
    public void saveClassSessions(List<ClassSession> list) {
        try (FileWriter fw = new FileWriter(CLASS_FILE, false)) {
            for (ClassSession cs : list)
                fw.write(cs.toFileLine() + System.lineSeparator());
        } catch (IOException e) {
            System.out.println("[!] Class file write failed: " + e.getMessage());
        }
    }

    // -- SHOP / PRODUCT PERSISTENCE -----------------------------------

    public List<Product> getAllProducts() {
        return shopCatalog.getAllProducts();
    }

    public ShopCatalog getShopCatalog() { 
    	return shopCatalog;
    	 }
    	 
// ========================= FILE METHOD =========================
// FUNCTION : Loads product data from product file
// OUTPUT   : List of Product objects
    private List<Product> loadProducts() {
        List<Product> list = new ArrayList<>();
        for (String line : readAllLines(PRODUCT_FILE)) {
            if (line.trim().isEmpty()) continue;
            Product p = Product.fromFileLine(line);
            if (p != null) list.add(p);
        }
        return list;
    }

    public boolean addProduct(Product p) {
        String newId = shopCatalog.generateNextProductId();
        Product np = new Product(newId, p.getName(), p.getBrand(),
                p.getCategory(), p.getPrice(), p.getStock());
        shopCatalog.addProduct(np);
        saveProducts();
        return true;
    }

    public boolean editProduct(String productId, String name,
                                double price, int stock) {
        Product p = shopCatalog.getProductById(productId);
        if (p == null) return false;
        if (name  != null && !name.isEmpty()) p.setName(name);
        if (price >= 10.0 && price <= 5000.0) {
         p.setPrice(price);
}
        if (stock >= 0)                       p.setStock(stock);
        saveProducts();
        return true;
    }

    public boolean deleteProduct(String productId) {
        boolean ok = shopCatalog.removeProduct(productId);
        if (ok) saveProducts();
        return ok;
    }

    /** Public wrapper so Main can persist stock deductions after a shop purchase. */
    public void saveProductsPublic() { 
    	saveProducts(); 
    		}

// ========================= FILE METHOD =========================
// FUNCTION : Saves all products into product file
// OUTPUT   : No return
    private void saveProducts() {
        try (FileWriter fw = new FileWriter(PRODUCT_FILE, false)) {
            for (Product p : shopCatalog.getAllProducts())
                fw.write(p.toFileLine() + System.lineSeparator());
        } catch (IOException e) {
            System.out.println("[!] Product file write failed: " + e.getMessage());
        }
    }

    // -- ORDER PERSISTENCE --------------------------------------------

    public List<Order> getAllOrders() {
        List<Order> list = new ArrayList<>();
        for (String line : readAllLines(ORDER_FILE)) {
            if (line.trim().isEmpty()) continue;
            Order o = Order.fromFileLine(line);
            if (o != null) list.add(o);
        }
        return list;
    }
    
// ========================= FILE METHOD =========================
// FUNCTION : Saves one order into order file
// INPUT    : o = order object
// OUTPUT   : No return
    public void saveOrder(Order o) {
        try (FileWriter fw = new FileWriter(ORDER_FILE, true)) {
            fw.write(o.toFileLine() + System.lineSeparator());
        } catch (IOException e) {
            System.out.println("[!] Order save failed: " + e.getMessage());
        }
    }

    public boolean updateOrderStatus(String orderId, String status) {
        List<Order> list = getAllOrders();
        boolean found = false;
        for (Order o : list) {
            if (o.getOrderId().equalsIgnoreCase(orderId.trim())) {
                o.setStatus(status);
                found = true;
            }
        }
        if (!found) return false;
        try (FileWriter fw = new FileWriter(ORDER_FILE, false)) {
            for (Order o : list) fw.write(o.toFileLine() + System.lineSeparator());
        } catch (IOException e) {
            System.out.println("[!] Order rewrite failed: " + e.getMessage());
        }
        return true;
    }

    // -- AUDIT --------------------------------------------------------

    public void logAudit(String action, String actor, String target, String detail) {
        auditLog.logAction(action, actor, target, detail);
    }

    public List<String> viewAuditTrail() {
        return auditLog.viewAuditTrail();
    }

    public List<String> filterAuditTrail(String date) {
        return auditLog.filterAuditTrail(date);
    }

// -- REVENUE REPORT -----------------------------------------------
// FUNCTION : Generates monthly revenue report
// INPUT    : month, year
// OUTPUT   : Returns formatted revenue report as String
public String generateRevenueReport(int month, int year) {
    List<Member>  members  = getAllMembers();
    List<Trainer> trainers = getAllTrainers();
    List<Order>   orders   = getAllOrders();

    double membershipRev = getMembershipRevenueFromAudit(month, year);
    double shopRev = 0.0;
    double ptRev = 0.0;

    int totalBookings = 0;
    int totalPT = 0;
    int totalCancellations = 0;

    for (Member m : members) {
        for (ClassBooking cb : m.getClassBookings()) {
            try {
                String line = cb.toFileLine();
                String[] p = line.split(",", -1);

                if (p.length > 1) {
                    LocalDate d = LocalDate.parse(p[1].trim(), DATE_FMT);

                    if (d.getMonthValue() == month && d.getYear() == year) {
                        totalBookings++;
                        if (ClassBooking.CANCELLED.equalsIgnoreCase(cb.getStatus())) {
                            totalCancellations++;
                        }
                    }
                }
            } catch (Exception ignored) {
            }
        }

        for (TrainerAppointment ta : m.getAppointments()) {
            try {
                LocalDate d = LocalDate.parse(ta.getDate(), DATE_FMT);
                if (d.getMonthValue() == month && d.getYear() == year) {
                    if (!TrainerAppointment.CANCELLED.equalsIgnoreCase(ta.getStatus())) {
                        totalPT++;
                    }
                }
            } catch (Exception ignored) {
            }
        }
    }

    for (Order o : orders) {
        try {
            LocalDate d = LocalDate.parse(o.getDate(), DATE_FMT);
            if (d.getMonthValue() == month && d.getYear() == year) {
                if (!Order.CANCELLED.equalsIgnoreCase(o.getStatus())) {
                    shopRev += o.getTotalPaid();
                }
            }
        } catch (Exception ignored) {
        }
    }

    double total = membershipRev + shopRev + ptRev;

    StringBuilder sb = new StringBuilder();
    sb.append("=========================================\n");
    sb.append(String.format("MONTHLY REVENUE REPORT (%02d/%d)%n", month, year));
    sb.append("=========================================\n");
    sb.append("TOTAL MEMBERS   : ").append(members.size()).append("\n");
    sb.append("ACTIVE TRAINERS : ").append(
            trainers.stream()
                    .filter(t -> "ACTIVE".equalsIgnoreCase(t.getStatus()))
                    .count()
    ).append("\n");

    sb.append("--------------------------------------------------------------------\n");
    sb.append("TRAINER ACTIVITY SUMMARY:\n");
    sb.append(String.format("%-20s %-10s %-10s%n", "TRAINER", "CLIENTS", "RATING"));

    for (Trainer t : trainers) {
        long clients = getAllMembers().stream()
            .flatMap(m -> m.getAppointments().stream())
            .filter(a -> a.getTrainerId().equalsIgnoreCase(t.getAccountId())
                      && !TrainerAppointment.CANCELLED.equalsIgnoreCase(a.getStatus()))
            .count();

        sb.append(String.format("%-20s %-10d %-10.1f%n",
                t.getProfile().getName(),
                clients,
                t.getRatingAvg()));
    }

    sb.append("--------------------------------------------------------------------\n");
    sb.append("STUDENT ENGAGEMENT SUMMARY:\n");
    sb.append("- Total Class Bookings : ").append(totalBookings).append("\n");
    sb.append("- Total PT Sessions    : ").append(totalPT).append("\n");
    sb.append("- Cancellations        : ").append(totalCancellations).append("\n");

    sb.append("--------------------------------------------------------------------\n");
    sb.append("REVENUE BREAKDOWN:\n");
    sb.append(String.format("- Membership Fees : RM %.2f%n", membershipRev));
    sb.append(String.format("- Shop Sales      : RM %.2f%n", shopRev));

    if (ptRev > 0) {
        sb.append(String.format("- PT Revenue      : RM %.2f%n", ptRev));
    }

    sb.append("=========================================\n");
    sb.append(String.format("TOTAL REVENUE     : RM %.2f%n", total));
    sb.append("=========================================\n");

    return sb.toString();
}


// ========================= HELPER METHOD =========================
// FUNCTION : Calculates membership revenue from audit log
// INPUT    : month, year
// OUTPUT   : Total membership revenue
        private double getMembershipRevenueFromAudit(int month, int year) {
    double total = 0.0;

    for (String line : viewAuditTrail()) {
        if (line == null || line.trim().isEmpty()) continue;

        try {
            String[] parts = line.split("\\|");
            if (parts.length < 5) continue;

            String timePart = parts[0].trim();   // example: 2026-03-31 18:25:19
            LocalDate d = LocalDate.parse(timePart.substring(0, 10)); // yyyy-MM-dd

            if (d.getYear() != year || d.getMonthValue() != month) continue;

            String action = "";
            String detail = "";

            for (int i = 1; i < parts.length; i++) {
                String p = parts[i].trim();

                if (p.startsWith("ACTION=")) {
                    action = p.substring("ACTION=".length()).trim();
                } else if (p.startsWith("DETAIL=")) {
                    detail = p.substring("DETAIL=".length()).trim();
                }
            }

            if ("UPGRADE_MEMBERSHIP".equalsIgnoreCase(action)) {
                if (detail.toUpperCase().contains("GOLD")) {
                    total += 250.0;
                } else if (detail.toUpperCase().contains("SILVER")) {
                    total += 150.0;
                } else if (detail.toUpperCase().contains("BASIC")) {
                    total += 60.0;
                }
            }
        } catch (Exception ignored) {
        }
    }

    return total;
}
// ========================= HELPER METHOD =========================
// FUNCTION : Checks whether shop revenue exists in a given month
// INPUT    : month, year
// OUTPUT   : true = revenue exists, false = none
private boolean hasShopRevenueInMonth(int month, int year) {
    for (Order o : getAllOrders()) {
        try {
            LocalDate d = LocalDate.parse(o.getDate(), DATE_FMT);
            if (d.getYear() == year
                    && d.getMonthValue() == month
                    && !Order.CANCELLED.equalsIgnoreCase(o.getStatus())) {
                return true;
            }
        } catch (Exception ignored) {
        }
    }
    return false;
}

// ========================= HELPER METHOD =========================
// FUNCTION : Checks whether membership revenue exists in a given month
// INPUT    : month, year
// OUTPUT   : true = revenue exists, false = none
private boolean hasMembershipRevenueInMonth(int month, int year) {
    return getMembershipRevenueFromAudit(month, year) > 0;
}

// ========================= DATA METHOD =========================
// FUNCTION : Returns months that contain revenue data for selected year
// INPUT    : year
// OUTPUT   : List of available months
public List<Integer> getAvailableRevenueMonths(int year) {
    List<Integer> months = new ArrayList<Integer>();

    for (int m = 1; m <= 12; m++) {
        if (hasShopRevenueInMonth(m, year) || hasMembershipRevenueInMonth(m, year)) {
            months.add(m);
        }
    }

    return months;
}
   

    // -- DEFAULT DATA SEED --------------------------------------------
    /**
     * Seeds default trainers, equipment, class sessions, and products
     * if their files do not exist yet. This ensures the system works
     * out-of-the-box matching the draft output.
     */
    private void seedDefaultData() {
        seedTrainers();
        seedEquipment();
        seedClasses();
        seedProducts();
    }

    private void seedTrainers() {
        if (new File(TRAINER_FILE).exists()) return;
        String[][] data = {
            {"T0001","ACTIVE","Sarah Connor","F","32","0123456789","Yoga / Hatha, Stress Relief","5","4.9","Find your inner peace"},
            {"T0002","ACTIVE","John Doe",    "M","28","0129998888","HIIT / Fat Loss, High Intensity","3","4.7","No pain, no gain!"},
            {"T0003","ACTIVE","Maria Santos","F","30","0126665555","Zumba / Latin Dance, Aerobics","4","5.0","Dance like nobody's watching"},
            {"T0004","ACTIVE","Mike Lim",    "M","40","0124443333","Pilates / Core, Injury Rehab","8","4.8","Control is power"},
        };
        try (FileWriter fw = new FileWriter(TRAINER_FILE, false)) {
            String[] ids = {"T0001","T0002","T0003","T0004"};
            String pwdHash = Account.md5("Trainer@1");
            for (int i = 0; i < data.length; i++) {
                String[] d = data[i];
                // id,hash,status,name,age,phone,gender,specialty,exp,rating,motto,ratingCount
                fw.write(ids[i] + "," + pwdHash + "," + d[1] + ","
                       + d[2] + ",25," + d[4] + "," + d[3] + ","
                       + d[6] + "," + d[7] + "," + d[8] + ","
                       + d[9] + ",0" + System.lineSeparator());
            }
        } catch (IOException e) {
            System.out.println("[!] Trainer seed failed: " + e.getMessage());
        }
    }

    private void seedEquipment() {
        if (new File(EQUIPMENT_FILE).exists()) return;
        String[][] eq = {
            {"E001","Treadmill",        "Window Area",   "CARDIO",   "AVAILABLE"},
            {"E002","Treadmill",        "Window Area",   "CARDIO",   "IN_USE"},
            {"E003","Elliptical Trainer","Window Area",  "CARDIO",   "AVAILABLE"},
            {"E004","Elliptical Trainer","Window Area",  "CARDIO",   "AVAILABLE"},
            {"E005","Spinning Bike",    "Cycle Studio",  "CARDIO",   "MAINTENANCE"},
            {"E006","Spinning Bike",    "Cycle Studio",  "CARDIO",   "AVAILABLE"},
            {"E007","Smith Machine",    "Free Weights",  "STRENGTH", "IN_USE"},
            {"E008","Smith Machine",    "Free Weights",  "STRENGTH", "AVAILABLE"},
            {"E009","Leg Press Machine","Strength Zone", "STRENGTH", "AVAILABLE"},
            {"E010","Cable Cross Station","Strength Zone","STRENGTH","AVAILABLE"},
        };
        try (FileWriter fw = new FileWriter(EQUIPMENT_FILE, false)) {
            for (String[] e : eq)
                fw.write(e[0]+","+e[1]+","+e[2]+","+e[3]+","+e[4]+System.lineSeparator());
        } catch (IOException ex) {
            System.out.println("[!] Equipment seed failed: " + ex.getMessage());
        }
    }

    private void seedClasses() {
        if (new File(CLASS_FILE).exists()) return;
        // classNo,day,time,className,location,slotUsed,slotLimit,trainerId,trainerName
        String[] lines = {
            "1,MON,10:00 AM,YOGA,Mind Studio,5,20,T0001,Sarah Connor",
            "2,MON,02:00 PM,HIIT,Gym Floor,20,20,T0002,John Doe",
            "3,TUE,06:00 PM,ZUMBA,Dance Studio A,12,25,T0003,Maria Santos",
            "4,WED,10:00 AM,PILATES,Mind Studio,8,15,T0004,Mike Lim",
        };
        try (FileWriter fw = new FileWriter(CLASS_FILE, false)) {
            for (String l : lines) fw.write(l + System.lineSeparator());
        } catch (IOException e) {
            System.out.println("[!] Class seed failed: " + e.getMessage());
        }
    }

    private void seedProducts() {
        if (new File(PRODUCT_FILE).exists()) return;
        String[][] prods = {
            {"P001","Resistance Band Set",        "FitPro",     "FITNESS_EQUIPMENT","35.00", "50"},
            {"P002","Adjustable Dumbbell 5KG",    "IronMax",    "FITNESS_EQUIPMENT","120.00","30"},
            {"P003","Yoga Mat (6mm)",              "ZenFlex",    "FITNESS_EQUIPMENT","55.00", "22"},
            {"P004","Jump Rope Pro",               "SpeedFit",   "FITNESS_EQUIPMENT","28.00", "45"},
            {"P005","Pull-Up Bar (Doorframe)",     "GripKing",   "FITNESS_EQUIPMENT","75.00", "20"},
            {"P006","Foam Roller",                 "RecoveryX",  "FITNESS_EQUIPMENT","45.00", "0"},
            {"P007","Whey Protein 1KG",            "MuscleCore", "SUPPLEMENTS",      "150.00","42"},
            {"P008","BCAA (Berry Flavour)",        "RecoverPro", "SUPPLEMENTS",      "89.00", "9"},
            {"P009","Creatine Monohydrate",        "PowerBase",  "SUPPLEMENTS",      "75.00", "2"},
            {"P010","Multivitamin (30 Tabs)",      "VitaMax",    "SUPPLEMENTS",      "42.00", "55"},
            {"P011","Pre-Workout Energy",          "BlastFuel",  "SUPPLEMENTS",      "98.00", "1"},
        };
        try (FileWriter fw = new FileWriter(PRODUCT_FILE, false)) {
            for (String[] p : prods)
                fw.write(p[0]+","+p[1]+","+p[2]+","+p[3]+","+p[4]+","+p[5]+System.lineSeparator());
        } catch (IOException e) {
            System.out.println("[!] Product seed failed: " + e.getMessage());
        }
        shopCatalog = new ShopCatalog(loadProducts());
    }


// ========================= HELPER METHOD =========================
// FUNCTION : Generates next ID based on existing file records
// INPUT    : filePath, prefix
// OUTPUT   : Next formatted ID
    private String generateNextId(String filePath, String prefix) {
        int max = 0;
        for (String line : readAllLines(filePath)) {
            if (line.trim().isEmpty()) continue;
            String[] p = line.split("\\|", -1);
            if (p.length >= 1) {
                String id = p[0].trim();
                if (id.startsWith(prefix)) {
                    try {
                        int n = Integer.parseInt(id.substring(prefix.length()));
                        if (n > max) max = n;
                    } catch (Exception ignored) {}
                }
            }
        }
        return String.format(prefix + "%04d", max + 1);
    }
    
// ========================= FILE METHOD =========================
// FUNCTION : Reads all lines from target file
// INPUT    : filePath
// OUTPUT   : List of file lines
    private List<String> readAllLines(String filePath) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) lines.add(line);
        } catch (IOException ignored) {}
        return lines;
    }
    
// ========================= HELPER METHOD =========================
    // FUNCTION : Safely parses string into integer
    private int    parseInt(String s)    { try { return Integer.parseInt(s.trim()); } catch (Exception e) { return 0; } }
    // FUNCTION : Safely parses string into double
    private double parseDouble(String s) { try { return Double.parseDouble(s.trim()); } catch (Exception e) { return 0.0; } }


   // ========================= HELPER METHOD =========================
    // FUNCTION : Encodes equipment booking list into single string for file storage
    // Encode lists using semicolons; commas within entries become ~c~
    private String encodeBookingList(List<EquipmentBooking> list) {
        StringBuilder sb = new StringBuilder();
        for (EquipmentBooking eb : list) {
            if (sb.length() > 0) sb.append(";");
            sb.append(eb.toFileLine().replace(",", "~c~"));
        }
        return sb.toString();
    }
     
 // ========================= HELPER METHOD =========================
// FUNCTION : Encodes class booking list into single string for file storage
    private String encodeClassBookingList(List<ClassBooking> list) {
        StringBuilder sb = new StringBuilder();
        for (ClassBooking cb : list) {
            if (sb.length() > 0) sb.append(";");
            sb.append(cb.toFileLine().replace(",", "~c~"));
        }
        return sb.toString();
    }

// ========================= HELPER METHOD =========================
// FUNCTION : Encodes trainer appointment list into single string for file storage
    private String encodeApptList(List<TrainerAppointment> list) {
        StringBuilder sb = new StringBuilder();
        for (TrainerAppointment ta : list) {
            if (sb.length() > 0) sb.append(";");
            sb.append(ta.toFileLine().replace(",", "~c~"));
        }
        return sb.toString();
    }
    
// ========================= HELPER METHOD =========================
// FUNCTION : Encodes order list into single string for file storage
    private String encodeOrderList(List<Order> list) {
        StringBuilder sb = new StringBuilder();
        for (Order o : list) {
            if (sb.length() > 0) sb.append(";");
            // Orders use | inside - encode | as ~p~
            sb.append(o.toFileLine().replace("|", "~p~"));
        }
        return sb.toString();
    }
    
    
    // ========================= HELPER METHOD =========================
// FUNCTION : Generates next trainer ID
// OUTPUT   : New trainer ID in T0001 format
    private String generateNextTrainerId() {
    int max = 0;

    for (String line : readAllLines(TRAINER_FILE)) {
        if (line.trim().isEmpty()) continue;

        String[] p = line.split(",", -1);   // trainer file uses comma
        if (p.length < 1) continue;

        String id = p[0].trim();
        if (id.matches("T\\d{4}")) {
            try {
                int n = Integer.parseInt(id.substring(1));
                if (n > max) max = n;
            } catch (Exception e) {
                // ignore bad row
            }
        }
    }

    return String.format("T%04d", max + 1);
}



// ========================= HELPER METHOD =========================
// FUNCTION : Generates next order ID
// OUTPUT   : New order ID in ORD-YYYY-0001 format
public String generateNextOrderId() {
    List<Order> orders = getAllOrders();
    int max = 0;
    int year = LocalDate.now().getYear();

    for (Order o : orders) {
        String oid = o.getOrderId();
        if (oid == null) continue;

        String[] parts = oid.split("-");
        if (parts.length == 3) {
            try {
                int num = Integer.parseInt(parts[2]);
                if (num > max) max = num;
            } catch (Exception e) {}
        }
    }

    return String.format("ORD-%d-%04d", year, max + 1);
}



// ========================= SEARCH METHOD =========================
// FUNCTION : Returns all orders that belong to a member name
// INPUT    : memberName
// OUTPUT   : List of matching Order objects
   public List<Order> getOrdersByMemberName(String memberName) {
    List<Order> all = getAllOrders();
    List<Order> result = new ArrayList<>();

    for (Order o : all) {
        if (o.getMemberName() != null &&
            o.getMemberName().equalsIgnoreCase(memberName.trim())) {
            result.add(o);
        }
    }

    return result;
}
}