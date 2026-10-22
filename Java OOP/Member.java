import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * - Manages bookings (equipment, class, trainer)
 * - Manages health profile and BMI tracking
 * - Handles loyalty points and rewards
 * - Supports shopping and order tracking
 * - Handles membership upgrade
 */
public class Member extends Account {

    private static final DateTimeFormatter DATE_FMT =DateTimeFormatter.ofPattern("dd-MM-yyyy");

    // -- TIER CONSTANTS -----------------------------------------------
    public static final String TIER_BASIC  = "BASIC";
    public static final String TIER_SILVER = "SILVER";
    public static final String TIER_GOLD   = "GOLD";

    // -- ATTRIBUTES ----------------------------------------------
    private int           age;           // kept in Member per UML (separate from MyProfile)
    private String        tier;
    private LoyaltyPoints loyaltyPoints;
    private Membership    membership;
    private HealthProfile healthProfile;

    private List<EquipmentBooking>  equipmentBookings;
    private List<ClassBooking>      classBookings;
    private List<TrainerAppointment> appointments;
    private List<Order>             orders;

    // Composition: Member "has a" MyProfile
    private MyProfile profile;

    // Internal booking-number counters (per member session; persisted in member file)
    private int nextEquipBookingNo  = 1;
    private int nextClassBookingNo  = 1;
    private int nextApptNo          = 1;
   

    // -- CONSTRUCTOR --------------------------------------------------
    /**
     * Proper super() call - passes accountId, passwordHash and status up to Account.
     */
    public Member(String memberId, String passwordHash,String accountStatus, MyProfile profile) {
        super(memberId, passwordHash, accountStatus);   // <- fixes the missing super()
        this.profile          = profile;
        this.age              = profile.getAge();
        this.tier             = TIER_BASIC;
        this.loyaltyPoints    = new LoyaltyPoints();
        this.membership       = new Membership(TIER_BASIC);
        this.equipmentBookings = new ArrayList<>();
        this.classBookings    = new ArrayList<>();
        this.appointments     = new ArrayList<>();
        this.orders           = new ArrayList<>();
    }



    // --- PROFILE SETUP -----------------------------------------------

    /**
     * +setupProfile(weightKg:double, heightCm:double, targetKg:double, goal:String) : void
     * Creates the HealthProfile on first login.
     */
    public void setupProfile(double weightKg, double heightCm,
                             double targetKg, String goal) {
        String today = LocalDate.now().format(DATE_FMT);
        this.healthProfile = new HealthProfile(weightKg, heightCm, targetKg, goal, today);
    }

    // --- EQUIPMENT ---------------------------------------------------

    /**
     * +viewEquipment(date:String) : List<Equipment>
     * Returns the full equipment list (date parameter reserved for future slot-based filtering).
     */
    public List<Equipment> viewEquipment(String date, List<Equipment> allEquipment) {
        return allEquipment;
    }

    /**
     * +bookEquipment(equipmentNo:int, date:String, startTime:String) : EquipmentBooking
     * equipmentNo is 1-based index into allEquipment.
     * Duration is fixed at 2 hours per draft specification.
     */
    public EquipmentBooking bookEquipment(int equipmentNo, String date,
                                          String startTime,
                                          List<Equipment> allEquipment) {
        if (equipmentNo < 1 || equipmentNo > allEquipment.size()) return null;
        Equipment eq = allEquipment.get(equipmentNo - 1);

        if (!eq.isAvailable()) return null;   // MAINTENANCE or IN_USE

        String endTime = addTwoHours(startTime);
        EquipmentBooking eb = new EquipmentBooking(
                nextEquipBookingNo++,
                eq.getEquipmentId(), eq.getName(),
                date, startTime, endTime);
        equipmentBookings.add(eb);
        return eb;
    }

    /**
     * +cancelEquipmentBooking(bookingNo:int) : boolean
     */
    public boolean cancelEquipmentBooking(int bookingNo) {
        for (EquipmentBooking eb : equipmentBookings) {
            if (eb.getBookingNo() == bookingNo) {
                return eb.cancel();
            }
        }
        return false;
    }

    // --- CLASSES -----------------------------------------------------

    /**
     * +viewWeeklyClassSchedule() : List<ClassSession>
     * Returns all class sessions (caller filters by week if needed).
     */
    public List<ClassSession> viewWeeklyClassSchedule(List<ClassSession> all) {
        return all;
    }

    /**
     * +bookClass(classNo:int) : ClassBooking
     * Pre-condition: tier must be SILVER or GOLD.
     */
    public ClassBooking bookClass(int classNo, List<ClassSession> allSessions) {
        if (TIER_BASIC.equals(tier)) {
            System.out.println("[!] Class booking requires SILVER or GOLD tier.");
            return null;
        }
        ClassSession cs = findSession(classNo, allSessions);
        if (cs == null || !cs.hasAvailableSlot()) return null;

        cs.reserveSlot();
        ClassBooking cb = new ClassBooking(
                nextClassBookingNo++,
                cs.getClassNo(), cs.getClassName(),
                cs.getDay(), cs.getTime(),
                cs.getTrainerName(), cs.getLocation());
        classBookings.add(cb);
        return cb;
    }

    /**
     * +cancelClassBooking(bookingNo:int) : boolean
     */
    public boolean cancelClassBooking(int bookingNo, List<ClassSession> allSessions) {
        for (ClassBooking cb : classBookings) {
            if (cb.getBookingNo() == bookingNo && !ClassBooking.CANCELLED.equals(cb.getStatus())) {
                cb.cancel();
                // Release the slot in the session
                ClassSession cs = findSession(cb.getClassNo(), allSessions);
                if (cs != null) cs.releaseSlot();
                return true;
            }
        }
        return false;
    }

    // --- TRAINER / APPOINTMENTS --------------------------------------

    /**
     * +viewTrainerList() : List<Trainer>
     */
    public List<Trainer> viewTrainerList(List<Trainer> all) {
        return all;
    }

    /**
     * +bookTrainer(trainerNo:int, date:String, startTime:String) : TrainerAppointment
     * {pre: tier = GOLD}
     */
    public TrainerAppointment bookTrainer(int trainerNo, String date,
                                          String startTime, List<Trainer> allTrainers) {
        if (!TIER_GOLD.equals(tier)) {
            System.out.println("[!] Trainer booking requires GOLD tier.");
            return null;
        }
        if (trainerNo < 1 || trainerNo > allTrainers.size()) return null;
        Trainer t = allTrainers.get(trainerNo - 1);

        String endTime = addTwoHours(startTime);
        TrainerAppointment ta = new TrainerAppointment(
                nextApptNo++,
                t.getAccountId(), t.getProfile().getName(),
                t.getSpecialty(), date, startTime, endTime);
        appointments.add(ta);
        return ta;
    }

    /**
     * +cancelAppointment(appointmentNo:int) : boolean
     */
    public boolean cancelAppointment(int appointmentNo) {
        for (TrainerAppointment ta : appointments) {
            if (ta.getAppointmentNo() == appointmentNo) {
                return ta.cancel();
            }
        }
        return false;
    }

    // --- BMI / HEALTH ------------------------------------------------

    /**
     * +viewBMIProgress() : String
     * Returns a formatted BMI history table.
     */
    public String viewBMIProgress() {
    if (healthProfile == null) return "No health profile set up.";
    StringBuilder sb = new StringBuilder();

    sb.append(String.format("%-5s %-12s %-12s %-12s %-8s %-15s%n",
            "NO", "DATE", "WEIGHT", "HEIGHT", "BMI", "STATUS"));
    sb.append("-".repeat(70)).append("\n");

    int i = 1;

    for (String[] row : healthProfile.getParsedHistory()) {
        String status = bmiStatus(Double.parseDouble(row[3]));
        sb.append(String.format("%-5d %-12s %-12s %-12s %-8s %-15s%n",
                i++, row[0], row[1] + " KG", row[2] + " CM", row[3], status));
    }

    return sb.toString();
}

    /**
     * +updateBMI(updateType:String, value:double) : void
     * updateType: "WEIGHT" | "HEIGHT" | "TARGET"
     */
    public void updateBMI(String updateType, double value) {
        if (healthProfile == null) {
            System.out.println("[!] No health profile. Please complete profile setup first.");
            return;
        }
        String today = LocalDate.now().format(DATE_FMT);
        switch (updateType.toUpperCase()) {
            case "WEIGHT": healthProfile.updateWeight(value, today);       break;
            case "HEIGHT": healthProfile.updateHeight(value, today);       break;
            case "TARGET": healthProfile.updateTargetWeight(value, today); break;
            default:
                System.out.println("[!] Unknown update type: " + updateType);
        }
    }

    // --- ACCOUNT -----------------------------------------------------

    /**
     * +deleteAccount() : void
     * Sets status to DISABLED. Admin can do a hard BLOCKED/DELETED.
     */
    public void deleteAccount() {
        setStatus("DISABLED");
        System.out.println("Account has been disabled.");
    }

    // --- RATING ------------------------------------------------------

    /**
     * +rateTrainer(trainerNo:int, rating:double) : void
     * trainerNo is 1-based index into the trainer list.
     * rating must be 0.0-5.0.
     */
    public void rateTrainer(int trainerNo, double rating, List<Trainer> allTrainers) {
        if (trainerNo < 1 || trainerNo > allTrainers.size()) return;
        if (rating < 0.0 || rating > 5.0) return;
        allTrainers.get(trainerNo - 1).addRating(rating);
    }

    // --- SHOP --------------------------------------------------------

    /**
     * +shopPurchase(productNo:int, qty:int, method:String) : Order
     * productNo is 1-based index into category list passed from catalog.
     * method: "E_WALLET" | "CASH"
     */
   public Order shopPurchase(Product product, int qty,String method, ShopCatalog catalog,FitnessClubSystem system) {
        if (product == null) return null;
        if (!product.isInStock(qty)) {
            System.out.println("[!] Insufficient stock.");
            return null;
        }
        product.deductStock(qty);

       String today    = LocalDate.now().format(DATE_FMT);
       String orderId  = system.generateNextOrderId();
       String summary  = product.getName() + " x" + qty;
       double subTotal = product.getPrice() * qty;

        Order order = new Order(orderId, today, profile.getName(),summary, subTotal, method);
        orders.add(order);

        // Award loyalty points: 1 point per RM spent (rounded down)
        int pts = (int) subTotal;
        loyaltyPoints.earn(pts, "Purchase: " + product.getName(), today);

        return order;
    }

    /**
     * +viewOrderHistory() : List<Order>
     */
    public List<Order> viewOrderHistory() { return orders; }

    // --- LOYALTY REWARDS ---------------------------------------------

    /**
     * +redeemReward(rewardNo:int) : boolean
     * Hard-coded reward catalogue matching the draft output.
     * rewardNo: 1 = Fitness Club T-Shirt (500 pts), 2 = Shaker Bottle (800 pts)
     */
   public boolean redeemReward(int rewardNo) {
    String today = LocalDate.now().format(DATE_FMT);

    switch (rewardNo) {
        case 1:
            return loyaltyPoints.spend(500, "Fitness Club T-Shirt", today);
        case 2:
            return loyaltyPoints.spend(800, "Shaker Bottle", today);
        case 3:
            return loyaltyPoints.spend(1500, "Fitness Club Towel", today);
        case 4:
            return loyaltyPoints.spend(3000, "Premium Gym Bag", today);
        default:
            System.out.println("[!] Invalid reward number.");
            return false;
    }
}

    /**
     * +viewPointsHistory() : String
     */
    public String viewPointsHistory() {
    StringBuilder sb = new StringBuilder();
    sb.append(String.format("%-5s %-12s %-10s %-30s%n",
            "NO", "DATE", "POINTS", "ACTION"));
    sb.append("-".repeat(60)).append("\n");

    String[] lines = loyaltyPoints.getHistory().split("\n");
    int i = 1;

    for (String line : lines) {
        if (line == null || line.trim().isEmpty()) continue;

        String[] p = line.split("\\|");
        if (p.length < 4) continue;

        String date   = p[0].trim();
        String type   = p[1].trim();
        String points = p[2].trim();
        String action = p[3].trim();

        // only show redeem records
        if (!type.equalsIgnoreCase("REDEEM")) continue;

        sb.append(String.format("%-5d %-12s %-10s %-30s%n",
                i++, date, points, action));
    }

    if (i == 1) {
        sb.append("No redemption history found.\n");
    }

    sb.append("\nCURRENT TOTAL: ")
      .append(loyaltyPoints.getPoints()).append(" PTS\n");

    return sb.toString();
}

    // --- MEMBERSHIP UPGRADE ------------------------------------------

    /**
     * +upgradeMembership(newTier:String, method:String) : boolean
     * method = "E_WALLET" | "CASH"
     */
    public boolean upgradeMembership(String newTier, String method) {
        if (!TIER_BASIC.equals(newTier)
                && !TIER_SILVER.equals(newTier)
                && !TIER_GOLD.equals(newTier)) {
            System.out.println("[!] Invalid tier: " + newTier);
            return false;
        }
        if (newTier.equals(tier)) {
            System.out.println("[!] Already on " + tier + " tier.");
            return false;
        }
        membership.upgradeTo(newTier);
        this.tier = newTier;

        // Award 500 loyalty points on membership renewal/upgrade
        String today = LocalDate.now().format(DATE_FMT);
        loyaltyPoints.earn(500, "Membership Renewal", today);
        return true;
    }

    // -- GETTERS ------------------------------------------------------
    public MyProfile getProfile() { 
    	return profile;
    	 }
    public String getTier(){ 
    	return tier; 
    		}
    public int getLoyaltyPoints(){ 
    	return loyaltyPoints.getPoints();
    	 }
    public LoyaltyPoints getLoyaltyObj(){
    	 return loyaltyPoints;
    	 }
    public Membership getMembership(){ 
    	return membership; 
    		}
    public HealthProfile getHealthProfile()    {
    	 return healthProfile;
    	  }
    public List<EquipmentBooking> getEquipmentBookings(){ 
    	return equipmentBookings;
    	 }
    public List<ClassBooking> getClassBookings()    { 
    	return classBookings;
    	 }
    public List<TrainerAppointment> getAppointments()  {
    	 return appointments; 
    	 	}
    public List<Order> getOrders(){ 
    	return orders;
    	 }

    // -- SETTERS (used by Admin and file-loading) ----------------------
    public void setTier(String tier) {
        this.tier = tier;
        if (membership != null) membership.setTier(tier);
    }
    public void setMembership(Membership m)        {
    	 this.membership    = m; 
    	 	}
    public void setHealthProfile(HealthProfile hp) { 
    	this.healthProfile = hp; 
    		}
    public void setLoyaltyPoints(LoyaltyPoints lp) { 
    	this.loyaltyPoints = lp;
    	 }
    public void setNextEquipBookingNo(int n)        { 
    	this.nextEquipBookingNo = n;
    	 }
    public void setNextClassBookingNo(int n)        { 
    	this.nextClassBookingNo = n;
    	 }
    public void setNextApptNo(int n)                { 
    	this.nextApptNo = n; 
    		}
   

    // -- PRIVATE HELPERS ----------------------------------------------
    private ClassSession findSession(int classNo, List<ClassSession> sessions) {
        for (ClassSession cs : sessions) {
            if (cs.getClassNo() == classNo) return cs;
        }
        return null;
    }

    /**
     * Adds 2 hours to a "HH:mm" time string.
     * Example: "15:00" -> "17:00"
     */
    private String addTwoHours(String startTime) {
        try {
            String[] parts = startTime.split(":");
            int hour = Integer.parseInt(parts[0]) + 2;
            return String.format("%02d:%02d", hour, Integer.parseInt(parts[1]));
        } catch (Exception e) {
            return startTime;
        }
    }

    private String bmiStatus(double bmi) {
        if (bmi < 18.5)       return "UNDERWEIGHT";
        else if (bmi < 25.0)  return "NORMAL WEIGHT";
        else if (bmi < 30.0)  return "OVERWEIGHT";
        else                  return "OBESE";
    }
     @Override
     public String getRole() {
    return "Member";
}
}