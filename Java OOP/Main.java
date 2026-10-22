import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.YearMonth;
import java.time.DayOfWeek;
import java.time.temporal.TemporalAdjusters;

public class Main {

    private static final DateTimeFormatter DATE_FMT =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Admin admin = new Admin();
        FitnessClubSystem system = new FitnessClubSystem();

        while (true) {
            printHome();
            String choice = sc.nextLine().trim();

            if      (choice.equals("1")) loginMenu(sc, admin, system);
            else if (choice.equals("2")) registerMemberFlow(sc, system);
            else if (choice.equals("3")) { System.out.println("GOODBYE!"); break; }
            else    System.out.println("[!] Invalid choice. Please enter 1, 2, or 3.");
        }
        sc.close();
    }

    // ================================================================
    // HOME / LOGIN TYPE MENU
    // ================================================================
	 private static void printLogo() {																													
		System.out.println(" 				_________							                                _________");
		System.out.println("	   	  	   |*********|	                                                       |*********|");
		System.out.println("	   		   |*********|	                                                       |*********|");
		System.out.println("	   		   |*********|	                                                       |*********|");
		System.out.println("			   |*********|	                                                       |*********|");
		System.out.println("		_______|*********|	                                                       |*********|");
		System.out.println("	   |*******|*********|	                                                       |*********|_______");
		System.out.println("	   |*******|*********|                                                         |*********|*******|");
		System.out.println("	   |*******|*********|	                                                       |*********|*******|");
 	    System.out.println(" ______|*******|*********|	                     ____________                      |*********|*******|______");
   		System.out.println("|******|*******|*********|______________________|   |    |   |_____________________|*********|*******|******|");
 	    System.out.println("|******|*******|*********|                      |   |    |   |                     |*********|*******|******|");
    	System.out.println("|******|*******|*********|                      |   |    |   |                     |*********|*******|******|");
    	System.out.println("|******|*******|*********|______________________|   |    |   |_____________________|*********|*******|******|");
  	    System.out.println("|______|*******|*********|                      |============|                     |*********|*******|______|");
    	System.out.println("	   |*******|*********|                     ||            ||                    |*********|*******|");
		System.out.println("	   |*******|*********|                 ====                ====                |*********|*******|");
		System.out.println("	   |*******|*********|	             ||                        ||              |*********|*******|");
   		System.out.println("	   |_______|*********|	           ==                            ==            |*********|_______|");
		System.out.println("	       	   |*********|           ||                                ||          |*********|");
		System.out.println("		       |*********|           ||                                ||          |*********|");
		System.out.println("	 		   |*********|	      ===          Life.Career.Money         ===       |*********|");
		System.out.println("		 	   |*********|      ||                ===========               ||     |*********|");
		System.out.println(" 			   |_________|	    ||               |   L.C.M   |              ||     |_________|");
    	System.out.println("                       		    ||               | gym club  |              ||");
    	System.out.println("                       		    ||                ===========               ||");
    	System.out.println("                        	      ===                                    ==="); 
    	System.out.println("                            		 ||                                ||");
    	System.out.println("                            		 ||                                ||");
    	System.out.println("                              	 	   ==                            ==");
    	System.out.println("                                 		 ||                        ||");
    	System.out.println("                                    	   ====                ====");
    	System.out.println("                                        	   ||            ||");
    	System.out.println("                                          		 ============");
    }


    private static void printHome() {
    	printLogo();
        System.out.println("--------------------------------------------------");
        System.out.println("Welcome To FITNESS CLUB SYSTEM");
        System.out.println("--------------------------------------------------");
        System.out.println("1. LOGIN");
        System.out.println("2. REGISTER NEW MEMBER");
        System.out.println("3. EXIT ");
        System.out.print("PLEASE ENTER YOUR CHOICE (1-3): ");
    }

    private static void loginMenu(Scanner sc, Admin admin, FitnessClubSystem system) {
        while (true) {
            System.out.println("===============");
            System.out.println("LOGIN");
            System.out.println("===============");
            System.out.println("1. MEMBER");
            System.out.println("2. TRAINER");
            System.out.println("3. ADMIN");
            System.out.println("4. BACK");
            System.out.print("PLEASE ENTER YOUR SELECTION (1-4): ");
            String sel = sc.nextLine().trim();

            if      (sel.equals("1")) memberLoginFlow(sc, system);
            else if (sel.equals("2")) trainerLoginFlow(sc, system);
            else if (sel.equals("3")) adminLoginFlow(sc, admin, system);
            else if (sel.equals("4")) return;
            else    System.out.println("[!] Invalid selection. Please enter 1-4.");
        }
    }

    // ================================================================
    // SECTION 1: MEMBER REGISTRATION
    // ================================================================

    private static void registerMemberFlow(Scanner sc, FitnessClubSystem system) {
    System.out.println("================================================================");
    System.out.println("SECTION 1 : REGISTER NEW MEMBER");
    System.out.println("================================================================");
    System.out.println("==============================");
    System.out.println("MEMBER REGISTRATION");
    System.out.println("==============================");
	
    String name;
    while (true) {
        name = readName(sc, "PLEASE ENTER YOUR NAME: ");
        if (system.memberNameExists(name)) {
            System.out.println("[!] This name is already registered. Please use another name.");
        } else {
            break;
        }
    }

    int age = readAge(sc, "PLEASE ENTER YOUR AGE: ");

String phone;
while (true) {
    phone = readPhone(sc, "PLEASE ENTER YOUR PHONE NUMBER: ");
    if (system.memberPhoneExists(phone)) {
        System.out.println("[!] This phone number is already registered.");
    } else {
        break;
    }
}

String gender   = readGender(sc, "PLEASE ENTER YOUR GENDER (M/F): ");
String password = readPassword(sc, "PLEASE ENTER YOUR PASSWORD: ");

Member m;
try {
    m = system.registerMember(name, age, phone, gender, password);
} catch (IllegalArgumentException e) {
    System.out.println("[!] " + e.getMessage());
    pressEnter(sc);
    return;
}

    System.out.println("YOU REQUESTED A ONE-TIME-PASSWORD (OTP) TO VERIFY YOUR ACCOUNT.");
    String otp = system.requestOTP(m.getAccountId());
    System.out.println("USE THE CODE BELOW TO COMPLETE YOUR VERIFICATION: " + otp);

    int tries = 3;
    boolean verified = false;
    while (tries > 0) {
        System.out.print("PLEASE ENTER YOUR OTP CODE: ");
        String entered = sc.nextLine().trim();
        if (entered.isEmpty()) {
            System.out.println("[!] OTP cannot be empty.");
            continue;
        }

        if (system.verifyOTP(m.getAccountId(), entered)) {
            verified = true;
            break;
        }

        tries--;
        if (tries > 0) {
            System.out.println("[!] Wrong OTP. Remaining tries: " + tries);
        } else {
            System.out.println("[!] Wrong OTP. Registration cancelled.");
        }
    }

    if (verified) {
        try {
            system.confirmRegistration(m.getAccountId());
            System.out.println("VERIFICATION SUCCESSFUL!");
            System.out.println("REGISTER SUCCESSFUL!");
            system.logAudit("REGISTER_MEMBER", m.getAccountId(),
                    m.getAccountId(), "New member registered: " + name);
        } catch (Exception e) {
            System.out.println("[!] Save error: " + e.getMessage());
        }
    }

    pressEnter(sc);
}

    // ================================================================
    // SECTION 2: MEMBER LOGIN & DASHBOARD
    // ================================================================
   private static void memberLoginFlow(Scanner sc, FitnessClubSystem system) {
    while (true) {
        System.out.println("================================================================");
        System.out.println("SECTION 2 : LOGIN - MEMBER");
        System.out.println("================================================================");

        System.out.print("PLEASE ENTER YOUR NAME: ");
        String nameOrId = sc.nextLine().trim();

        System.out.print("PLEASE ENTER YOUR PASSWORD: ");
        String password = sc.nextLine();

        Member m = system.loginMember(nameOrId, password);

        if (m == null) {
            System.out.println("[!] LOGIN FAILED. Wrong name or password.");

            String choice;
            while (true) {
                System.out.print("DO YOU WANT TO CONTINUE LOGIN? (Y/N): ");
                choice = sc.nextLine().trim();

                if (choice.equalsIgnoreCase("Y") || choice.equalsIgnoreCase("N")) {
                    break;
                }

                System.out.println("[!] Invalid input. Please enter Y or N only.");
            }

            if (choice.equalsIgnoreCase("Y")) {
                continue;
            } else {
                pressEnter(sc);
                return;
            }
        }

        System.out.println("LOGIN SUCCESSFUL!");
        system.logAudit("LOGIN_MEMBER", m.getAccountId(),
                m.getAccountId(), "Member login: " + m.getProfile().getName());

        if (m.getHealthProfile() == null) {
            firstTimeProfileSetup(sc, m, system);
        }

        memberDashboard(sc, m, system);
        return;
    }
}

	// ================================================================
    // setup your profile
    // ================================================================
    private static void firstTimeProfileSetup(Scanner sc, Member m, FitnessClubSystem system) {
    System.out.println("================================");
    System.out.println("SETUP YOUR PROFILE");
    System.out.println("================================");
    System.out.println("Hi, " + m.getProfile().getName() + "! Let's customize your plan.");

    double weight = readDoubleInRange(sc, "PLEASE ENTER YOUR WEIGHT (KG): ", 20, 600);
    double height = readDoubleInRange(sc, "PLEASE ENTER YOUR HEIGHT (CM): ", 80, 220);
    double bmi    = weight / ((height / 100) * (height / 100));
    System.out.printf("YOUR BMI RESULT: %.2f [Formula: Weight(kg) / Height(m) x Height(m)]%n", bmi);

    double target;
    while (true) {
        target = readWeightKg(sc, "PLEASE ENTER YOUR TARGET WEIGHT (KG): ");

        if (Math.abs(target - weight) > 300) {
            System.out.println("[!] Target weight difference is too extreme. Please enter a more realistic value.");
            continue;
        }
        break;
    }

    System.out.println("What is your main reason for joining?");
    System.out.println("1. Weight Loss");
    System.out.println("2. General Health & Fitness");
    System.out.println("3. Maintain Physique / Bodybuilding");
    int goalChoice = readInt(sc, "PLEASE SELECT (1-3): ", 1, 3);

    String goal;
    switch (goalChoice) {
        case 1:  goal = HealthProfile.GOAL_WEIGHT_LOSS; break;
        case 2:  goal = HealthProfile.GOAL_GENERAL;     break;
        default: goal = HealthProfile.GOAL_PHYSIQUE;    break;
    }

    System.out.println("Your entered " + goalChoice + ": " + goal);
    m.setupProfile(weight, height, target, goal);

    String joinMembership = readYesNo(sc, "DO YOU WANT TO JOIN MEMBERSHIP? (Y/N): ");
    if (joinMembership.equals("Y")) {
        membershipUpgradeFlow(sc, m, system);
    } else {
        System.out.println("CONTINUING AS BASIC MEMBER.");
    }

    system.rewriteMemberFile(m);
}

	//read double in range
	// check valid value
    private static double readDoubleInRange(Scanner sc, String prompt, double min, double max) {
    while (true) {
        System.out.print(prompt);
        String input = sc.nextLine().trim();

        if (!input.matches("^\\d+(\\.\\d{1,2})?$")) {
            System.out.println("[!] Invalid format. Please enter numbers only, with at most 2 decimal places.");
            continue;
        }

        try {
            double value = Double.parseDouble(input);

            if (value < min || value > max) {
                System.out.printf("[!] Value must be between %.1f and %.1f.%n", min, max);
                continue;
            }

            return value;
        } catch (NumberFormatException e) {
            System.out.println("[!] Invalid number. Please enter a valid numeric value.");
        }
    }
}

// choose yes / no
// allow uppercase and lowercase
private static String readYesNo(Scanner sc, String prompt) {
    while (true) {
        System.out.print(prompt);
        String input = sc.nextLine().trim();

        if (input.equalsIgnoreCase("Y") || input.equalsIgnoreCase("N")) {
            return input.toUpperCase();
        }

        System.out.println("[!] Invalid input. Please enter Y or N only.");
    }
}

	//member dashboard, member home page, show member function
   private static void memberDashboard(Scanner sc, Member m, FitnessClubSystem system) {
    while (true) {
        System.out.println("=========================================");
        System.out.println("MEMBER HOME PAGE");
        System.out.println("ROLE: " + m.getRole());
        System.out.println("=========================================");
        System.out.println("Hello, " + m.getProfile().getName());
        System.out.println("Member: " + m.getTier());
        System.out.println("=========================================");
        System.out.println("1.  GYM EQUIPMENT");

        boolean isSilverOrGold = !Member.TIER_BASIC.equals(m.getTier());
        boolean isGold         = Member.TIER_GOLD.equals(m.getTier());

        System.out.println("2.  VIEW & BOOK A CLASS"
                + (isSilverOrGold ? "" : " [LOCKED - SILVER/GOLD ONLY]"));
        System.out.println("3.  VIEW TRAINER LIST"
                + (isGold ? "" : " [LOCKED - GOLD ONLY]"));
        System.out.println("4.  TRACK BMI & PROGRESS");
        System.out.println("5.  MY PROFILE");
        System.out.println("6.  MEMBERSHIP");
        System.out.println("7.  TRAINER SCORE");
        System.out.println("8.  SHOP (FITNESS PRODUCTS)");
        System.out.println("9.  LOYALTY REWARDS");
        System.out.println("10. LOGOUT");
        System.out.print("PLEASE SELECT (1-10): ");

		//select member function
        String sel = sc.nextLine().trim();
        switch (sel) {
            case "1":  memberEquipmentMenu(sc, m, system);   break;
            case "2":  memberClassMenu(sc, m, system);       break;
            case "3":  memberTrainerMenu(sc, m, system);     break;
            case "4":  memberBMIMenu(sc, m, system);         break;
            case "5":  memberProfileMenu(sc, m, system);     break;
            case "6":  memberMembershipMenu(sc, m, system);  break;
            case "7":  memberRateTrainerMenu(sc, m, system); break;
            case "8":  memberShopMenu(sc, m, system);        break;
            case "9":  memberLoyaltyMenu(sc, m, system);     break;

            case "10": {
                String logoutChoice = readYesNo(sc, "DO YOU WANT TO LOGOUT OF FITNESS CLUB SYSTEM? (Y/N): ");
                if (logoutChoice.equals("Y")) {
                    System.out.println("LOGGING OUT...");
                    System.out.println("GOODBYE, " + m.getProfile().getName().toUpperCase() + "! SEE YOU NEXT TIME!");
                    system.logAudit("LOGOUT_MEMBER", m.getAccountId(),
                            m.getAccountId(), "Member logout");
                    pressEnter(sc);
                    return;
                } else {
                    System.out.println("LOGOUT CANCELLED.");
                }
                break;
            }

            default:
                System.out.println("[!] Invalid option. Please enter 1-10.");
        }
    }
}

    // == MEMBER FUNCTION 1: GYM EQUIPMENT ============================

    private static void memberEquipmentMenu(Scanner sc, Member m, FitnessClubSystem system) {
    String selectedDate = readFutureOrTodayDate(sc, "PLEASE ENTER BOOKING DATE (DD-MM-YYYY): ");

    while (true) {
        List<Equipment> equip = system.getAllEquipment();

        System.out.println("=====================================================================");
        System.out.println("GYM EQUIPMENT");
        System.out.println("=====================================================================");
        System.out.println("SELECTED DATE: " + selectedDate);
        System.out.printf("%-4s %-25s %-18s %-10s %-12s%n",
                "NO", "EQUIPMENT NAME", "ZONE", "CATEGORY", "STATUS");
        System.out.println("--------------------------------------------------------------------");

        if (equip.isEmpty()) {
            System.out.println("No equipment available.");
        } else {
            int no = 1;
            for (Equipment eq : equip) {
                System.out.printf("%-4d %-25s %-18s %-10s %-12s%n",
                        no++, eq.getName(), eq.getZone(),
                        eq.getCategory(), eq.getStatus());
            }
        }

        System.out.println("--------------------------------------------------------------------");
        System.out.println("1. BOOK EQUIPMENT");
        System.out.println("2. CHECK BOOKING");
        System.out.println("3. BACK");
        String sel = readMenuChoice(sc, "PLEASE SELECT (1-3): ", "1", "2", "3");

        if (sel.equals("1")) {
            if (equip.isEmpty()) {
                System.out.println("[!] No equipment available for booking.");
                pressEnter(sc);
                continue;
            }
            selectedDate = bookEquipmentFlow(sc, m, system, selectedDate);
        } else if (sel.equals("2")) {
            checkEquipmentBookingFlow(sc, m, system);
        } else {
            return;
        }
    }
}

	//book equipment flow, validation
  private static String bookEquipmentFlow(Scanner sc, Member m, FitnessClubSystem system, String firstBookDate) {
    List<Equipment> equip = system.getAllEquipment();

    if (equip == null || equip.isEmpty()) {
        System.out.println("[!] No equipment available for booking.");
        pressEnter(sc);
        return firstBookDate;
    }

    int equipNo;
    Equipment chosen = null;

    while (true) {
        equipNo = readInt(sc, "ENTER EQUIPMENT NO (1-" + equip.size() + "): ", 1, equip.size());
        chosen = equip.get(equipNo - 1);

        if (chosen.isMaintenance()) {
            System.out.println("[ERROR]: This equipment is currently UNDER MAINTENANCE. Please select another.");
            continue;
        }
        if (!chosen.isAvailable()) {
            System.out.println("[ERROR]: This equipment is currently IN USE. Please select another.");
            continue;
        }
        break;
    }

    System.out.println("SELECTED: " + chosen.getName() + " (" + chosen.getEquipmentId() + ") - " + chosen.getZone());

    String useSameDate = readYesNo(sc, "DO YOU WANT TO BOOK ON " + firstBookDate + "? (Y/N): ");
    String bookDate = firstBookDate;

   if (useSameDate.equals("N")) {
    bookDate = readFutureOrTodayDate(sc, "PLEASE ENTER NEW BOOKING DATE (DD-MM-YYYY): ");
}

String startTime = readStartTime(sc, "PLEASE ENTER START TIME (9:00AM - 20:00PM): ");

System.out.println("CALCULATING DURATION (2 HOURS FIXED)");
String continueChoice = readYesNo(sc, "PLEASE ENTER Y TO CONTINUE (Y/N): ");
if (continueChoice.equals("N")) {
    return bookDate;
}

String endTime = addTwoHours(startTime);

    if (isEquipmentAlreadyBooked(system, chosen.getEquipmentId(), bookDate, startTime, endTime)) {
    System.out.println("[!] This equipment is already booked by another member for the selected date and time slot.");
    pressEnter(sc);
    return bookDate;
    }

    if (hasEquipmentTimeConflict(m, bookDate, startTime, endTime)) {
    System.out.println("[!] You already have a booking on the same date and time slot.");
    pressEnter(sc);
    return bookDate;
    }
    
    //booking confirmation
    System.out.println("=========================================");
    System.out.println("BOOKING CONFIRMATION");
    System.out.println("=========================================");
    System.out.println("EQUIPMENT: " + chosen.getName() + " (" + chosen.getEquipmentId() + ")");
    System.out.println("DATE      : " + bookDate);
    System.out.println("TIME SLOT : " + startTime + " - " + endTime);
    System.out.println("DURATION  : 2 HOURS (System Auto-Set)");
    System.out.println("USER      : " + m.getProfile().getName());
    System.out.println("-----------------------------------------");
    String confirm = readYesNo(sc, "CONFIRM THIS BOOKING? (Y/N): ");
    if (confirm.equals("N")) return bookDate;

    EquipmentBooking eb = m.bookEquipment(equipNo, bookDate, startTime, equip);
    if (eb != null) {
        System.out.println("BOOKING CONFIRMED!");
        system.rewriteMemberFile(m);
        system.logAudit("BOOK_EQUIPMENT", m.getAccountId(),
                chosen.getEquipmentId(), "Booked " + chosen.getName() + " on " + bookDate);
    } else {
        System.out.println("[!] Booking failed.");
    }

    pressEnter(sc);
    return bookDate;
}

	
	//check equipment booking flow
    private static void checkEquipmentBookingFlow(Scanner sc, Member m, FitnessClubSystem system) {
    while (true) {
        refreshEquipmentBookingStatuses(m);
        system.rewriteMemberFile(m);

        List<EquipmentBooking> bookings = m.getEquipmentBookings();

        System.out.println("=====================================================================");
        System.out.println("MY EQUIPMENT BOOKINGS");
        System.out.println("=====================================================================");
        System.out.printf("%-4s %-25s %-12s %-22s %-12s%n",
                "NO", "EQUIPMENT NAME", "DATE", "TIME SLOT", "STATUS");
        System.out.println("--------------------------------------------------------------------");

        boolean any = false;
        for (EquipmentBooking eb : bookings) {
            if (!EquipmentBooking.CANCELLED.equalsIgnoreCase(eb.getStatus())) {
                System.out.printf("%-4d %-25s %-12s %-22s %-12s%n",
                        eb.getBookingNo(),
                        eb.getEquipmentName(),
                        eb.getDate(),
                        eb.getStartTime() + " - " + eb.getEndTime(),
                        eb.getStatus());
                any = true;
            }
        }

        if (!any) {
            System.out.println("No booking records found.");
        }

        System.out.println("--------------------------------------------------------------------");
        System.out.println("1. CANCEL AN UPCOMING BOOKING");
        System.out.println("2. BACK TO MENU");
        String sel = readMenuChoice(sc, "PLEASE SELECT (1-2): ", "1", "2");

        if (sel.equals("2")) {
            return;
        }

        if (!any) {
            System.out.println("[!] There is no booking to cancel.");
            pressEnter(sc);
            continue;
        }

        int bno;
        EquipmentBooking target = null;

        while (true) {
            bno = readInt(sc, "ENTER BOOKING NO TO CANCEL: ", 1, 9999);

            target = null;
            for (EquipmentBooking eb : bookings) {
                if (eb.getBookingNo() == bno &&
                    !EquipmentBooking.CANCELLED.equalsIgnoreCase(eb.getStatus())) {
                    target = eb;
                    break;
                }
            }

            if (target == null) {
                System.out.println("[!] Invalid booking number. Please enter an existing booking number.");
                continue;
            }

            if (!isUpcomingEquipmentBooking(target)) {
                System.out.println("[!] Only UPCOMING bookings can be cancelled.");
                continue;
            }

            break;
        }

        String confirm = readYesNo(sc, "ARE YOU SURE? (THIS CANNOT BE UNDONE) (Y/N): ");
        if (confirm.equals("Y")) {
            if (m.cancelEquipmentBooking(bno)) {
                System.out.println("BOOKING CANCELLED SUCCESSFULLY.");
                system.rewriteMemberFile(m);
            } else {
                System.out.println("[!] Booking not found or already cancelled.");
            }
        }

        pressEnter(sc);
    }
}
    // == MEMBER FUNCTION 2: CLASSES ===================================
    private static void memberClassMenu(Scanner sc, Member m, FitnessClubSystem system) {
    int year  = readBookingYear(sc);
    int month = readBookingMonth(sc);
    int week  = readWeekOfMonth(sc);

    LocalDate[] range = getWeekRange(year, month, week);
    LocalDate weekStart = range[0];
    LocalDate weekEnd   = range[1];

    List<ClassSession> sessions = system.getAllClassSessions();

    while (true) {
        System.out.println("=====================================================================");
        System.out.println("SELECTED PERIOD");
        System.out.println("=====================================================================");
        System.out.println("YEAR  : " + year);
        System.out.println("MONTH : " + month);
        System.out.println("WEEK  : " + week);
        System.out.println("DATE  : " + formatDateRange(weekStart, weekEnd));

        if (isPastWeek(weekEnd)) {
            System.out.println("[!] WARNING: The selected week is already in the past.");
        }

        System.out.println("=====================================================================");
        printClassScheduleWithDates(sessions, weekStart);

        if (Member.TIER_BASIC.equals(m.getTier())) {
            System.out.println("[!] BASIC MEMBERS CAN VIEW SCHEDULE ONLY.");
        }

        System.out.println("1. BOOK A CLASS");
        System.out.println("2. MY BOOKED CLASSES");
        System.out.println("3. VIEW TRAINER DETAILS");
        System.out.println("4. CHANGE YEAR / MONTH / WEEK");
        System.out.println("5. BACK TO HOME PAGE");
        String sel = readMenuChoice(sc, "PLEASE SELECT (1-5): ", "1", "2", "3", "4", "5");

        if (sel.equals("1")) {
            if (isPastWeek(weekEnd)) {
                System.out.println("[!] You cannot book a class for a past week.");
                pressEnter(sc);
                continue;
            }

            if (Member.TIER_BASIC.equals(m.getTier())) {
                System.out.println("[!] THIS FEATURE IS LOCKED FOR BASIC MEMBERS.");
                String upgradeChoice = readYesNo(sc,
                        "WOULD YOU LIKE TO UPGRADE TO UNLOCK THIS FEATURE? (Y/N): ");
                if (upgradeChoice.equals("Y")) {
                    membershipUpgradeFlow(sc, m, system);
                    sessions = system.getAllClassSessions();
                }
            } else {
                bookClassFlow(sc, m, system, sessions, year, month, week);
            }

        } else if (sel.equals("2")) {
            myBookedClassesFlow(sc, m, system, sessions);
        } else if (sel.equals("3")) {
            viewTrainerDetailsFlow(sc, system);
        } else if (sel.equals("4")) {
            year  = readBookingYear(sc);
            month = readBookingMonth(sc);
            week  = readWeekOfMonth(sc);

            range = getWeekRange(year, month, week);
            weekStart = range[0];
            weekEnd   = range[1];
        } else {
            return;
        }
    }
}     
     //format date range
     private static String formatDateRange(LocalDate start, LocalDate end) {
     return start + " - " + end;
     }
     
     private static boolean isPastWeek(LocalDate weekEnd) {
     return weekEnd.isBefore(LocalDate.now());
     }
     
     //weekly class schedule, view
   private static void printClassScheduleWithDates(List<ClassSession> sessions, LocalDate weekStart) {
    System.out.println("=====================================================================");
    System.out.println("WEEKLY CLASS SCHEDULE");
    System.out.println("=====================================================================");
    System.out.printf("%-4s %-12s %-5s %-10s %-10s %-15s %-18s %-6s%n",
            "NO", "DATE", "DAY", "TIME", "CLASS", "TRAINER", "LOCATION", "SLOTS");
    System.out.println("--------------------------------------------------------------------");

    for (ClassSession cs : sessions) {
        LocalDate classDate = getClassDateFromDay(cs.getDay(), weekStart);

        System.out.printf("%-4d %-12s %-5s %-10s %-10s %-15s %-18s %d/%d%n",
                cs.getClassNo(),
                classDate,
                cs.getDay(),
                cs.getTime(),
                cs.getClassName(),
                cs.getTrainerName(),
                cs.getLocation(),
                cs.getSlotUsed(),
                cs.getSlotLimit());
    }

    System.out.println("--------------------------------------------------------------------");
}

	// book class flow
    private static void bookClassFlow(Scanner sc, Member m,FitnessClubSystem system,List<ClassSession> sessions,int year, int month, int week) {
    if (sessions == null || sessions.isEmpty()) {
        System.out.println("[!] No class session available.");
        pressEnter(sc);
        return;
    }

    int classNo = readInt(sc, "ENTER CLASS NO TO BOOK: ", 1, sessions.size());

    ClassSession cs = sessions.stream()
            .filter(s -> s.getClassNo() == classNo)
            .findFirst()
            .orElse(null);

    if (cs == null) {
        System.out.println("[!] Class not found.");
        pressEnter(sc);
        return;
    }

    if (!cs.hasAvailableSlot()) {
        System.out.println("[!] Class is full.");
        pressEnter(sc);
        return;
    }

    if (hasClassConflict(m, cs)) {
        System.out.println("[!] You already booked another class at the same day and time.");
        pressEnter(sc);
        return;
    }
	//member booking details
    System.out.println("YOU SELECTED: " + cs.getClassName());
    System.out.println("=========================================");
    System.out.println("CLASS BOOKING DETAILS");
    System.out.println("=========================================");
    System.out.println("YEAR     : " + year);
    System.out.println("MONTH    : " + month);
    System.out.println("WEEK     : " + week);
    System.out.println("CLASS    : " + cs.getClassName());
    System.out.println("TRAINER  : " + cs.getTrainerName());
    System.out.println("TIME     : " + cs.getDay() + ", " + cs.getTime());
    System.out.println("LOCATION : " + cs.getLocation());
    System.out.println("MEMBER   : " + m.getProfile().getName() + " (" + m.getTier() + ")");
    System.out.println("-----------------------------------------");

    String confirm = readYesNo(sc, "CONFIRM BOOKING? (Y/N): ");
    if (confirm.equals("N")) {
        System.out.println("CLASS BOOKING CANCELLED.");
        pressEnter(sc);
        return;
    }

    ClassBooking cb = m.bookClass(classNo, sessions);
    if (cb != null) {
        System.out.println("BOOKING SUCCESSFUL!");
        system.saveClassSessions(sessions);
        system.rewriteMemberFile(m);
        system.logAudit("BOOK_CLASS", m.getAccountId(),
                "CLASS-" + classNo, "Booked " + cs.getClassName()
                        + " [Year=" + year + ", Month=" + month + ", Week=" + week + "]");
    } else {
        System.out.println("[!] Booking failed.");
    }

    pressEnter(sc);
}

	//book class flow 
    private static void myBookedClassesFlow(Scanner sc, Member m, FitnessClubSystem system,List<ClassSession> sessions) {
        List<ClassBooking> bookings = m.getClassBookings();
        System.out.println("=====================================================================");
        System.out.println("MY CLASS BOOKINGS");
        System.out.println("=====================================================================");
        System.out.println(m.getProfile().getName() + " (" + m.getTier() + ")");
        System.out.println("=====================================================================");
        System.out.printf("%-4s %-10s %-5s %-10s %-15s %-18s%n", "NO", "CLASS", "DAY", "TIME", "TRAINER", "LOCATION");
        System.out.println("--------------------------------------------------------------------");
        boolean any = false;
        for (ClassBooking cb : bookings) {
            if (!ClassBooking.CANCELLED.equals(cb.getStatus())) {
                System.out.printf("%-4d %-10s %-5s %-10s %-15s %-18s%n",
                        cb.getBookingNo(), cb.getClassName(), cb.getDay(),
                        cb.getTime(), cb.getTrainerName(), cb.getLocation());
                any = true;
            }
        }
        if (!any) System.out.println("No active class bookings.");
        System.out.println("--------------------------------------------------------------------");
        System.out.println("1. CANCEL CLASS");
        System.out.println("2. BACK TO MENU");
        String sel = readMenuChoice(sc, "PLEASE SELECT (1-2): ", "1", "2");

        if (sel.equals("2")) {
         pressEnter(sc);
         return;
         }

      if (!any) {
    System.out.println("[!] There is no active class booking to cancel.");
    pressEnter(sc);
    return;
    }

int bno;
while (true) {
    bno = readInt(sc, "ENTER CLASS BOOKING NO TO CANCEL: ", 1, 9999);

    if (!hasActiveClassBookingNo(bookings, bno)) {
        System.out.println("[!] Invalid class booking number. Please enter an existing active booking number.");
        continue;
    }
    break;
}

String confirm = readYesNo(sc, "ARE YOU SURE? (THIS CANNOT BE UNDONE) (Y/N): ");
if (confirm.equals("Y")) {
    if (m.cancelClassBooking(bno, sessions)) {
        System.out.println("CLASS BOOKING CANCELLED SUCCESSFULLY.");
        System.out.println("The slot has been released.");
        system.saveClassSessions(sessions);
        system.rewriteMemberFile(m);
    } else {
        System.out.println("[!] Booking not found.");
    }
}

pressEnter(sc);
    }
    // == MEMBER FUNCTION 3: TRAINER ===================================

    private static void memberTrainerMenu(Scanner sc, Member m, FitnessClubSystem system) {
    if (!Member.TIER_GOLD.equals(m.getTier())) {
        System.out.println("[!] THIS FEATURE IS LOCKED. UPGRADE TO GOLD TO ACCESS.");
        String upgradeChoice = readYesNo(sc, "WOULD YOU LIKE TO UPGRADE? (Y/N): ");
        if (upgradeChoice.equals("Y")) {
            membershipUpgradeFlow(sc, m, system);
        }
        if (!Member.TIER_GOLD.equals(m.getTier())) return;
    }

    while (true) { 
        System.out.println("=====================================================================");
        System.out.println("TRAINER MANAGEMENT");
        System.out.println("=====================================================================");
        System.out.println("1. BOOK A TRAINER");
        System.out.println("2. MY APPOINTMENTS");
        System.out.println("3. VIEW TRAINER DETAILS");
        System.out.println("4. BACK TO HOME PAGE");

        String sel = readMenuChoice(sc, "PLEASE SELECT (1-4): ", "1", "2", "3", "4");

        if (sel.equals("1")) {
            bookTrainerFlow(sc, m, system);
        } else if (sel.equals("2")) {
            myAppointmentsFlow(sc, m, system);
        } else if (sel.equals("3")) {
            viewTrainerDetailsFlow(sc, system);
        } else {
            return;
        }
    }
}

	// book trainer flow, validation
    private static void bookTrainerFlow(Scanner sc, Member m, FitnessClubSystem system) {
    List<Trainer> trainers = system.getAllTrainers();

    if (trainers == null || trainers.isEmpty()) {
        System.out.println("[!] No trainer available for booking.");
        pressEnter(sc);
        return;
    }

    String firstBookDate = readTrainerBookingDate(sc, "PLEASE ENTER YOUR DATE (DD-MM-YYYY): ");
    printTrainerList(trainers, true);

    int trainerNo;
    Trainer chosen = null;

    while (true) {
        trainerNo = readInt(sc, "ENTER TRAINER NO (1-" + trainers.size() + "): ", 1, trainers.size());
        chosen = trainers.get(trainerNo - 1);

        if (!"ACTIVE".equalsIgnoreCase(chosen.getStatus())) {
            System.out.println("[!] This trainer is currently not available. Please select another trainer.");
            continue;
        }
        break;
    }

    System.out.println("YOU SELECTED: " + chosen.getProfile().getName());

    String useSameDate = readYesNo(sc, "DO YOU WANT TO BOOK ON " + firstBookDate + "? (Y/N): ");
    String bookDate = firstBookDate;

    if (useSameDate.equals("N")) {
    String continueBooking = readYesNo(sc, "DID YOU WANT TO CONTINUE BOOKING TRAINER? (Y/N): ");
    if (continueBooking.equals("N")) {
        System.out.println("TRAINER BOOKING CANCELLED.");
        pressEnter(sc);
        return;
      }

    bookDate = readTrainerBookingDate(sc, "PLEASE ENTER BOOKING DATE (DD-MM-YYYY): ");
    }

    String startTime = readStartTime(sc, "PLEASE ENTER START TIME (9:00AM - 20:00PM): ");

    System.out.println("CALCULATING DURATION (2 HOURS FIXED)");
    String continueChoice = readYesNo(sc, "PLEASE ENTER Y TO CONTINUE (Y/N): ");
    if (continueChoice.equals("N")) {
        System.out.println("TRAINER BOOKING CANCELLED.");
        pressEnter(sc);
        return;
    }

    String endTime = addTwoHours(startTime);

    if (hasTrainerTimeConflict(m, bookDate, startTime, endTime)) {
    System.out.println("[!] TRAINER BOOKING WAS NOT CREATED.");
    System.out.println("[!] Reason: You already have another trainer appointment on the same date and time slot.");
    System.out.println("[!] Please choose another time.");
    pressEnter(sc);
    return;
}

  if (hasEquipmentTimeConflict(m, bookDate, startTime, endTime)) {
    System.out.println("[!] TRAINER BOOKING WAS NOT CREATED.");
    System.out.println("[!] Reason: You already have an equipment booking on the same date and time slot.");
    System.out.println("[!] Please choose another time or cancel your equipment booking first.");
    pressEnter(sc);
    return;
}

	//trainer booking details
    System.out.println("=========================================");
    System.out.println("TRAINER BOOKING DETAILS");
    System.out.println("=========================================");
    System.out.println("TRAINER  : " + chosen.getProfile().getName());
    System.out.println("FOCUS    : " + chosen.getSpecialty());
    System.out.println("DATE     : " + bookDate);
    System.out.println("TIME     : " + startTime + " - " + endTime);
    System.out.println("MEMBER   : " + m.getProfile().getName() + " (" + m.getTier() + ")");
    System.out.println("-----------------------------------------");

    String confirm = readYesNo(sc, "CONFIRM BOOKING? (Y/N): ");
    if (confirm.equals("N")) {
        System.out.println("TRAINER BOOKING CANCELLED.");
        pressEnter(sc);
        return;
    }

    TrainerAppointment ta = m.bookTrainer(trainerNo, bookDate, startTime, trainers);

    if (ta == null) {
    System.out.println("[!] TRAINER BOOKING FAILED.");
    System.out.println("[!] The appointment could not be created.");
    pressEnter(sc);
    return;
    }

    System.out.println("BOOKING CONFIRMED!");
    System.out.println("TRAINER APPOINTMENT CREATED SUCCESSFULLY.");
    system.rewriteMemberFile(m);
    system.logAudit("BOOK_TRAINER", m.getAccountId(),
        chosen.getAccountId(), "Booked trainer: " + chosen.getProfile().getName());

    pressEnter(sc);
    }
    
    //my appointment flow
    private static void myAppointmentsFlow(Scanner sc, Member m, FitnessClubSystem system) {
    List<TrainerAppointment> appts = m.getAppointments();

    System.out.println("=====================================================================");
    System.out.println("MY APPOINTMENTS");
    System.out.println("=====================================================================");
    System.out.println(m.getProfile().getName() + " (" + m.getTier() + ")");
    System.out.printf("%-4s %-12s %-10s %-15s %-15s %-12s%n",
            "NO", "DATE", "TIME", "TRAINER", "FOCUS", "STATUS");
    System.out.println("--------------------------------------------------------------------");

    boolean any = false;
    for (TrainerAppointment ta : appts) {
        if (!TrainerAppointment.CANCELLED.equalsIgnoreCase(ta.getStatus())) {
            System.out.printf("%-4d %-12s %-10s %-15s %-15s %-12s%n",
                    ta.getAppointmentNo(), ta.getDate(), ta.getStartTime(),
                    ta.getTrainerName(), ta.getSpecialty(), ta.getStatus());
            any = true;
        }
    }

    if (!any) {
        System.out.println("No active appointments.");
    }

    System.out.println("--------------------------------------------------------------------");
    System.out.println("1. CANCEL SESSION");
    System.out.println("2. BACK");

    String sel = readMenuChoice(sc, "PLEASE SELECT (1-2): ", "1", "2");

    if (sel.equals("2")) {
        pressEnter(sc);
        return;
    }

    if (!any) {
        System.out.println("[!] There is no active appointment to cancel.");
        pressEnter(sc);
        return;
    }

    int ano;
    TrainerAppointment target = null;

    while (true) {
        ano = readInt(sc, "ENTER APPOINTMENT NO TO CANCEL: ", 1, 9999);

        target = null;
        for (TrainerAppointment ta : appts) {
            if (ta.getAppointmentNo() == ano &&
                !TrainerAppointment.CANCELLED.equalsIgnoreCase(ta.getStatus())) {
                target = ta;
                break;
            }
        }

        if (target == null) {
            System.out.println("[!] Invalid appointment number. Please enter an existing active appointment number.");
            continue;
        }

        break;
    }

    String confirm = readYesNo(sc, "ARE YOU SURE? (THIS CANNOT BE UNDONE) (Y/N): ");
    if (confirm.equals("Y")) {
        if (m.cancelAppointment(ano)) {
            System.out.println("SESSION CANCELLED SUCCESSFULLY.");
            system.rewriteMemberFile(m);
        } else {
            System.out.println("[!] Appointment not found.");
        }
    }

    pressEnter(sc);
}
	
	//view trainer details flow
    private static void viewTrainerDetailsFlow(Scanner sc, FitnessClubSystem system) {
        List<Trainer> trainers = system.getAllTrainers();
        printTrainerList(trainers, false);
        int no = readInt(sc, "ENTER TRAINER NO TO VIEW DETAILS (1-" + trainers.size() + "): ",
                1, trainers.size());
        Trainer t = trainers.get(no - 1);
        System.out.println("=====================================================================");
        System.out.println("TRAINER PROFILE: " + t.getProfile().getName().toUpperCase());
        System.out.println("=====================================================================");
        System.out.println("NAME       : " + t.getProfile().getName());
        System.out.println("SPECIALTY  : " + t.getSpecialty());
        System.out.println("EXPERIENCE : " + t.getExperienceYears() + " Years");
        System.out.printf ("RATING     : %.1f / 5.0%n", t.getRatingAvg());
        System.out.println("STATUS     : " + t.getStatus());
        System.out.println("MOTTO      : \"" + t.getMotto() + "\"");
        System.out.println("---------------------------------------------------------------------");
        pressEnter(sc);
    }

	//print trainer list
   private static void printTrainerList(List<Trainer> trainers, boolean showStatus) {
    System.out.println("=====================================================================");
    System.out.println("MEET OUR EXPERT TRAINERS");
    System.out.println("=====================================================================");
    System.out.printf("%-4s %-20s %-5s %-24s %-6s %-20s%s%n",
            "NO", "NAME", "EXP", "SPECIALTY", "RATING", "QUOTE",
            showStatus ? " STATUS" : "");

    System.out.println("-----------------------------------------------------------------------");

    int i = 1;

    for (Trainer t : trainers) {
        String motto = t.getMotto();
        if (motto.length() > 18) {
            motto = motto.substring(0, 18) + "...";
        }

        System.out.printf("%-4d %-20s %-5s %-24s %-6.1f \"%-20s\"%s%n",
                i++, t.getProfile().getName() + " (" + t.getSpecialty().split("/")[0].trim() + ")",
                t.getExperienceYears() + " Yrs",
                t.getSpecialty(),
                t.getRatingAvg(),
                motto,
                showStatus ? " " + t.getStatus() : "");
    }
}

    // == MEMBER FUNCTION 4: BMI TRACKER ==============================

    private static void memberBMIMenu(Scanner sc, Member m, FitnessClubSystem system) {
        while (true) {
            HealthProfile hp = m.getHealthProfile();
            System.out.println("=====================================================================");
            System.out.println("HEALTH & PROGRESS TRACKER");
            System.out.println("=====================================================================");
            System.out.println("[USER PROFILE]");
            System.out.println("NAME          : " + m.getProfile().getName());
            if (hp != null) {
                System.out.println("HEIGHT        : " + hp.getHeightCm() + " CM");
                System.out.println("WEIGHT        : " + hp.getCurrentWeightKg() + " KG (Current)");
                System.out.println("Target Weight : " + hp.getTargetWeightKg() + " KG");
                System.out.printf("BMI           : %.2f%n", hp.getBMI());
                System.out.println("STATUS        : " + hp.getBMIStatus());
            } else {
                System.out.println("No health profile set up.");
            }
            System.out.println("=====================================================================");
            System.out.println("1. UPDATE AND VIEW BMI");
            System.out.println("2. BMI DETAILS");
            System.out.println("3. BACK");
            System.out.print("PLEASE ENTER (1-3): ");
            String sel = sc.nextLine().trim();

            if (sel.equals("1")) {
                if (hp == null) {
                    System.out.println("[!] Please set up your profile first.");
                    pressEnter(sc);
                    continue;
                }
                //weight progress log
                System.out.println("=====================================================================");
                System.out.println("WEIGHT PROGRESS LOG");
                System.out.println("=====================================================================");
                printHealthProgressTable(m);
                String updateChoice = readYesNo(sc,"DO YOU WANT TO UPDATE YOUR INFORMATION? (Y/N): ");
                    if (updateChoice.equals("Y")) {
                    System.out.println("1. HEIGHT");
                    System.out.println("2. WEIGHT");
                    System.out.println("3. TARGET WEIGHT");
                    int choice = readInt(sc, "PLEASE ENTER (1-3): ", 1, 3);
                    if (choice == 1) {
                        double val = readHeightCm(sc, "ENTER NEW HEIGHT (CM): ");
                        double prev = hp.getHeightCm();
                        m.updateBMI("HEIGHT", val);
                        System.out.printf("UPDATE SUCCESSFUL! New BMI: %.2f (%s)%n",
                                hp.getBMI(), hp.getBMIStatus());
                    } else if (choice == 2) {
                        double prev = hp.getCurrentWeightKg();
                        double val = readWeightKg(sc, "ENTER NEW WEIGHT (KG): ");
                        m.updateBMI("WEIGHT", val);
                        System.out.println("UPDATE SUCCESSFUL!");
                        System.out.println("TARGET WEIGHT : " + hp.getTargetWeightKg() + "KG");
                        System.out.println("PREVIOUS      : " + prev + " KG");
                        System.out.println("CURRENT       : " + hp.getCurrentWeightKg() + " KG");
                        System.out.printf("CHANGE        : %.1f KG%n",
                                hp.getCurrentWeightKg() - prev);
                    } else {
                        double val = readWeightKg(sc, "ENTER NEW TARGET WEIGHT (KG): ");
                        m.updateBMI("TARGET", val);
                        System.out.println("UPDATE SUCCESSFUL!");
                    }
                    System.out.println("-".repeat(60));
                    printHealthProgressTable(m);
                    system.rewriteMemberFile(m);
                }
                pressEnter(sc);
            } else if (sel.equals("2")) {
                printBMIGuide(hp);
                pressEnter(sc);
            } else if (sel.equals("3")) {
                return;
            } else {
                System.out.println("[!] Invalid selection.");
            }
        }
    }

	// BMI guide
    private static void printBMIGuide(HealthProfile hp) {
        System.out.println("=====================================================================");
        System.out.println("BMI REFERENCE GUIDE");
        System.out.println("=====================================================================");
        System.out.println("Understanding your Body Mass Index (BMI) and potential health risks.");
        System.out.println("--------------------------------------------------------------------");
        System.out.printf("%-15s %-18s %-30s%n", "BMI RANGE", "CATEGORY", "HEALTH RISKS");
        System.out.println("--------------------------------------------------------------------");
        System.out.printf("%-15s %-18s %-30s%n", "Below 18.5", "UNDERWEIGHT",
                "Malnutrition, Weak Bones, Low Immunity");
        System.out.printf("%-15s %-18s %-30s%n", "18.5 - 24.9", "NORMAL WEIGHT",
                "Low Risk (Healthy Range) [OK]");
        System.out.printf("%-15s %-18s %-30s%n", "25.0 - 29.9", "OVERWEIGHT",
                "Fatigue, Joint Pain, Cardiovascular Risk");
        System.out.printf("%-15s %-18s %-30s%n", "30.0 & Above", "OBESE",
                "Type 2 Diabetes, High BP, Heart Risk [!]");
        System.out.println("--------------------------------------------------------------------");
        if (hp != null) {
            System.out.printf("Your current BMI is %.2f (%s).%n",
                    hp.getBMI(), hp.getBMIStatus());
        }
    }

    // == MEMBER FUNCTION 5: MY PROFILE ===============================

    private static void memberProfileMenu(Scanner sc, Member m,
                                           FitnessClubSystem system) {
        while (true) {
            System.out.println("==============================");
            System.out.println("MY PROFILE");
            System.out.println("==============================");
            System.out.println("1. NAME          : " + m.getProfile().getName());
            System.out.println("2. AGE           : " + m.getProfile().getAge());
            System.out.println("3. PHONE NUMBER  : " + m.getProfile().getPhone());
            System.out.println("4. GENDER        : " + m.getProfile().getGender());
            System.out.println("==============================");
            System.out.println("1. EDIT PHONE NUMBER");
            System.out.println("2. DELETE ACCOUNT");
            System.out.println("3. BACK TO HOME PAGE");
            System.out.print("Please select (1-3): ");
            String sel = sc.nextLine().trim();

            if (sel.equals("1")) {
                String newPhone;
    while (true) {
    newPhone = readPhone(sc, "NEW PHONE NUMBER: ");

    if (newPhone.equalsIgnoreCase(m.getProfile().getPhone())) {
        break; // same as current, allow
    }

    if (system.memberPhoneExists(newPhone)) {
        System.out.println("[!] This phone number is already registered.");
        continue;
    }

    break;
   }

   String save = readYesNo(sc, "SAVE? (Y/N): ");
if (save.equals("Y")) {
    m.getProfile().setPhone(newPhone);
    system.rewriteMemberFile(m);
    System.out.println("UPDATE SUCCESSFUL!");
} else {
    System.out.println("UPDATE CANCELLED.");
}
         
            pressEnter(sc);
            //print warning msg
            } else if (sel.equals("2")) {
                System.out.println("===============================");
                System.out.println("WARNING: ACCOUNT DELETION");
                System.out.println("================================");
                String confirmDelete = readYesNo(sc, "ARE YOU SURE YOU WANT TO PROCEED? (Y/N): ");
               if (confirmDelete.equals("Y")) {
                         m.deleteAccount();
                         system.rewriteMemberFile(m);
                         System.out.println("ACCOUNT HAS BEEN DISABLED SUCCESSFULLY.");
                         pressEnter(sc);
                         return;
}
            } else if (sel.equals("3")) {
                return;
            } else {
                System.out.println("[!] Invalid selection.");
            }
        }
    }

    // == MEMBER FUNCTION 6: MEMBERSHIP ===============================

    private static void memberMembershipMenu(Scanner sc, Member m,FitnessClubSystem system) {
    System.out.println("YOUR CURRENT STATUS: " + m.getTier() + " MEMBER.");

    String upgradeChoice = readYesNo(sc, "DO YOU WANT TO UPGRADE YOUR MEMBERSHIP? (Y/N): ");
    if (upgradeChoice.equals("Y")) {
        membershipUpgradeFlow(sc, m, system);
    } else {
        System.out.println("No changes made. Continuing as current member...");
        pressEnter(sc);
    }
}

	//membership upgrade flow
    private static void membershipUpgradeFlow(Scanner sc, Member m,FitnessClubSystem system) {
        printMembershipTable(m.getTier());
        System.out.println("CHOOSE YOUR UPGRADE:");
        System.out.println("1. UPGRADE TO SILVER (RM 150)");
        System.out.println("2. UPGRADE TO GOLD (RM 250)");
        System.out.println("3. STAY AS CURRENT MEMBER");
        String sel = readMenuChoice(sc, "PLEASE SELECT (1-3): ", "1", "2", "3");

        String newTier = null;
        double fee = 0;
        if      (sel.equals("1")) { newTier = Member.TIER_SILVER; fee = 150.0; }
        else if (sel.equals("2")) { newTier = Member.TIER_GOLD;   fee = 250.0; }
        else {
            System.out.println("No changes made. Continuing as current member...");
            pressEnter(sc);
            return;
        }

        if (newTier.equals(m.getTier())) {
            System.out.println("[!] You are already on " + m.getTier() + " tier.");
            pressEnter(sc);
            return;
        }

    String processPayment = readYesNo(sc, "DO YOU WANT TO PROCESS PAYMENT? (Y/N): ");
    if (processPayment.equals("N")) {
    System.out.println("PAYMENT CANCELLED.");
    pressEnter(sc);
    return;
    }
    System.out.println("PROCEEDING TO PAYMENT...");

        String method = selectPaymentMethod(sc);
        System.out.println("PAYMENT SUCCESSFUL!");
        System.out.println("=========================================");
        System.out.println("UPGRADE CONFIRMATION");
        System.out.println("=========================================");
        System.out.println("MEMBER : " + m.getProfile().getName());
        System.out.println("TIER   : " + m.getTier() + " --> " + newTier);
        System.out.printf ("FEE    : RM %.2f / Month%n", fee);
        System.out.println("-----------------------------------------");
        System.out.println("CONGRATULATIONS! YOU ARE NOW A " + newTier + " MEMBER!");
        System.out.println("All features are now unlocked.");
        System.out.println("=========================================");

        m.upgradeMembership(newTier, method);
        system.rewriteMemberFile(m);
        system.logAudit("UPGRADE_MEMBERSHIP", m.getAccountId(),
                m.getAccountId(), "Upgraded to " + newTier);
        pressEnter(sc);
    }

	// membership tier table
    private static void printMembershipTable(String currentTier) {
        System.out.println("=====================================================================");
        System.out.println("MEMBERSHIP TIER COMPARISON");
        System.out.println("=====================================================================");
        System.out.printf("%-25s %-20s %-15s %-10s%n",
                "FEATURE", "BASIC (Current)", "SILVER (RM150)", "GOLD (RM250)");
        System.out.println("-".repeat(75));
        String[][] rows = {
            {"GYM FLOOR ACCESS", "YES", "YES", "YES"},
            {"GROUP CLASSES",    "NO",  "YES", "YES"},
            {"WELLNESS AREA",    "NO",  "NO",  "YES"},
            {"PERSONAL TRAINER", "NO",  "NO",  "YES"},
            {"GUEST PASS",       "NO",  "YES", "YES"},
            {"PARKING ACCESS",   "NO",  "NO",  "YES (Exclusive)"},
        };
        for (String[] row : rows) {
            System.out.printf("%-25s %-20s %-15s %-10s%n",
                    row[0], row[1], row[2], row[3]);
        }
        System.out.println("=====================================================================");
        System.out.println("YOUR CURRENT STATUS: " + currentTier + " MEMBER.");
    }

    // == MEMBER FUNCTION 7: RATE TRAINER =============================

    private static void memberRateTrainerMenu(Scanner sc, Member m,FitnessClubSystem system) {
        if (Member.TIER_BASIC.equals(m.getTier())) {
            System.out.println("[!] THIS FEATURE REQUIRES SILVER OR GOLD MEMBERSHIP.");
            String upgradeChoice = readYesNo(sc, "WOULD YOU LIKE TO UPGRADE? (Y/N): ");
            if (upgradeChoice.equals("Y")) {
                membershipUpgradeFlow(sc, m, system);
            }
            if (Member.TIER_BASIC.equals(m.getTier())) return;
        }
        List<Trainer> trainers = system.getAllTrainers();
        printTrainerList(trainers, false);
        int no = readInt(sc, "Please select the trainer you want to rate (IN NUMBER): ",
                1, trainers.size());
        Trainer t = trainers.get(no - 1);
        System.out.println("YOU SELECTED: " + t.getProfile().getName());
        double rating = readRating(sc, "Please select star rating (0.0 - 5.0): ");

        String confirmRating = readYesNo(sc, "ARE YOU SURE ABOUT THIS RATING? (Y/N): ");
        if (confirmRating.equals("Y")) {
        m.rateTrainer(no, rating, trainers);
        system.updateTrainerRating(t.getAccountId(), rating);
        System.out.println("RATING SUBMITTED SUCCESSFULLY!");
        System.out.println("Thank you for your feedback.");
        system.logAudit("RATE_TRAINER", m.getAccountId(),
            t.getAccountId(), "Rating: " + rating);
} else {
    System.out.println("RATING CANCELLED.");
}
pressEnter(sc);
       }
    // == MEMBER FUNCTION 8: SHOP ======================================

    private static void memberShopMenu(Scanner sc, Member m, FitnessClubSystem system) {
        while (true) {
            System.out.println("=====================================================================");
            System.out.println("SHOP (FITNESS PRODUCTS)");
            System.out.println("=====================================================================");
            System.out.println("1. SHOP");
            System.out.println("2. VIEW ORDER HISTORY");
            System.out.println("3. BACK");
            System.out.print("PLEASE SELECT (1-3): ");
            String sel = sc.nextLine().trim();

            if (sel.equals("1")) {
                shopBuyFlow(sc, m, system);
            } else if (sel.equals("2")) {
                viewOrderHistoryFlow(sc, m, system);
            } else if (sel.equals("3")) {
                return;
            } else {
                System.out.println("[!] Invalid selection.");
            }
        }
    }

	//shop buy flow
    private static void shopBuyFlow(Scanner sc, Member m, FitnessClubSystem system) {
    ShopCatalog catalog = system.getShopCatalog();

    while (true) {
        System.out.println("=====================================================================");
        System.out.println("SELECT PRODUCT CATEGORY");
        System.out.println("=====================================================================");
        System.out.println("1. FITNESS EQUIPMENT");
        System.out.println("2. SUPPLEMENTS");
        System.out.println("3. BACK TO MAIN MENU");
        System.out.print("PLEASE SELECT (1-3): ");
        String catSel = sc.nextLine().trim();

        String category;
        if (catSel.equals("1")) {
            category = Product.FITNESS_EQUIPMENT;
        } else if (catSel.equals("2")) {
            category = Product.SUPPLEMENTS;
        } else if (catSel.equals("3")) {
            return;
        } else {
            System.out.println("[!] Invalid selection. Please enter 1, 2, or 3.");
            continue;
        }

        List<Product> products = catalog.listByCategory(category);

        while (true) {
            System.out.printf("%-4s %-30s %-12s %-10s %-12s %-8s%n",
                    "NO", "PRODUCT NAME", "BRAND", "PRICE (RM)", "STATUS", "QTY");
            System.out.println("--------------------------------------------------------------------");
            int i = 1;
            for (Product p : products) {
                System.out.printf("%-4d %-30s %-12s %-10.2f %-12s %-8d%n",
                        i++, p.getName(), p.getBrand(), p.getPrice(),
                        p.isOutOfStock() ? "Out of Stock" : "In Stock", p.getStock());
            }
            System.out.println("--------------------------------------------------------------------");

            int no = readInt(sc, "ENTER PRODUCT NO TO PURCHASE (1-" + products.size() + "): ",
                    1, products.size());
            Product chosen = products.get(no - 1);

            if (chosen.isOutOfStock()) {
                System.out.println("[!] This product is OUT OF STOCK.");
                String continueBuying = readYesNo(sc, "DO YOU WANT TO CONTINUE BUYING? (Y/N): ");
                if (continueBuying.equals("Y")) {
                    continue;   // stay in current product list
                } else {
                    break;      // back to category menu
                }
            }

            int qty = readInt(sc, "ENTER QUANTITY: ", 1, chosen.getStock());
            double subTotal = chosen.getPrice() * qty;
            double sst      = subTotal * 0.06;
            double total    = subTotal + sst;

			//order comfirmation 
            System.out.println("-----------------------------------------");
            System.out.println("ORDER CONFIRMATION");
            System.out.println("-----------------------------------------");
            System.out.println("PRODUCT    : " + chosen.getName() + " (" + chosen.getBrand() + ")");
            System.out.println("QUANTITY   : " + qty);
            System.out.printf("UNIT PRICE : RM %.2f%n", chosen.getPrice());
            System.out.printf("SUBTOTAL   : RM %.2f%n", subTotal);
            System.out.printf("SST (6%%)   : RM %.2f%n", sst);
            System.out.printf("TOTAL      : RM %.2f%n", total);
            System.out.println("-----------------------------------------");

            String confirmPurchase = readYesNo(sc, "CONFIRM PURCHASE? (Y/N): ");
            if (confirmPurchase.equals("N")) {
                String continueBuying = readYesNo(sc, "DO YOU WANT TO CONTINUE BUYING? (Y/N): ");
                if (continueBuying.equals("Y")) {
                    continue;
                } else {
                    break;
                }
            }

            String method = selectPaymentMethod(sc);
            System.out.println("PAYMENT SUCCESSFUL!");

            Order order = m.shopPurchase(chosen, qty, method, catalog, system);
            if (order != null) {
                system.saveOrder(order);
                system.rewriteMemberFile(m);
                system.saveProductsPublic();

                String printReceipt = readYesNo(sc, "DO YOU WANT TO PRINT RECEIPT? (Y/N): ");
                if (printReceipt.equals("Y")) {
                    System.out.println(order.printReceipt());
                }

                system.logAudit("SHOP_PURCHASE", m.getAccountId(),
                        order.getOrderId(), "Purchased " + chosen.getName() + " x" + qty);

                pressEnter(sc);
                return;
            } else {
                System.out.println("[!] Purchase failed.");

                String continueBuying = readYesNo(sc, "DO YOU WANT TO CONTINUE BUYING? (Y/N): ");
                if (continueBuying.equals("Y")) {
                    continue;
                } else {
                    break;
                }
            }
        }
    }
}

	//view order history flow
    private static void viewOrderHistoryFlow(Scanner sc, Member m, FitnessClubSystem system) {
    List<Order> orders = system.getOrdersByMemberName(m.getProfile().getName());

    System.out.println("=====================================================================");
    System.out.println("MY ORDER HISTORY");
    System.out.println("=====================================================================");
    System.out.printf("%-4s %-15s %-12s %-30s %-10s%n",
            "NO", "ORDER ID", "DATE", "ITEMS", "TOTAL");
    System.out.println("--------------------------------------------------------------------");

    if (orders.isEmpty()) {
        System.out.println("No orders yet.");
        System.out.println("--------------------------------------------------------------------");
        pressEnter(sc);
        return;
    }

    int i = 1;
    for (Order o : orders) {
        System.out.printf("%-4d %-15s %-12s %-30s RM %.2f%n",
                i++, o.getOrderId(), o.getDate(),
                o.getItemsSummary(), o.getTotalPaid());
    }
    System.out.println("--------------------------------------------------------------------");

    String check = readYesNo(sc, "DO YOU WANT TO CHECK ORDER DETAILS? (Y/N): ");
    if (check.equals("N")) {
        pressEnter(sc);
        return;
    }

    while (true) {
        System.out.print("ENTER ORDER ID: ");
        String orderId = sc.nextLine().trim();

        if (orderId.isEmpty()) {
            System.out.println("[!] Order ID cannot be empty.");
            continue;
        }

        Order found = null;
        for (Order o : orders) {
            if (o.getOrderId().equalsIgnoreCase(orderId)) {
                found = o;
                break;
            }
        }

        if (found == null) {
            System.out.println("[!] Order ID not found. Please enter a valid Order ID.");
            continue;
        }

        System.out.println("-----------------------------------------");
        System.out.println("ORDER DETAILS: " + found.getOrderId());
        System.out.println("-----------------------------------------");
        System.out.println("DATE       : " + found.getDate());
        System.out.println("STATUS     : " + found.getStatus());
        System.out.println("ITEM       : " + found.getItemsSummary());
        System.out.printf ("TOTAL PAID : RM %.2f%n", found.getTotalPaid());
        System.out.println("PAYMENT    : " + found.getPaymentMethod());
        System.out.println("-----------------------------------------");
        break;
    }

    pressEnter(sc);
}

    // == MEMBER FUNCTION 9: LOYALTY REWARDS ==========================

    private static void memberLoyaltyMenu(Scanner sc, Member m, FitnessClubSystem system) {
        while (true) {
            System.out.println("=====================================================================");
            System.out.println("LOYALTY REWARDS CENTRE");
            System.out.println("=====================================================================");
            System.out.println("[USER]: " + m.getProfile().getName().toUpperCase()
                    + " | TIER: " + m.getTier());
            System.out.println("LOYALTY POINTS : " + m.getLoyaltyPoints() + " PTS");
            System.out.println("--------------------------------------------------------------------");
            System.out.println("1. REDEEM POINTS");
            System.out.println("2. POINTS HISTORY");
            System.out.println("3. BACK");
            System.out.print("PLEASE SELECT (1-3): ");
            String sel = sc.nextLine().trim();

            if (sel.equals("1")) {
                redeemRewardFlow(sc, m, system);
            } else if (sel.equals("2")) {
                System.out.println("=====================================================================");
                System.out.println("POINTS HISTORY");
                System.out.println("=====================================================================");
                System.out.print(m.viewPointsHistory());
                pressEnter(sc);
            } else if (sel.equals("3")) {
                return;
            } else {
                System.out.println("[!] Invalid selection.");
            }
        }
    }

	// redeem reward flow
    private static void redeemRewardFlow(Scanner sc, Member m, FitnessClubSystem system) {
        System.out.println("=====================================================================");
        System.out.println("REWARDS CATALOGUE");
        System.out.println("=====================================================================");
        System.out.printf("%-4s %-25s %-15s %-15s%n", "NO", "REWARD", "POINTS NEEDED", "STATUS");
        System.out.println("--------------------------------------------------------------------");
        for (int i = 1; i <= 4; i++) {
            String name   = LoyaltyPoints.REWARD_NAMES[i];
            int    pts    = LoyaltyPoints.REWARD_POINTS[i];
            String status = m.getLoyaltyPoints() >= pts ? "Redeemable" : "Not Enough Points";
            System.out.printf("%-4d %-25s %-15d %-15s%n", i, name, pts, status);
        }
        System.out.println("--------------------------------------------------------------------");
        int no = readInt(sc, "PLEASE SELECT (1-4): ", 1, 4);
        String rName  = LoyaltyPoints.REWARD_NAMES[no];
        int    rPts   = LoyaltyPoints.REWARD_POINTS[no];

        if (m.getLoyaltyPoints() < rPts) {
            System.out.println("[!] Not enough points to redeem this reward.");
            pressEnter(sc);
            return;
        }

        System.out.println("-----------------------------------------");
        System.out.println("CONFIRM REDEMPTION");
        System.out.println("-----------------------------------------");
        System.out.println("REWARD      : " + rName);
        System.out.println("POINTS COST : " + rPts + " PTS");
        System.out.println("YOUR POINTS : " + m.getLoyaltyPoints() + " PTS");
        System.out.println("AFTER       : " + (m.getLoyaltyPoints() - rPts) + " PTS REMAINING");
        String confirm = readYesNo(sc, "CONFIRM REDEMPTION? (Y/N): ");
        if (confirm.equals("N")) return;

        if (m.redeemReward(no)) {
        System.out.println("SUCCESS! Reward redeemed.");
        System.out.println("A " + rName + " voucher has been added to your account.");
        System.out.println("REMAINING POINTS: " + m.getLoyaltyPoints() + " PTS");
        system.rewriteMemberFile(m);
        system.logAudit("REDEEM_REWARD", m.getAccountId(), m.getAccountId(), "Redeemed reward: " + rName + " | Points used: " + rPts);
        } else {
        System.out.println("[!] Redemption failed.");
         }
        pressEnter(sc);
    }

    // ================================================================
    // SECTION 4: TRAINER LOGIN & DASHBOARD
    // ================================================================

    private static void trainerLoginFlow(Scanner sc, FitnessClubSystem system) {
    while (true) {
        System.out.println("================================================================");
        System.out.println("SECTION 4 : LOGIN - TRAINER");
        System.out.println("================================================================");

        System.out.print("PLEASE ENTER YOUR TRAINER ID: ");
        String trainerId = sc.nextLine().trim();

        System.out.print("PLEASE ENTER YOUR PASSWORD: ");
        String password = sc.nextLine();

        Trainer t = system.loginTrainer(trainerId, password);

        if (t == null) {
            System.out.println("[!] LOGIN FAILED. Wrong ID or password.");

            String choice;
            while (true) {
                System.out.print("DO YOU WANT TO CONTINUE LOGIN? (Y/N): ");
                choice = sc.nextLine().trim();

                if (choice.equalsIgnoreCase("Y") || choice.equalsIgnoreCase("N")) {
                    break;
                }

                System.out.println("[!] Invalid input. Please enter Y or N only.");
            }

            if (choice.equalsIgnoreCase("Y")) {
                continue;
            } else {
                pressEnter(sc);
                return;
            }
        }

        System.out.println("LOGIN SUCCESSFUL!");
        System.out.println("ROLE: " + t.getRole());
        system.logAudit("LOGIN_TRAINER", t.getAccountId(),t.getAccountId(), "Trainer login: " + t.getProfile().getName());

        trainerDashboard(sc, t, system);
        return;
    }
}

	//trainer dashboard
    private static void trainerDashboard(Scanner sc, Trainer t,FitnessClubSystem system) {
    while (true) {
        System.out.println("=========================================");
        System.out.println("TRAINER DASHBOARD");
        System.out.println("=========================================");
        System.out.println("Welcome, Coach " + t.getProfile().getName().toUpperCase());
        System.out.println("=========================================");
        System.out.println("1. VIEW MY SCHEDULE");
        System.out.println("2. MANAGE CLIENTS (UPDATE PROGRESS)");
        System.out.println("3. MY PROFILE");
        System.out.println("4. LOGOUT");
        System.out.print("PLEASE SELECT (1-4): ");
        String sel = sc.nextLine().trim();

        if (sel.equals("1")) {
            trainerViewSchedule(sc, t, system);
        } else if (sel.equals("2")) {
            trainerManageClients(sc, t, system);
        } else if (sel.equals("3")) {
            trainerProfileMenu(sc, t, system);
        } else if (sel.equals("4")) {
    String logoutChoice = readYesNo(sc, "DO YOU WANT TO LOGOUT? (Y/N): ");

    if (logoutChoice.equals("Y")) {
        System.out.println("GOODBYE, " + t.getProfile().getName().toUpperCase() + "!");
        system.logAudit("LOGOUT_TRAINER", t.getAccountId(),
                t.getAccountId(), "Trainer logout");
        pressEnter(sc);
        return;
    } else {
        System.out.println("LOGOUT CANCELLED.");
    }
            } else 
            	{
            System.out.println("[!] Invalid selection.");
        }
    }
}
	//trainer profile
     private static void trainerProfileMenu(Scanner sc, Trainer t, FitnessClubSystem system) {
      while (true) {
        System.out.println("================================");
        System.out.println("TRAINER PROFILE");
        System.out.println("================================");
        System.out.println("TRAINER ID   : " + t.getAccountId());
        System.out.println("NAME         : " + t.getProfile().getName());
        System.out.println("SPECIALTY    : " + t.getSpecialty());
        System.out.println("AGE          : " + t.getProfile().getAge());
        System.out.println("PHONE        : " + t.getProfile().getPhone());
        System.out.println("GENDER       : " + t.getProfile().getGender());
        System.out.println("EXPERIENCE   : " + t.getExperienceYears() + " Years");
        System.out.println("RATING       : " + String.format("%.1f", t.getRatingAvg()));
        System.out.println("MOTTO        : " + t.getMotto());
        System.out.println("================================");
        System.out.println("1. EDIT MOTTO");
        System.out.println("2. BACK");

        String sel = readMenuChoice(sc, "PLEASE SELECT (1-2): ", "1", "2");

        if (sel.equals("2")) {
            return;
        }

        System.out.print("ENTER NEW MOTTO: ");
        String newMotto = sc.nextLine().trim();

    if (newMotto.length() < 4 || newMotto.length() > 30) {
    System.out.println("[!] Motto must be between 4 and 30 characters.");
    pressEnter(sc);
    continue;
  }

        t.editMotto(newMotto);
        system.editTrainer(t.getAccountId(), null, null, null, 0, newMotto);
        System.out.println("Motto updated successfully.");
        pressEnter(sc);
    }
}

	// trainer view schedule
    private static void trainerViewSchedule(Scanner sc, Trainer t, FitnessClubSystem system) {
    while (true) {
        System.out.println("=========================================");
        System.out.println("VIEW MY SCHEDULE");
        System.out.println("=========================================");
        System.out.println("1. CLASS BOOKINGS");
        System.out.println("2. PERSONAL TRAINER BOOKINGS");
        System.out.println("3. BACK");

        String sel = readMenuChoice(sc, "PLEASE SELECT (1-3): ", "1", "2", "3");

        if (sel.equals("1")) {
            trainerViewClassBookings(sc, t, system); 
        } else if (sel.equals("2")) {
            trainerViewPersonalBookings(sc, t, system);
        } else {
            return;
        }
    }
}

	// trainer manage client
    private static void trainerManageClients(Scanner sc, Trainer t,FitnessClubSystem system) {
        List<Member> clients = t.viewAssignedClients(system.getAllMembers());

        System.out.println("=====================================================================");
        System.out.println("MY ASSIGNED CLIENTS");
        System.out.println("=====================================================================");
        System.out.printf("%-4s %-10s %-20s %-20s%n",
                "NO", "MEMBER ID", "NAME", "GOAL");
        System.out.println("--------------------------------------------------------------------");
        if (clients.isEmpty()) {
            System.out.println("No assigned clients.");
            pressEnter(sc);
            return;
        }
        int i = 1;
        for (Member mem : clients) {
            String goal = mem.getHealthProfile() != null
                    ? mem.getHealthProfile().getGoal() : "N/A";
            System.out.printf("%-4d %-10s %-20s %-20s%n",
                    i++, mem.getAccountId(), mem.getProfile().getName(), goal);
        }
        System.out.println("--------------------------------------------------------------------");

        System.out.print("ENTER MEMBER ID TO UPDATE: ");
        String mid = sc.nextLine().trim();
        Member target = null;
        for (Member mem : clients) {
            if (mem.getAccountId().equalsIgnoreCase(mid)) { target = mem; break; }
        }
        if (target == null) {
            System.out.println("[!] Member not found.");
            pressEnter(sc);
            return;
        }
		//update profile
        System.out.println("Loading Profile for: " + target.getProfile().getName() + "...");
        System.out.println("-------------------------------------");
        System.out.println("CLIENT PROFILE: " + target.getProfile().getName());
        System.out.println("-------------------------------------");
        if (target.getHealthProfile() != null) {
            System.out.println("Current Weight : " + target.getHealthProfile().getCurrentWeightKg() + " KG");
            System.out.println("Target Weight  : " + target.getHealthProfile().getTargetWeightKg() + " KG");
        }
        System.out.println("-------------------------------------");
        System.out.println("1. UPDATE CURRENT WEIGHT");
        System.out.println("2. CANCEL");
        System.out.print("PLEASE SELECT (1-2): ");
        if (sc.nextLine().trim().equals("1")) {
            double prev = target.getHealthProfile() != null
                    ? target.getHealthProfile().getCurrentWeightKg() : 0;
            double newW = readWeightKg(sc, "ENTER NEW WEIGHT (KG): ");
           String saveChoice = readYesNo(sc, "SAVE? (Y/N): ");
                if (saveChoice.equals("Y")) {
                String today = LocalDate.now().format(DATE_FMT);
                t.updateClientWeight(target.getAccountId(), newW, today, clients);
                system.editMemberPhysical(target.getAccountId(), newW, 0);
                System.out.println("UPDATE SUCCESSFUL!");
                if (target.getHealthProfile() != null) {
                    System.out.printf("PREVIOUS : %.1f KG (BMI %.2f)%n",
                            prev, prev / Math.pow(target.getHealthProfile().getHeightCm() / 100.0, 2));
                    System.out.printf("CURRENT  : %.1f KG (BMI %.2f)%n",
                            target.getHealthProfile().getCurrentWeightKg(),
                            target.getHealthProfile().getBMI());
                }
                system.logAudit("UPDATE_CLIENT_WEIGHT", t.getAccountId(),
                        target.getAccountId(), "Weight updated to " + newW + " kg");
            }
        }
        pressEnter(sc);
    }
    
    //trainer view personal bookings
   	private static void trainerViewPersonalBookings(Scanner sc, Trainer t, FitnessClubSystem system) {
    	List<Member> members = system.getAllMembers();

    	System.out.println("=====================================================================");
    	System.out.println("PERSONAL TRAINER BOOKINGS");
    	System.out.println("=====================================================================");
    	System.out.printf("%-4s %-10s %-20s %-12s %-18s %-12s%n",
            	"NO", "MEMBER ID", "NAME", "DATE", "TIME", "STATUS");
    	System.out.println("---------------------------------------------------------------------");

    	int no = 1;
    	boolean found = false;

    	for (Member m : members) {
        	for (TrainerAppointment ta : m.getAppointments()) {
            	if (ta.getTrainerId().equalsIgnoreCase(t.getAccountId())) {
                	System.out.printf("%-4d %-10s %-20s %-12s %-18s %-12s%n",
                        	no++,
                        	m.getAccountId(),
                        	m.getProfile().getName(),
                        	ta.getDate(),
                        	ta.getStartTime() + " - " + ta.getEndTime(),
                        	ta.getStatus());
                	found = true;
            	}
        	}
    	}

    	if (!found) {
       		System.out.println("No personal trainer bookings found.");
    	}

    	pressEnter(sc);
	}
  
  	// trainer view class booking
    private static void trainerViewClassBookings(Scanner sc, Trainer t, FitnessClubSystem system) {
    int year  = readBookingYear(sc);
    int month = readBookingMonth(sc);
    int week  = readWeekOfMonth(sc);

    LocalDate[] range = getWeekRange(year, month, week);
    LocalDate weekStart = range[0];
    LocalDate weekEnd   = range[1];

    List<ClassSession> sessions = system.getAllClassSessions();

    while (true) {
        System.out.println("=====================================================================");
        System.out.println("CLASS BOOKINGS");
        System.out.println("=====================================================================");
        System.out.println("YEAR  : " + year);
        System.out.println("MONTH : " + month);
        System.out.println("WEEK  : " + week);
        System.out.println("DATE  : " + formatDateRange(weekStart, weekEnd));

        if (isPastWeek(weekEnd)) {
            System.out.println("[!] WARNING: The selected week is already in the past.");
        }

        System.out.println("=====================================================================");
        System.out.printf("%-4s %-12s %-5s %-10s %-15s %-18s %-10s%n",
                "NO", "DATE", "DAY", "TIME", "CLASS", "LOCATION", "STATUS");
        System.out.println("--------------------------------------------------------------------");

        boolean any = false;

        for (ClassSession cs : sessions) {
            if (cs.getTrainerName().equalsIgnoreCase(t.getProfile().getName())) {
                LocalDate classDate = getClassDateFromDay(cs.getDay(), weekStart);

                String status = cs.hasAvailableSlot() ? "UPCOMING" : "FULL";

                System.out.printf("%-4d %-12s %-5s %-10s %-15s %-18s %-10s%n",
                        cs.getClassNo(),
                        classDate,
                        cs.getDay(),
                        cs.getTime(),
                        cs.getClassName(),
                        cs.getLocation(),
                        status);

                any = true;
            }
        }

        if (!any) {
            System.out.println("No class bookings found for this selected week.");
        }

        System.out.println("--------------------------------------------------------------------");
        System.out.println("1. CHANGE YEAR / MONTH / WEEK");
        System.out.println("2. BACK");

        String sel = readMenuChoice(sc, "PLEASE SELECT (1-2): ", "1", "2");

        if (sel.equals("1")) {
            year  = readBookingYear(sc);
            month = readBookingMonth(sc);
            week  = readWeekOfMonth(sc);

            range = getWeekRange(year, month, week);
            weekStart = range[0];
            weekEnd   = range[1];
        } else {
            pressEnter(sc);
            return;
        }
    }
}
    // ================================================================
    // SECTION 5: ADMIN LOGIN & DASHBOARD
    // ================================================================
   	private static void adminLoginFlow(Scanner sc, Admin admin, FitnessClubSystem system) {
    	while (true) {
        	System.out.println("================================================================");
        	System.out.println("SECTION 5 : LOGIN - ADMIN");
        	System.out.println("================================================================");

        	System.out.print("Please enter Admin ID: ");
        	String adminId = sc.nextLine().trim();

        	System.out.print("Please enter Password: ");
        	String password = sc.nextLine();

        	boolean ok = admin.getAccountId().equalsIgnoreCase(adminId)
                	&& admin.getPasswordHash().equals(Account.md5(password));

        	if (!ok) {
            	System.out.println("[!] LOGIN FAILED. Wrong ID or password.");

            	String choice;
            	while (true) {
                	System.out.print("DO YOU WANT TO CONTINUE LOGIN? (Y/N): ");
                	choice = sc.nextLine().trim();

                	if (choice.equalsIgnoreCase("Y") || choice.equalsIgnoreCase("N")) {
                    	break;
                	}

                	System.out.println("[!] Invalid input. Please enter Y or N only.");
            	}

            	if (choice.equalsIgnoreCase("Y")) {
                	continue;
            	} else {
                	pressEnter(sc);
                	return;
            	}
        	}

        	System.out.println("LOGIN SUCCESSFUL!");
            system.logAudit("LOGIN_ADMIN", admin.getAccountId(),
            admin.getAccountId(), "Admin login");

           adminDashboard(sc, admin, system);
           return;
    	}
	}
	
	//admin dashboard, admin function
    private static void adminDashboard(Scanner sc, Admin admin,FitnessClubSystem system) {
    while (true) {
        System.out.println("=========================================");
        System.out.println("ADMINISTRATION DASHBOARD");
        System.out.println("ROLE: " + admin.getRole());
        System.out.println("=========================================");
        System.out.println("Welcome, Administrator (" + admin.getAccountId() + ")");
        System.out.println("-----------------------------------------");
        System.out.println("1. MANAGE STAFF (TRAINERS)");
        System.out.println("2. MANAGE MEMBERSHIP RECORDS");
        System.out.println("3. GYM EQUIPMENT STATUS");
        System.out.println("4. GENERATE REVENUE REPORT");
        System.out.println("5. TRAINER STATUS MANAGEMENT");
        System.out.println("6. ADMIN ACTIVITY AUDIT TRAIL");
        System.out.println("7. MANAGE SHOP (PRODUCTS)");
        System.out.println("8. LOGOUT");
        System.out.print("Select option (1-8): ");
        String opt = sc.nextLine().trim();

        if (opt.equals("1")) {
            adminManageStaff(sc, admin, system);
        } else if (opt.equals("2")) {
            adminManageMembers(sc, admin, system);
        } else if (opt.equals("3")) {
            adminEquipmentMenu(sc, admin, system);
        } else if (opt.equals("4")) {
    int year = 2026;
    List<Integer> availableMonths = system.getAvailableRevenueMonths(year);
    if (availableMonths.isEmpty()) {
        System.out.println("[!] No revenue records found for year " + year + ".");
        pressEnter(sc);
        continue;
    }
     System.out.println("AVAILABLE REVENUE MONTHS IN " + year + ": " + availableMonths);
     int mon;
      while (true) {
        mon = readInt(sc, "ENTER MONTH " + availableMonths + ": ", 1, 12);

        if (!availableMonths.contains(mon)) {
            System.out.println("[!] Invalid month. You can only select months that contain revenue records.");
            continue;
        }
        break;
    }

	//generate revenue report
    System.out.println(admin.generateRevenueReport(system, mon, year));
    pressEnter(sc);
    }else if (opt.equals("5")) {
            adminTrainerStatus(sc, admin, system);
        } else if (opt.equals("6")) {
            adminAuditTrail(sc, admin, system);
        } else if (opt.equals("7")) {
            adminShopMenu(sc, admin, system);
        } else if (opt.equals("8")) {
            String logoutChoice = readYesNo(sc, "DO YOU WANT TO LOGOUT? (Y/N): ");
            if (logoutChoice.equals("Y")) {
                admin.logout();
                system.logAudit("LOGOUT_ADMIN", admin.getAccountId(),
                        admin.getAccountId(), "Admin logout");
                pressEnter(sc);
                return;
            } else {
                System.out.println("LOGOUT CANCELLED.");
            }
        } else {
            System.out.println("[!] Invalid option. Please enter 1-8.");
        }
    }
}

    // == ADMIN FUNCTION 1: MANAGE TRAINERS ===========================

    private static void adminManageStaff(Scanner sc, Admin admin,
                                          FitnessClubSystem system) {
        while (true) {
            System.out.println("=========================================");
            System.out.println("STAFF MANAGEMENT");
            System.out.println("=========================================");
            System.out.println("1. VIEW ALL TRAINERS (READ)");
            System.out.println("2. REGISTER NEW TRAINER (CREATE)");
            System.out.println("3. REMOVE TRAINER (DELETE)");
            System.out.println("4. EDIT TRAINER PROFILE");
            System.out.println("5. BACK");
            String sel = readMenuChoice(sc, "PLEASE SELECT (1-5): ", "1", "2", "3", "4", "5");
            
           if (sel.equals("1")) {
           List<Trainer> trainers = admin.viewAllTrainers(system);
           printAdminTrainerTable(trainers);

          String seeDetail = readYesNo(sc, "DO YOU WANT TO SEE TRAINER DETAIL INFORMATION (Y/N): ");
          if (seeDetail.equals("Y")) {
        System.out.print("PLEASE ENTER TRAINER ID: ");
        String tid = sc.nextLine().trim();

        Trainer t = system.findTrainerById(tid);
        if (t != null) {
            printTrainerProfile(t);
        } else {
            System.out.println("[!] Trainer not found.");
        }
    }

    pressEnter(sc);

            
            } else if (sel.equals("2")) {
                adminRegisterTrainer(sc, admin, system);
            } else if (sel.equals("3")) {
                adminRemoveTrainer(sc, admin, system);
            } else if (sel.equals("4")) {
                adminEditTrainer(sc, system);
            } else if (sel.equals("5")) {
                return;
            } else {
                System.out.println("[!] Invalid selection.");
            }
        }
    }

	//admin trainer table
    private static void printAdminTrainerTable(List<Trainer> trainers) {
        System.out.println("-----------------------------------------------------------------------");
        System.out.printf("%-8s %-20s %-15s %-10s %-8s %-8s%n",
                "ID", "NAME", "SPECIALTY", "RATING", "STATUS", "CLIENTS");
        System.out.println("-----------------------------------------------------------------------");
        for (Trainer t : trainers) {
            System.out.printf("%-8s %-20s %-15s %-10.1f %-8s %-8d%n",
                    t.getAccountId(), t.getProfile().getName(),
                    t.getSpecialty().split("/")[0].trim(),
                    t.getRatingAvg(), t.getAvailabilityStatus(), 0);
        }
        System.out.println("-----------------------------------------------------------------------");
    }
	//trainer profile
    private static void printTrainerProfile(Trainer t) {
        System.out.println("========================================");
        System.out.println("TRAINER PROFILE");
        System.out.println("========================================");
        System.out.println("ID         : " + t.getAccountId());
        System.out.println("NAME       : " + t.getProfile().getName());
        System.out.println("PHONE      : " + t.getProfile().getPhone());
        System.out.println("SPECIALTY  : " + t.getSpecialty());
        System.out.println("EXPERIENCE : " + t.getExperienceYears() + " Years");
        System.out.printf ("RATING     : %.1f / 5.0%n", t.getRatingAvg());
        System.out.println("MOTTO      : \"" + t.getMotto() + "\"");
        System.out.println("STATUS     : " + t.getAvailabilityStatus());
        System.out.println("==========================================");
    }
	//admin register trainer 
    private static void adminRegisterTrainer(Scanner sc, Admin admin,FitnessClubSystem system) {
    System.out.println("-----------------------------------------");
    System.out.println("NEW TRAINER REGISTRATION");
    System.out.println("-----------------------------------------");

    String name;
    while (true) {
        name = readName(sc, "ENTER TRAINER NAME: ");
        if (system.trainerNameExists(name)) {
            System.out.println("[!] This trainer name is already registered. Please use another name.");
        } else {
            break;
        }
    }

    String specialty = readSpecialty(sc);
    int age = readTrainerAge(sc, "PLEASE ENTER AGE: ");

    String phone;
    while (true) {
        phone = readPhone(sc, "PLEASE ENTER PHONE NUMBER: ");
        if (system.trainerPhoneExists(phone)) {
            System.out.println("[!] This trainer phone number is already registered.");
        } else {
            break;
        }
    }

    String gender = readGender(sc, "PLEASE ENTER GENDER (M/F): ");
    int exp = readExperience(sc, age);
    String password = readPassword(sc, "PLEASE ENTER PASSWORD: ");

    String confirm = readYesNo(sc, "CONFIRM REGISTRATION? (Y/N): ");
    if (confirm.equals("N")) {
        System.out.println("REGISTRATION CANCELLED.");
        pressEnter(sc);
        return;
    }

    try {
        Trainer t = admin.registerTrainer(system, name, specialty, age,
                phone, gender, exp, password);

        if (t != null) {
            System.out.println("SUCCESS: Trainer [" + name + "] has been added.");
            System.out.println("YOUR TRAINER ID IS: " + t.getAccountId());
            system.logAudit("REGISTER_TRAINER", admin.getAccountId(),
                    t.getAccountId(), "Registered trainer: " + name);
        } else {
            System.out.println("[!] Registration failed.");
        }
    } catch (IllegalArgumentException e) {
        System.out.println("[!] " + e.getMessage());
    } catch (Exception e) {
        System.out.println("[!] Registration failed.");
    }

    pressEnter(sc);
}

	//admin remove trainer
    private static void adminRemoveTrainer(Scanner sc, Admin admin,FitnessClubSystem system) {
        List<Trainer> trainers = admin.viewAllTrainers(system);
        printAdminTrainerTable(trainers);
        System.out.print("ENTER TRAINER ID TO REMOVE: ");
        String tid = sc.nextLine().trim();
        Trainer t = system.findTrainerById(tid);
        if (t == null) { System.out.println("[!] Trainer not found."); pressEnter(sc); return; }

        System.out.println("TRAINER: " + t.getProfile().getName() + " (" + tid + ")");
        System.out.println("[WARNING]: This action is permanent.");
        String confirm = readYesNo(sc, "ARE YOU SURE? (Y/N): ");

    if (confirm.equals("Y")) {
    if (admin.removeTrainer(system, tid)) {
        System.out.println("SUCCESS: Trainer [" + t.getProfile().getName() + "] has been removed.");
        system.logAudit("REMOVE_TRAINER", admin.getAccountId(), tid,
                "Removed trainer: " + t.getProfile().getName());
         } else {
        System.out.println("[!] Remove failed.");
       }
      } else {
    System.out.println("REMOVE CANCELLED.");
     }

      pressEnter(sc);
    }
    
    //admin edit trainer
    private static void adminEditTrainer(Scanner sc, FitnessClubSystem system) {
    System.out.print("ENTER TRAINER ID TO EDIT: ");
    String tid = sc.nextLine().trim();
    Trainer t = system.findTrainerById(tid);

    if (t == null) {
        System.out.println("[!] Trainer not found.");
        pressEnter(sc);
        return;
    }

    printTrainerProfile(t);
    System.out.println("1. EDIT NAME");
    System.out.println("2. EDIT PHONE NUMBER");
    System.out.println("3. EDIT SPECIALTY");
    System.out.println("4. EDIT EXPERIENCE");
    System.out.println("5. EDIT MOTTO");
    System.out.println("6. BACK");

    String sel = readMenuChoice(sc, "PLEASE SELECT (1-6): ",
            "1", "2", "3", "4", "5", "6");

    String name = null, phone = null, specialty = null, motto = null;
    int expYears = 0;

    if (sel.equals("1")) {
        name = readName(sc, "ENTER NEW NAME: ");
    } else if (sel.equals("2")) {
        phone = readPhone(sc, "ENTER NEW PHONE: ");
    } else if (sel.equals("3")) {
        specialty = readSpecialty(sc);
    } else if (sel.equals("4")) {
        expYears = readExperience(sc, t.getProfile().getAge());
    } else if (sel.equals("5")) {
        System.out.print("ENTER NEW MOTTO: ");
        motto = sc.nextLine().trim();
    } else if (sel.equals("6")) {
        return;
    }

    String confirm = readYesNo(sc, "CONFIRM UPDATE? (Y/N): ");
    if (confirm.equals("Y")) {
        system.editTrainer(tid, name, phone, specialty, expYears, motto);
        System.out.println("UPDATE SUCCESSFUL!");
    } else {
        System.out.println("UPDATE CANCELLED.");
    }

    pressEnter(sc);
}

    // == ADMIN FUNCTION 2: MANAGE MEMBERS ============================

    // ================================================================
// ADMIN - MANAGE MEMBERSHIP RECORDS
// Replace your existing member-management methods with this block
// ================================================================

private static void  adminManageMembers(Scanner sc, Admin admin, FitnessClubSystem system) {
    while (true) {
        System.out.println("==============================================================");
        System.out.println("MEMBER MANAGEMENT");
        System.out.println("==============================================================");
        System.out.println("1. VIEW & EDIT MEMBERS");
        System.out.println("2. BACK");

        String sel = readMenuChoice(sc, "PLEASE SELECT (1-2): ", "1", "2");

        if (sel.equals("1")) {
            adminViewEditMembers(sc, admin, system);
        } else {
            return;
        }
    }
}

//admin view edit member
private static void adminViewEditMembers(Scanner sc, Admin admin, FitnessClubSystem system) {
    while (true) {
        List<Member> members = admin.viewAllMembers(system);

        System.out.println("==============================================================");
        System.out.println("MEMBER LIST");
        System.out.println("==============================================================");
        printMemberList(members);

        String editChoice = readYesNo(sc, "DID YOU WANT TO EDIT MEMBER (Y/N): ");
        if (editChoice.equals("N")) {
            return;
        }

        System.out.print("ENTER MEMBER ID OR NAME: ");
        String input = sc.nextLine().trim();

        if (input.isEmpty()) {
            System.out.println("[!] Member ID or name cannot be empty.");
            pressEnter(sc);
            continue;
        }

        Member found = null;
        for (Member mem : members) {
            if ("DELETED".equalsIgnoreCase(mem.getStatus())) continue;

            if (mem.getAccountId().equalsIgnoreCase(input)
                    || mem.getProfile().getName().equalsIgnoreCase(input)) {
                found = mem;
                break;
            }
        }

        if (found == null) {
            System.out.println("[!] Member not found.");
            pressEnter(sc);
            continue;
        }

        if ("DELETED".equalsIgnoreCase(found.getStatus())) {
            System.out.println("[!] This member has been permanently deleted and cannot be edited.");
            pressEnter(sc);
            continue;
        }

        if ("DISABLED".equalsIgnoreCase(found.getStatus())) {
            adminDisabledMemberMenu(sc, admin, system, found);
        } else {
            adminMemberEditMenu(sc, admin, system, found);
        }
    }
}

//print member list
private static void printMemberList(List<Member> members) {
    System.out.printf("%-10s %-22s %-10s %-10s %-12s%n",
            "ID", "NAME", "GENDER", "TIER", "STATUS");
    System.out.println("------------------------------------------------------------------");

    for (Member m : members) {
        if ("DELETED".equalsIgnoreCase(m.getStatus())) continue;

        System.out.printf("%-10s %-22s %-10s %-10s %-12s%n",
                m.getAccountId(),
                m.getProfile().getName(),
                m.getProfile().getGender(),
                m.getTier(),
                m.getStatus());
    }

    System.out.println("------------------------------------------------------------------");
}

//admin member edit menu
private static void adminMemberEditMenu(Scanner sc, Admin admin, FitnessClubSystem system, Member m) {
    while (true) {
        System.out.println("-----------------------------------------");
        System.out.println("MEMBER DETAILS: " + m.getAccountId());
        System.out.println("-----------------------------------------");
        System.out.println("[PERSONAL INFO]");
        System.out.println("NAME     : " + m.getProfile().getName());
        System.out.println("AGE      : " + m.getProfile().getAge());
        System.out.println("GENDER   : " + m.getProfile().getGender());
        System.out.println("PHONE    : " + m.getProfile().getPhone());
        System.out.println("PASSWORD : ****** (Hidden)");
        System.out.println("[PHYSICAL STATS]");
        if (m.getHealthProfile() != null) {
            System.out.printf("WEIGHT   : %.1f KG%n", m.getHealthProfile().getCurrentWeightKg());
            System.out.printf("HEIGHT   : %.1f CM%n", m.getHealthProfile().getHeightCm());
            System.out.printf("BMI      : %.2f (%s)%n",
                    m.getHealthProfile().getBMI(), m.getHealthProfile().getBMIStatus());
        } else {
            System.out.println("WEIGHT/HEIGHT/BMI: N/A");
        }
        //print and edit membership info
        System.out.println("[MEMBERSHIP INFO]");
        System.out.println("TIER     : " + m.getTier());
        System.out.println("STATUS   : " + m.getStatus());
        System.out.println("-----------------------------------------");
        System.out.println("WHAT WOULD YOU LIKE TO EDIT?");
        System.out.println("1. EDIT PERSONAL INFO (Name, Phone, Age, Password)");
        System.out.println("2. EDIT PHYSICAL STATS (Weight, Height)");
        System.out.println("3. CHANGE MEMBERSHIP TIER");
        System.out.println("4. DELETE & BLOCK MEMBER");
        System.out.println("5. BACK TO LIST");

        String sel = readMenuChoice(sc, "PLEASE SELECT (1-5): ", "1", "2", "3", "4", "5");

        if (sel.equals("1")) {
            adminEditPersonalInfo(sc, admin, system, m);
        } else if (sel.equals("2")) {
            adminEditPhysicalStats(sc, admin, system, m);
        } else if (sel.equals("3")) {
            adminChangeTier(sc, admin, system, m);
        } else if (sel.equals("4")) {
            adminManageMemberStatus(sc, admin, system, m);
            return;
        } else {
            return;
        }
    }
}

//admin disable member menu
private static void adminDisabledMemberMenu(Scanner sc, Admin admin, FitnessClubSystem system, Member m) {
    while (true) {
        System.out.println("-----------------------------------------");
        System.out.println("DISABLED MEMBER DETAILS: " + m.getAccountId());
        System.out.println("-----------------------------------------");
        System.out.println("NAME   : " + m.getProfile().getName());
        System.out.println("STATUS : " + m.getStatus());
        System.out.println("TIER   : " + m.getTier());
        System.out.println("-----------------------------------------");
        System.out.println("1. REACTIVATE MEMBER");
        System.out.println("2. PERMANENT DELETE");
        System.out.println("3. BACK");

        String sel = readMenuChoice(sc, "PLEASE SELECT (1-3): ", "1", "2", "3");

        if (sel.equals("1")) {
            String confirm = readYesNo(sc, "CONFIRM REACTIVATE? (Y/N): ");
            if (confirm.equals("Y")) {
                admin.deleteAndBlockMember(system, m.getAccountId(), "ACTIVE");
                m.setStatus("ACTIVE");
                System.out.println("MEMBER REACTIVATED SUCCESSFULLY.");
                system.logAudit("REACTIVATE_MEMBER", admin.getAccountId(),
                        m.getAccountId(), "Disabled member reactivated");
            } else {
                System.out.println("REACTIVATION CANCELLED.");
            }
            pressEnter(sc);

        } else if (sel.equals("2")) {
            String confirm = readYesNo(sc, "CONFIRM PERMANENT DELETE? (Y/N): ");
            if (confirm.equals("Y")) {
                admin.deleteAndBlockMember(system, m.getAccountId(), "DELETED");
                m.setStatus("DELETED");
                System.out.println("MEMBER PERMANENTLY DELETED.");
                system.logAudit("PERMANENT_DELETE_MEMBER", admin.getAccountId(),
                        m.getAccountId(), "Disabled member permanently deleted");
            } else {
                System.out.println("DELETE CANCELLED.");
            }
            pressEnter(sc);
            return;

        } else {
            return;
        }
    }
}

//admin edit personal info
private static void adminEditPersonalInfo(Scanner sc, Admin admin, FitnessClubSystem system, Member m) {
    if ("DISABLED".equalsIgnoreCase(m.getStatus())) {
        System.out.println("[!] Disabled member account cannot edit personal info.");
        pressEnter(sc);
        return;
    }

    while (true) {
        System.out.println("1. EDIT NAME");
        System.out.println("2. EDIT PHONE NUMBER");
        System.out.println("3. EDIT AGE");
        System.out.println("4. EDIT PASSWORD");
        System.out.println("5. BACK");

        String sel = readMenuChoice(sc, "PLEASE SELECT (1-5): ", "1", "2", "3", "4", "5");
        if (sel.equals("5")) return;

        if (sel.equals("1")) {
            System.out.println("CURRENT NAME: " + m.getProfile().getName());
            String newName = readName(sc, "ENTER NEW NAME: ");
            String save = readYesNo(sc, "SAVE? (Y/N): ");

            if (save.equals("Y")) {
                m.getProfile().setName(newName);
                system.rewriteMemberFile(m);
                system.logAudit("EDIT_MEMBER_NAME", admin.getAccountId(),
                        m.getAccountId(), "Changed member name to " + newName);
                System.out.println("UPDATE SUCCESSFUL!");
            } else {
                System.out.println("UPDATE CANCELLED.");
            }
            pressEnter(sc);
            return;
        }

        if (sel.equals("2")) {
            System.out.println("CURRENT PHONE: " + m.getProfile().getPhone());
            String newPhone = readPhone(sc, "ENTER NEW PHONE NUMBER: ");
            String save = readYesNo(sc, "SAVE? (Y/N): ");

            if (save.equals("Y")) {
                m.getProfile().setPhone(newPhone);
                system.rewriteMemberFile(m);
                system.logAudit("EDIT_MEMBER_PHONE", admin.getAccountId(),
                        m.getAccountId(), "Changed member phone");
                System.out.println("UPDATE SUCCESSFUL!");
            } else {
                System.out.println("UPDATE CANCELLED.");
            }
            pressEnter(sc);
            return;
        }

        if (sel.equals("3")) {
            System.out.println("CURRENT AGE: " + m.getProfile().getAge());
            int newAge = readAge(sc, "ENTER NEW AGE: ");
            String save = readYesNo(sc, "SAVE? (Y/N): ");

            if (save.equals("Y")) {
                m.getProfile().setAge(newAge);
                system.rewriteMemberFile(m);
                system.logAudit("EDIT_MEMBER_AGE", admin.getAccountId(),
                        m.getAccountId(), "Changed member age to " + newAge);
                System.out.println("UPDATE SUCCESSFUL!");
            } else {
                System.out.println("UPDATE CANCELLED.");
            }
            pressEnter(sc);
            return;
        }

        if (sel.equals("4")) {
            String change = readYesNo(sc, "DO YOU WANT TO CHANGE PASSWORD? (Y/N): ");
            if (change.equals("N")) {
                System.out.println("UPDATE CANCELLED.");
                pressEnter(sc);
                return;
            }

            String newPassword = readPassword(sc, "ENTER NEW PASSWORD: ");
            String save = readYesNo(sc, "SAVE? (Y/N): ");

            if (save.equals("Y")) {
                m.setPasswordHash(Account.md5(newPassword));
                system.rewriteMemberFile(m);
                system.logAudit("EDIT_MEMBER_PASSWORD", admin.getAccountId(),
                        m.getAccountId(), "Changed member password");
                System.out.println("UPDATE SUCCESSFUL!");
            } else {
                System.out.println("UPDATE CANCELLED.");
            }
            pressEnter(sc);
            return;
        }
    }
}
//admin edit physical status
private static void adminEditPhysicalStats(Scanner sc, Admin admin, FitnessClubSystem system, Member m) {
    if ("DISABLED".equalsIgnoreCase(m.getStatus())) {
        System.out.println("[!] Disabled member account cannot edit physical stats.");
        pressEnter(sc);
        return;
    }

    if (m.getHealthProfile() == null) {
        System.out.println("[!] This member has no health profile yet.");
        pressEnter(sc);
        return;
    }

    while (true) {
        System.out.println("1. EDIT WEIGHT");
        System.out.println("2. EDIT HEIGHT");
        System.out.println("3. BACK");

        String sel = readMenuChoice(sc, "PLEASE SELECT (1-3): ", "1", "2", "3");
        if (sel.equals("3")) return;

        if (sel.equals("1")) {
            double newWeight = readWeightKg(sc, "ENTER NEW WEIGHT (KG): ");
            String save = readYesNo(sc, "SAVE? (Y/N): ");

            if (save.equals("Y")) {
                m.updateBMI("WEIGHT", newWeight);
                system.rewriteMemberFile(m);
                system.logAudit("EDIT_MEMBER_WEIGHT", admin.getAccountId(),
                        m.getAccountId(), "Changed member weight to " + newWeight);
                System.out.println("UPDATE SUCCESSFUL!");
            } else {
                System.out.println("UPDATE CANCELLED.");
            }
            pressEnter(sc);
            return;
        }

        if (sel.equals("2")) {
            double newHeight = readHeightCm(sc, "ENTER NEW HEIGHT (CM): ");
            String save = readYesNo(sc, "SAVE? (Y/N): ");

            if (save.equals("Y")) {
                m.updateBMI("HEIGHT", newHeight);
                system.rewriteMemberFile(m);
                system.logAudit("EDIT_MEMBER_HEIGHT", admin.getAccountId(),
                        m.getAccountId(), "Changed member height to " + newHeight);
                System.out.println("UPDATE SUCCESSFUL!");
            } else {
                System.out.println("UPDATE CANCELLED.");
            }
            pressEnter(sc);
            return;
        }
    }
}

//admin change tier
private static void adminChangeTier(Scanner sc, Admin admin, FitnessClubSystem system, Member m) {
    if ("DISABLED".equalsIgnoreCase(m.getStatus())) {
        System.out.println("[!] Disabled member account cannot change membership tier.");
        pressEnter(sc);
        return;
    }

    System.out.println("CURRENT TIER: " + m.getTier());
    String ask = readYesNo(sc, "DO YOU WANT TO EDIT TIER? (Y/N): ");

    if (ask.equals("N")) {
        System.out.println("UPDATE CANCELLED.");
        pressEnter(sc);
        return;
    }

    System.out.println("1. BASIC");
    System.out.println("2. SILVER");
    System.out.println("3. GOLD");

    String sel = readMenuChoice(sc, "PLEASE SELECT (1-3): ", "1", "2", "3");

    String newTier;
    if (sel.equals("1")) {
        newTier = Member.TIER_BASIC;
    } else if (sel.equals("2")) {
        newTier = Member.TIER_SILVER;
    } else {
        newTier = Member.TIER_GOLD;
    }

    if (newTier.equalsIgnoreCase(m.getTier())) {
        System.out.println("[!] Member is already in " + newTier + " tier.");
        pressEnter(sc);
        return;
    }

    String confirm = readYesNo(sc, "CONFIRM TIER CHANGE? (Y/N): ");
    if (confirm.equals("Y")) {
        m.upgradeMembership(newTier, "ADMIN_UPDATE");
        system.rewriteMemberFile(m);
        system.logAudit("CHANGE_MEMBER_TIER", admin.getAccountId(),
                m.getAccountId(), "Changed member tier to " + newTier);
        System.out.println("TIER UPDATED SUCCESSFULLY.");
    } else {
        System.out.println("UPDATE CANCELLED.");
    }

    pressEnter(sc);
}

//admin manage member states
private static void adminManageMemberStatus(Scanner sc, Admin admin, FitnessClubSystem system, Member m) {
    String status = m.getStatus();

    if ("DISABLED".equalsIgnoreCase(status)) {
        adminDisabledMemberMenu(sc, admin, system, m);
        return;
    }

    if ("ACTIVE".equalsIgnoreCase(status)) {
        System.out.println("1. BLOCK MEMBER");
        System.out.println("2. PERMANENT DELETE");
        System.out.println("3. BACK");

        String sel = readMenuChoice(sc, "PLEASE ENTER (1-3): ", "1", "2", "3");
        if (sel.equals("3")) return;

        String newStatus = sel.equals("1") ? "BLOCKED" : "DELETED";
        String confirm = readYesNo(sc, "CONFIRM? (Y/N): ");

        if (confirm.equals("Y")) {
            admin.deleteAndBlockMember(system, m.getAccountId(), newStatus);
            m.setStatus(newStatus);
            system.rewriteMemberFile(m);
            system.logAudit("MEMBER_STATUS_CHANGE", admin.getAccountId(),
                    m.getAccountId(), "Status changed to " + newStatus);
            System.out.println("STATUS UPDATED TO " + newStatus + ".");
        } else {
            System.out.println("UPDATE CANCELLED.");
        }

        pressEnter(sc);
        return;
    }

    if ("BLOCKED".equalsIgnoreCase(status)) {
        System.out.println("1. UNBLOCK MEMBER");
        System.out.println("2. PERMANENT DELETE");
        System.out.println("3. BACK");

        String sel = readMenuChoice(sc, "PLEASE ENTER (1-3): ", "1", "2", "3");
        if (sel.equals("3")) return;

        String newStatus = sel.equals("1") ? "ACTIVE" : "DELETED";
        String confirm = readYesNo(sc, "CONFIRM? (Y/N): ");

        if (confirm.equals("Y")) {
            admin.deleteAndBlockMember(system, m.getAccountId(), newStatus);
            m.setStatus(newStatus);
            system.rewriteMemberFile(m);
            system.logAudit("MEMBER_STATUS_CHANGE", admin.getAccountId(),
                    m.getAccountId(), "Status changed to " + newStatus);
            System.out.println("STATUS UPDATED TO " + newStatus + ".");
        } else {
            System.out.println("UPDATE CANCELLED.");
        }

        pressEnter(sc);
        return;
    }

    System.out.println("[!] Unsupported member status: " + status);
    pressEnter(sc);
}

    // == ADMIN FUNCTION 3: EQUIPMENT ==================================
    //update and add equipment
    private static void adminEquipmentMenu(Scanner sc, Admin admin, FitnessClubSystem system) {
    while (true) {
        List<Equipment> equip = admin.viewEquipmentStatus(system);

        System.out.println("===================================================================");
        System.out.println("GYM EQUIPMENT INVENTORY");
        System.out.println("===================================================================");
        System.out.printf("%-6s %-25s %-12s %-12s%n", "ID", "NAME", "TYPE", "STATUS");
        System.out.println("-------------------------------------------------------------------");

        for (Equipment eq : equip) {
            System.out.printf("%-6s %-25s %-12s %-12s%n",
                    eq.getEquipmentId(),
                    eq.getName(),
                    eq.getCategory(),
                    eq.getStatus());
        }

        System.out.println("-------------------------------------------------------------------");
        System.out.println("1. UPDATE EQUIPMENT STATUS");
        System.out.println("2. ADD NEW EQUIPMENT");
        System.out.println("3. BACK");

        String sel = readMenuChoice(sc, "PLEASE SELECT (1-3): ", "1", "2", "3");

        if (sel.equals("1")) {
            String eid = readEquipmentId(sc, "ENTER EQUIPMENT ID TO UPDATE: ");

            System.out.println("1. AVAILABLE");
            System.out.println("2. UNDER MAINTENANCE");
            System.out.println("3. IN USE");
            System.out.println("4. BACK");

            int s = readInt(sc, "SELECT NEW STATUS (1-4): ", 1, 4);

            if (s == 4) {
                continue;
            }

            String newStatus;
            if (s == 1) {
                newStatus = "AVAILABLE";
            } else if (s == 2) {
                newStatus = "MAINTENANCE";
            } else {
                newStatus = "IN_USE";
            }

            if (admin.updateEquipmentStatus(system, eid, newStatus)) {
                System.out.println("SUCCESS: Equipment status updated to [" + newStatus + "].");
                system.logAudit("UPDATE_EQUIPMENT", admin.getAccountId(),
                        eid, "Status changed to " + newStatus);
            } else {
                System.out.println("[!] Equipment not found. Please enter an existing Equipment ID.");
            }

            pressEnter(sc);

        } else if (sel.equals("2")) {
    String name = readTextLength(sc, "ENTER EQUIPMENT NAME: ", 2, 50);
    String zone = readTextLength(sc, "ENTER ZONE: ", 2, 50);

    int cat = readInt(sc, "ENTER CATEGORY (1-Cardio / 2-Strength): ", 1, 2);
    String category = (cat == 1) ? "CARDIO" : "STRENGTH";

    String confirm = readYesNo(sc, "SAVE THIS EQUIPMENT? (Y/N): ");
    if (confirm.equals("N")) {
        System.out.println("ADD EQUIPMENT CANCELLED.");
        pressEnter(sc);
        continue;
    }

    Equipment ne = admin.addEquipment(system, name, zone, category, "AVAILABLE");
    System.out.println("SUCCESS: Equipment [" + name + "] added as " + ne.getEquipmentId() + ".");
    system.logAudit("ADD_EQUIPMENT", admin.getAccountId(),
            ne.getEquipmentId(), "Added: " + name);

    pressEnter(sc);
} else {
            return;
        }
    }
}

    // == ADMIN FUNCTION 5: TRAINER STATUS MANAGEMENT ==================

    private static void adminTrainerStatus(Scanner sc, Admin admin,FitnessClubSystem system) {
    while (true) {
        List<Trainer> trainers = admin.viewAllTrainers(system);

        System.out.println("=========================================");
        System.out.println("TRAINER STATUS MANAGEMENT");
        System.out.println("=========================================");
        System.out.println("[NOTE]: Client records will be preserved.");
        printAdminTrainerTable(trainers);

        Trainer t = null;
        String tid = "";

        while (true) {
            System.out.print("ENTER TRAINER ID TO MANAGE: ");
            tid = sc.nextLine().trim();

            if (tid.isEmpty()) {
                System.out.println("[!] Trainer ID cannot be empty.");
            } else {
                t = system.findTrainerById(tid);
                if (t != null) {
                    break;
                }
                System.out.println("[!] Trainer not found.");
            }

            String again = readYesNo(sc, "DO YOU WANT TO CONTINUE ENTER TRAINER ID? (Y/N): ");
            if (again.equals("N")) {
                return;
            }
        }

        System.out.println("MANAGE STATUS: " + t.getProfile().getName() + " (" + tid + ")");
        System.out.println("CURRENT STATUS: " + t.getStatus());
        System.out.println("1. DISABLE TRAINER ACCOUNT");
        System.out.println("2. ENABLE TRAINER ACCOUNT");
        System.out.println("3. BACK");

        String choice = readMenuChoice(sc, "PLEASE SELECT (1-3): ", "1", "2", "3");

        if (choice.equals("3")) {
            return;
        }

        String newStatus = choice.equals("1") ? "DISABLED" : "ACTIVE";

        if (newStatus.equalsIgnoreCase(t.getStatus())) {
            System.out.println("[!] Trainer is already " + newStatus + ".");
            pressEnter(sc);
            continue;
        }

        String confirm = readYesNo(sc, "CONFIRM? (Y/N): ");

        if (confirm.equals("Y")) {
            admin.manageTrainerStatus(system, tid, newStatus);
            System.out.println("SUCCESS: Trainer [" + t.getProfile().getName()
                    + "] account has been " + (choice.equals("1") ? "DISABLED" : "RE-ENABLED") + ".");
            system.logAudit("TRAINER_STATUS_CHANGE", admin.getAccountId(),
                    tid, "Status changed to " + newStatus);
        } else {
            System.out.println("UPDATE CANCELLED.");
        }

        pressEnter(sc);
        return;
    }
}

    // == ADMIN FUNCTION 6: AUDIT TRAIL ================================
	//filter by date
    private static void adminAuditTrail(Scanner sc, Admin admin, FitnessClubSystem system) {
    while (true) {
        System.out.println("==============================================================");
        System.out.println("ADMIN AUDIT TRAIL LOG (" + admin.getAccountId() + ")");
        System.out.println("==============================================================");

        List<String> logs = system.viewAuditTrail();

        if (logs.isEmpty()) {
            System.out.println("No audit entries found.");
        } else {
            printAuditTable(logs);
        }

        System.out.println("1. FILTER BY DATE");
        System.out.println("2. BACK TO DASHBOARD");

        String choice = readMenuChoice(sc, "PLEASE SELECT (1-2): ", "1", "2");

        if (choice.equals("2")) {
            return;
        }

        String date = readExistingAuditDate(sc, system, "ENTER DATE (YYYY-MM-DD): ");
        List<String> filtered = system.filterAuditTrail(date);

        System.out.println("==============================================================");
        System.out.println("AUDIT LOG - " + date);
        System.out.println("==============================================================");

        if (filtered.isEmpty()) {
            System.out.println("No entries found for this date.");
        } else {
            printAuditTable(filtered);
        }

        System.out.println("1. FILTER AGAIN");
        System.out.println("2. BACK TO DASHBOARD");
        String next = readMenuChoice(sc, "PLEASE SELECT (1-2): ", "1", "2");

        if (next.equals("2")) {
            return;
        }
    }
}
    // == ADMIN FUNCTION 7: SHOP MANAGEMENT ===========================
    //manage product, product summary
    private static void adminShopMenu(Scanner sc, Admin admin, FitnessClubSystem system) {
    while (true) {
        System.out.println("=========================================");
        System.out.println("SHOP MANAGEMENT");
        System.out.println("=========================================");
        System.out.println("1. VIEW ALL PRODUCTS");
        System.out.println("2. ADD NEW PRODUCT");
        System.out.println("3. EDIT PRODUCT");
        System.out.println("4. DELETE PRODUCT");
        System.out.println("5. VIEW ALL ORDERS");
        System.out.println("6. BACK");

        String sel = readMenuChoice(sc, "PLEASE SELECT (1-6): ", "1", "2", "3", "4", "5", "6");

        if (sel.equals("1")) {
            List<Product> products = admin.viewProducts(system);

            System.out.println("================================================================================");
            System.out.println("ALL PRODUCTS INVENTORY");
            System.out.println("================================================================================");
            System.out.printf("%-6s %-32s %-20s %-12s %-10s%n",
                    "ID", "PRODUCT NAME", "CATEGORY", "PRICE", "STOCK");
            System.out.println("--------------------------------------------------------------------------------");

            if (products.isEmpty()) {
                System.out.println("No product records found.");
            } else {
                for (Product p : products) {
                    String stockText = p.isOutOfStock() ? "0 [OUT]" : String.valueOf(p.getStock());

                    System.out.printf("%-6s %-32s %-20s RM %-9.2f %-10s%n",
                            p.getProductId(),
                            truncate(p.getName(), 30),
                            truncate(p.getCategory(), 18),
                            p.getPrice(),
                            stockText);
                }
            }

            System.out.println("--------------------------------------------------------------------------------");
            pressEnter(sc);

        } else if (sel.equals("2")) {
            System.out.println("-----------------------------------------");
            System.out.println("ADD NEW PRODUCT");
            System.out.println("-----------------------------------------");

           String name = readProductName(sc, "ENTER PRODUCT NAME: ");

        String catChoice = readMenuChoice(
        sc,
        "ENTER CATEGORY (1-Equipment / 2-Supplement): ",
        "1", "2"
        );
         String category = catChoice.equals("1")
        ? Product.FITNESS_EQUIPMENT
        : Product.SUPPLEMENTS;

            String brand = readProductBrand(sc, "ENTER BRAND: ");
            double price = readStrictProductPrice(sc, "ENTER PRICE (RM): ");
            int stock = readProductStock(sc, "ENTER INITIAL STOCK: ");
            Product np = new Product("", name, brand, category, price, stock);

            System.out.println("-----------------------------------------");
            System.out.println("PRODUCT SUMMARY");
            System.out.println("-----------------------------------------");
            System.out.println("NAME     : " + name);
            System.out.println("CATEGORY : " + category);
            System.out.println("BRAND    : " + brand);
            System.out.printf("PRICE    : RM %.2f%n", price);
            System.out.println("STOCK    : " + stock);
            System.out.println("-----------------------------------------");

            String save = readYesNo(sc, "SAVE THIS PRODUCT? (Y/N): ");
            if (save.equals("Y")) {
                admin.addProduct(system, np);
                System.out.println("SUCCESS: Product [" + name + "] has been added.");
                system.logAudit("ADD_PRODUCT", admin.getAccountId(), name, "Product added");
            } else {
                System.out.println("ADD PRODUCT CANCELLED.");
            }

            pressEnter(sc);

        } else if (sel.equals("3")) {
    List<Product> products = admin.viewProducts(system);

    if (products.isEmpty()) {
        System.out.println("[!] No product available to edit.");
        pressEnter(sc);
        continue;
    }

    while (true) {
        System.out.println("================================================================================");
        System.out.println("EDIT PRODUCT");
        System.out.println("================================================================================");
        System.out.printf("%-6s %-32s %-20s %-12s %-10s%n",
                "ID", "PRODUCT NAME", "CATEGORY", "PRICE", "STOCK");
        System.out.println("--------------------------------------------------------------------------------");

        for (Product p : products) {
            String stockText = p.isOutOfStock() ? "0 [OUT]" : String.valueOf(p.getStock());

            System.out.printf("%-6s %-32s %-20s RM %-9.2f %-10s%n",
                    p.getProductId(),
                    truncate(p.getName(), 30),
                    truncate(p.getCategory(), 18),
                    p.getPrice(),
                    stockText);
        }

        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("1. EDIT PRICE");
        System.out.println("2. EDIT STOCK");
        System.out.println("3. BACK");

        String editChoice = readMenuChoice(sc, "PLEASE SELECT (1-3): ", "1", "2", "3");

        if (editChoice.equals("3")) {
            break;
        }

        String pid = readExistingProductId(sc, products, "ENTER PRODUCT ID TO EDIT: ");
        Product target = findProductById(products, pid);

        if (editChoice.equals("1")) {
            double newPrice = readProductPrice(sc, "ENTER NEW PRICE (RM): ");

            System.out.println("-----------------------------------------");
            System.out.println("PRICE UPDATE SUMMARY");
            System.out.println("-----------------------------------------");
            System.out.println("PRODUCT ID   : " + target.getProductId());
            System.out.println("PRODUCT NAME : " + target.getName());
            System.out.printf("OLD PRICE    : RM %.2f%n", target.getPrice());
            System.out.printf("NEW PRICE    : RM %.2f%n", newPrice);
            System.out.println("-----------------------------------------");

            String confirm = readYesNo(sc, "SAVE CHANGES? (Y/N): ");
            if (confirm.equals("Y")) {
                boolean ok = system.editProduct(pid, target.getName(), newPrice, target.getStock());
                if (ok) {
                    System.out.println("SUCCESS: Product price updated successfully.");
                    system.logAudit("EDIT_PRODUCT_PRICE", admin.getAccountId(), pid,
                            "Updated price to RM " + String.format("%.2f", newPrice));
                    products = admin.viewProducts(system); // refresh latest data
                } else {
                    System.out.println("[!] Product not found.");
                }
            } else {
                System.out.println("PRICE UPDATE CANCELLED.");
            }

            pressEnter(sc);
        } else if (editChoice.equals("2")) {
            int newStock = readInt(sc, "ENTER NEW STOCK: ", 0, 9999);

            System.out.println("-----------------------------------------");
            System.out.println("STOCK UPDATE SUMMARY");
            System.out.println("-----------------------------------------");
            System.out.println("PRODUCT ID   : " + target.getProductId());
            System.out.println("PRODUCT NAME : " + target.getName());
            System.out.println("OLD STOCK    : " + target.getStock());
            System.out.println("NEW STOCK    : " + newStock);
            System.out.println("-----------------------------------------");

            String confirm = readYesNo(sc, "SAVE CHANGES? (Y/N): ");
            if (confirm.equals("Y")) {
                boolean ok = system.editProduct(pid, target.getName(), target.getPrice(), newStock);
                if (ok) {
                    System.out.println("SUCCESS: Product stock updated successfully.");
                    system.logAudit("EDIT_PRODUCT_STOCK", admin.getAccountId(), pid,
                            "Updated stock to " + newStock);
                    products = admin.viewProducts(system); // refresh latest data
                } else {
                    System.out.println("[!] Product not found.");
                }
            } else {
                System.out.println("STOCK UPDATE CANCELLED.");
            }

            pressEnter(sc);
        }
    }


        } else if (sel.equals("4")) {
            List<Product> products = admin.viewProducts(system);

            if (products.isEmpty()) {
                System.out.println("[!] No product available to delete.");
                pressEnter(sc);
                continue;
            }

            String pid = readExistingProductId(sc, products, "ENTER PRODUCT ID TO DELETE: ");
            Product target = findProductById(products, pid);

            System.out.println("-----------------------------------------");
            System.out.println("DELETE PRODUCT CONFIRMATION");
            System.out.println("-----------------------------------------");
            System.out.println("ID       : " + target.getProductId());
            System.out.println("NAME     : " + target.getName());
            System.out.println("CATEGORY : " + target.getCategory());
            System.out.println("BRAND    : " + target.getBrand());
            System.out.printf("PRICE    : RM %.2f%n", target.getPrice());
            System.out.println("STOCK    : " + target.getStock());
            System.out.println("-----------------------------------------");

            String confirm = readYesNo(sc, "ARE YOU SURE YOU WANT TO DELETE THIS PRODUCT? (Y/N): ");
            if (confirm.equals("Y")) {
                if (admin.deleteProduct(system, pid)) {
                    System.out.println("SUCCESS: Product [" + pid + "] removed from shop.");
                    system.logAudit("DELETE_PRODUCT", admin.getAccountId(), pid, "Product deleted");
                } else {
                    System.out.println("[!] Product not found.");
                }
            } else {
                System.out.println("DELETE PRODUCT CANCELLED.");
            }

            pressEnter(sc);

        } else if (sel.equals("5")) {
    List<Order> orders = system.getAllOrders();
	//view all member order history
    System.out.println("================================================================================");
    System.out.println("ALL MEMBER ORDERS");
    System.out.println("================================================================================");
    System.out.printf("%-4s %-15s %-12s %-22s %-14s %-12s%n",
            "NO", "ORDER ID", "DATE", "MEMBER", "TOTAL", "STATUS");
    System.out.println("--------------------------------------------------------------------------------");

    if (orders.isEmpty()) {
        System.out.println("No order records found.");
        System.out.println("--------------------------------------------------------------------------------");
        pressEnter(sc);
        continue;
    }

    int i = 1;
    for (Order o : orders) {
        System.out.printf("%-4d %-15s %-12s %-22s RM %-11.2f %-12s%n",
                i++,
                o.getOrderId(),
                o.getDate(),
                truncate(o.getMemberName(), 20),
                o.getTotalPaid(),
                o.getStatus());
    }
    System.out.println("--------------------------------------------------------------------------------");

    String updateChoice = readYesNo(sc, "DO YOU WANT TO UPDATE ORDER STATUS? (Y/N): ");
    if (updateChoice.equals("N")) {
        pressEnter(sc);
        continue;
    }

    String oid;
Order targetOrder = null;

while (true) {
    oid = readNonEmpty(sc, "ENTER ORDER ID TO UPDATE: ");

    targetOrder = null;
    for (Order o : orders) {
        if (o.getOrderId().equalsIgnoreCase(oid)) {
            targetOrder = o;
            break;
        }
    }

    if (targetOrder != null) {
        break;
    }

    System.out.println("[!] Order ID not found. Please enter an existing Order ID.");
}

if (Order.COMPLETED.equalsIgnoreCase(targetOrder.getStatus()) ||
    Order.CANCELLED.equalsIgnoreCase(targetOrder.getStatus())) {
    System.out.println("[!] This order is already in final status [" 
            + targetOrder.getStatus() + "] and cannot be changed anymore.");
    pressEnter(sc);
    continue;
}

System.out.println("1. COMPLETED");
System.out.println("2. CANCELLED (REFUND)");
System.out.println("3. BACK");

    String choice = readMenuChoice(sc, "PLEASE SELECT (1-3): ", "1", "2", "3");

    if (choice.equals("3")) {
        pressEnter(sc);
        continue;
    }

    String newStatus = choice.equals("1") ? Order.COMPLETED : Order.CANCELLED;

    String confirm = readYesNo(sc, "CONFIRM STATUS UPDATE? (Y/N): ");
    if (confirm.equals("Y")) {
        if (admin.updateOrderStatus(system, oid, newStatus)) {
            System.out.println("SUCCESS: Order [" + oid + "] updated to [" + newStatus + "].");
            system.logAudit("UPDATE_ORDER_STATUS", admin.getAccountId(), oid,
                    "Status changed to " + newStatus);
        } else {
            System.out.println("[!] Order not found.");
        }
    } else {
        System.out.println("ORDER STATUS UPDATE CANCELLED.");
    }

    pressEnter(sc);
} else {
            return;
        }
    } 
}

    // ================================================================
    // SHARED HELPERS
    // ================================================================

    private static String selectPaymentMethod(Scanner sc) {
    while (true) {
        System.out.println("SELECT PAYMENT METHOD:");
        System.out.println("1. E-WALLET");
        System.out.println("2. CASH");
        System.out.print("PLEASE SELECT (1-2): ");
        String sel = sc.nextLine().trim();

        if (sel.equals("1")) return Order.E_WALLET;
        if (sel.equals("2")) return Order.CASH;

        System.out.println("[!] Invalid selection. Please enter 1 or 2.");
    }
}
        private static String readDate(Scanner sc, String prompt) {
        while (true) {
        System.out.print(prompt);
        String input = sc.nextLine().trim();

        if (input.isEmpty()) {
            System.out.println("[!] Date cannot be empty.");
            continue;
        }

        try {
            LocalDate date = LocalDate.parse(input, DATE_FMT);
            return date.format(DATE_FMT);
        } catch (Exception e) {
            System.out.println("[!] Invalid date format. Please use DD-MM-YYYY.");
        }
    }
}

         private static void pressEnter(Scanner sc) {
         while (true) {
        System.out.print("PRESS ENTER TO CONTINUE...");
        String input = sc.nextLine();
        if (input.isEmpty()) {
            return;
        }
        System.out.println("[!] Please press ENTER only.");
    }
}
          
        private static String readMenuChoice(Scanner sc, String prompt, String... validChoices) {
        while (true) {
        System.out.print(prompt);
        String input = sc.nextLine().trim();

        for (String choice : validChoices) {
            if (choice.equals(input)) {
                return input;
            }
        }

        System.out.print("[!] Invalid selection. Please enter ");
        for (int i = 0; i < validChoices.length; i++) {
            if (i > 0 && i == validChoices.length - 1) {
                System.out.print(" or ");
            } else if (i > 0) {
                System.out.print(", ");
            }
            System.out.print(validChoices[i]);
        }
        System.out.println(".");
    }
}      
       private static boolean hasEquipmentTimeConflict(Member m, String date, String startTime, String endTime) {
        for (EquipmentBooking eb : m.getEquipmentBookings()) {
        if (EquipmentBooking.CANCELLED.equalsIgnoreCase(eb.getStatus())) {
            continue;
        }

        boolean sameDate = eb.getDate().equals(date);
        boolean sameStart = eb.getStartTime().equals(startTime);
        boolean sameEnd = eb.getEndTime().equals(endTime);

        if (sameDate && sameStart && sameEnd) {
            return true;
        }
    }
    return false;
}

      private static boolean hasActiveEquipmentBookingNo(List<EquipmentBooking> bookings, int bookingNo) {
      for (EquipmentBooking eb : bookings) {
        if (eb.getBookingNo() == bookingNo &&
            !EquipmentBooking.CANCELLED.equalsIgnoreCase(eb.getStatus())) {
            return true;
        }
    }
    return false;
}

       private static boolean hasActiveClassBookingNo(List<ClassBooking> bookings, int bookingNo) {
       for (ClassBooking cb : bookings) {
        if (cb.getBookingNo() == bookingNo &&
            !ClassBooking.CANCELLED.equalsIgnoreCase(cb.getStatus())) {
            return true;
        }
    }
    return false;
}

       private static int readBookingYear(Scanner sc) {
       int currentYear = LocalDate.now().getYear();
        return readInt(sc, "PLEASE ENTER YEAR (" + currentYear + "-" + (currentYear + 1) + "): ",
            currentYear, currentYear + 1);
       }

    private static int readBookingMonth(Scanner sc) {
    return readInt(sc, "PLEASE ENTER MONTH (1-12): ", 1, 12);
    }

     private static int readWeekOfMonth(Scanner sc) {
    return readInt(sc, "PLEASE ENTER WEEK OF THE MONTH (1-5): ", 1, 5);
     }
     
     private static boolean hasClassConflict(Member m, ClassSession cs) {
    for (ClassBooking cb : m.getClassBookings()) {
        if (ClassBooking.CANCELLED.equalsIgnoreCase(cb.getStatus())) {
            continue;
        }

        boolean sameDay = cb.getDay().equalsIgnoreCase(cs.getDay());
        boolean sameTime = cb.getTime().equalsIgnoreCase(cs.getTime());

        if (sameDay && sameTime) {
            return true;
        }
    }
    return false;
}
       private static void refreshEquipmentBookingStatuses(Member m) {
       LocalDate today = LocalDate.now();

    for (EquipmentBooking eb : m.getEquipmentBookings()) {
        if (EquipmentBooking.CANCELLED.equalsIgnoreCase(eb.getStatus())) {
            continue;
        }

        try {
            LocalDate bookingDate = LocalDate.parse(eb.getDate(), DATE_FMT);

            if (bookingDate.isBefore(today)) {
                eb.setStatus("COMPLETED");
            } else {
                eb.setStatus("UPCOMING");
            }
        } catch (Exception e) {
            // ignore invalid saved date
        }
    }
}      

         private static LocalDate[] getWeekRange(int year, int month, int weekOfMonth) {
         YearMonth ym = YearMonth.of(year, month);
         LocalDate firstDayOfMonth = ym.atDay(1);

         LocalDate firstMonday = firstDayOfMonth.with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));
         LocalDate weekStart = firstMonday.plusWeeks(weekOfMonth - 1);
         LocalDate weekEnd = weekStart.plusDays(6);

         return new LocalDate[]{weekStart, weekEnd};
}
	//upcoming equipment booking
    private static boolean isUpcomingEquipmentBooking(EquipmentBooking eb) {
    return "UPCOMING".equalsIgnoreCase(eb.getStatus());
     }
     private static LocalDate getClassDateFromDay(String day, LocalDate weekStart) {
    switch (day.toUpperCase()) {
        case "MON": return weekStart;
        case "TUE": return weekStart.plusDays(1);
        case "WED": return weekStart.plusDays(2);
        case "THU": return weekStart.plusDays(3);
        case "FRI": return weekStart.plusDays(4);
        case "SAT": return weekStart.plusDays(5);
        case "SUN": return weekStart.plusDays(6);
        default:    return weekStart;
    }
}

    private static boolean hasTrainerTimeConflict(Member m, String date, String startTime, String endTime) {
    List<TrainerAppointment> appts = m.getAppointments();

    for (TrainerAppointment ta : appts) {
        if (TrainerAppointment.CANCELLED.equalsIgnoreCase(ta.getStatus())) {
            continue;
        }

        if (!ta.getDate().equals(date)) {
            continue;
        }

        String existingStart = ta.getStartTime();
        String existingEnd   = addTwoHours(existingStart);

        if (isTimeOverlap(startTime, endTime, existingStart, existingEnd)) {
            return true;
        }
    }

    return false;
}
	// time overlap
    private static boolean isTimeOverlap(String start1, String end1, String start2, String end2) {
    int s1 = to24HourValue(start1);
    int e1 = to24HourValue(end1);
    int s2 = to24HourValue(start2);
    int e2 = to24HourValue(end2);

    return s1 < e2 && s2 < e1;
    }
    private static int to24HourValue(String time) {
    String t = time.trim().toUpperCase();

    if (t.contains(":")) {
        String hourPart = t.split(":")[0].trim();
        int hour = Integer.parseInt(hourPart);

        if (hour >= 1 && hour <= 8) {
            hour += 12;
        }
        return hour;
    }

    int hour = Integer.parseInt(t);
    if (hour >= 1 && hour <= 8) {
        hour += 12;
    }
    return hour;
    }
    //print audit table
    private static void printAuditTable(List<String> logs) {
    System.out.printf("%-4s %-19s %-22s %-12s %-15s %-40s%n",
            "NO", "TIMESTAMP", "ACTION", "ACTOR", "TARGET", "DETAIL");
    System.out.println("-----------------------------------------------------------------------------------------------");

    int no = 1;

    for (String raw : logs) {
        if (raw == null) continue;

        String line = raw.replace("\r", "").trim();
        if (line.isEmpty()) continue;

        String[] parts = line.split("\\|");

        if (parts.length < 4) {
            // bad line, skip instead of printing weird blank row
            continue;
        }

        String time   = parts[0].trim();
        String action = "";
        String actor  = "";
        String target = "";
        String detail = "";

        for (int i = 1; i < parts.length; i++) {
            String part = parts[i].trim();

            if (part.startsWith("ACTION=")) {
                action = part.substring("ACTION=".length()).trim();
            } else if (part.startsWith("ACTOR=")) {
                actor = part.substring("ACTOR=".length()).trim();
            } else if (part.startsWith("TARGET=")) {
                target = part.substring("TARGET=".length()).trim();
            } else if (part.startsWith("DETAIL=")) {
                detail = part.substring("DETAIL=".length()).trim();
            }
        }

        if (detail.length() > 40) {
            detail = detail.substring(0, 37) + "...";
        }

        System.out.printf("%-4d %-19s %-22s %-12s %-15s %-40s%n",
                no++, time, action, actor, target, detail);
    }
}
     

     

     private static String truncate(String text, int maxLen) {
    if (text == null) return "";
    if (text.length() <= maxLen) return text;
    return text.substring(0, maxLen - 3) + "...";
}

    private static Product findProductById(List<Product> products, String pid) {
    for (Product p : products) {
        if (p.getProductId().equalsIgnoreCase(pid)) {
            return p;
        }
    }
    return null;
}

    private static String readExistingProductId(Scanner sc, List<Product> products, String prompt) {
    while (true) {
        String pid = readNonEmpty(sc, prompt);

        if (findProductById(products, pid) != null) {
            return pid.toUpperCase();
        }

        System.out.println("[!] Product ID not found. Please enter an existing Product ID.");
    }
}
    // ================================================================
    // INPUT VALIDATION HELPERS
    // ================================================================

    /** Name: letters and spaces only, 2-50 characters. */
    private static String readName(Scanner sc, String prompt) {
    while (true) {
        System.out.print(prompt);
        String s = sc.nextLine().trim();

        if (s.isEmpty()) {
            System.out.println("[!] Name cannot be empty.");
            continue;
        }
        if (s.length() < 2 || s.length() > 50) {
            System.out.println("[!] Name must be 2-50 characters.");
            continue;
        }
        if (!s.matches("[a-zA-Z ]+")) {
            System.out.println("[!] Name must contain letters and spaces only.");
            continue;
        }

        //  check duplicate name in file
        if (isNameExists(s)) {
            System.out.println("[!] This name already is used. Please enter another name.");
            continue;
        }

        return s;
    }
}
//ckheck name exists
private static boolean isNameExists(String name) {
    try (BufferedReader br = new BufferedReader(new FileReader("DATA/member.txt"))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] p = line.split(",", -1);
            if (p.length > 3) {
                String fileName = p[3].trim();
                if (fileName.equalsIgnoreCase(name.trim())) {
                    return true;
                }
            }
        }
    } catch (Exception e) {
        System.out.println("[!] Error reading file.");
    }
    return false;
}
    /** Age: integer 12-70. */
   private static int readAge(Scanner sc, String prompt) {
    while (true) {
        System.out.print(prompt);
        String s = sc.nextLine().trim();

        if (s.isEmpty()) {
            System.out.println("[!] Age cannot be empty.");
            continue;
        }

        try {
            int age = Integer.parseInt(s);

            // validation for age 
            if (age < 12 || age > 70) {
                System.out.println("[!] Age must be between 12 and 70.");
                continue;
            }

            return age;

        } catch (NumberFormatException e) {
            System.out.println("[!] Age must be a number.");
        }
    }
}

    /**
     * Phone: accepts Malaysian formats.
     * Pure digits 10-11, OR format like 012-3456789 (3 digits, dash, 7-8 digits).
     */
    private static String readPhone(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim();
            if (s.isEmpty()) { System.out.println("[!] Phone cannot be empty."); continue; }
            // Accept digits only: 10-11 digits
            if (s.matches("\\d{10,11}")) return s;
            // Accept Malaysian format: 0XX-XXXXXXX or 0XX-XXXXXXXX
            if (s.matches("0\\d{2}-\\d{7,8}")) return s;
            System.out.println("[!] Enter a valid phone number (e.g. 0123456789 or 012-3456789).");
        }
    }

    /** Gender: M or F (case-insensitive). */
    private static String readGender(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim().toUpperCase();
            if (s.equals("MALE"))   return "M";
            if (s.equals("FEMALE")) return "F";
            if (s.equals("M") || s.equals("F")) return s;
            System.out.println("[!] Gender must be M, F, MALE, or FEMALE.");
        }
    }

    /**
     * Password rules:
     *   - 6-20 characters
     *   - At least 1 uppercase, 1 lowercase, 1 digit, 1 special (!@#$%&*)
     */
   private static String readPassword(Scanner sc, String prompt) {

    //  show rules password rules
    System.out.println("Password Requirements:");
    System.out.println(" - 6 to 20 characters");
    System.out.println(" - Must contain UPPERCASE and lowercase letters");
    System.out.println(" - Must contain a digit");
    System.out.println(" - Must contain special character (!@#$%&*)");

    while (true) {
        System.out.print(prompt);
        String pwd = sc.nextLine().trim();

        if (pwd.length() < 6 || pwd.length() > 20) {
            System.out.println("[!] Password must be 6-20 characters.");
            continue;
        }
        if (!pwd.matches(".*[A-Z].*")) {
            System.out.println("[!] Must contain uppercase letter.");
            continue;
        }
        if (!pwd.matches(".*[a-z].*")) {
            System.out.println("[!] Must contain lowercase letter.");
            continue;
        }
        if (!pwd.matches(".*\\d.*")) {
            System.out.println("[!] Must contain a digit.");
            continue;
        }
        if (!pwd.matches(".*[!@#$%&*].*")) {
            System.out.println("[!] Must contain special character.");
            continue;
        }

        return pwd;
    }
}

    private static String readSpecialty(Scanner sc) {
        while (true) {
            System.out.print("ENTER SPECIALIZATION: ");
            String s = sc.nextLine().trim();
            if (s.isEmpty())                       { System.out.println("[!] Specialization cannot be empty."); continue; }
            if (s.length() < 2 || s.length() > 50) { System.out.println("[!] Must be 2-50 characters."); continue; }
            return s;
        }
    }

    private static int readExperience(Scanner sc, int age) {
        while (true) {
            System.out.print("PLEASE ENTER YEARS OF EXPERIENCE: ");
            String s = sc.nextLine().trim();
            if (s.isEmpty()) { System.out.println("[!] Experience cannot be empty."); continue; }
            try {
                int exp = Integer.parseInt(s);
                if (exp < 0) { System.out.println("[!] Experience cannot be negative."); continue; }
                int max = Math.max(0, age - 18);
                if (exp > max) {
                    System.out.println("[!] A trainer aged " + age
                            + " cannot have more than " + max + " year(s) experience.");
                    continue;
                }
                return exp;
            } catch (NumberFormatException e) {
                System.out.println("[!] Please enter a valid number.");
            }
        }
    }

    private static int readInt(Scanner sc, String prompt, int min, int max) {
        while (true) {
            if (!prompt.isEmpty()) System.out.print(prompt);
            String s = sc.nextLine().trim();
            if (s.isEmpty()) { System.out.println("[!] Input cannot be empty."); continue; }
            try {
                int v = Integer.parseInt(s);
                if (v < min || v > max) {
                    System.out.println("[!] Please enter a number between " + min + " and " + max + ".");
                    continue;
                }
                return v;
            } catch (NumberFormatException e) {
                System.out.println("[!] Please enter a valid number.");
            }
        }
    }


    private static double readRating(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim();
            try {
                double v = Double.parseDouble(s);
                if (v < 0.0 || v > 5.0) {
                    System.out.println("[!] Rating must be between 0.0 and 5.0.");
                    continue;
                }
                return v;
            } catch (NumberFormatException e) {
                System.out.println("[!] Please enter a valid number.");
            }
        }
    }

    private static String addTwoHours(String startTime) {
        try {
            String cleaned = startTime.replace("AM","").replace("PM","").trim();
            String[] parts = cleaned.split(":");
            int hour = Integer.parseInt(parts[0].trim()) + 2;
            int min  = parts.length > 1 ? Integer.parseInt(parts[1].trim()) : 0;
            return String.format("%02d:%02d", hour, min);
        } catch (Exception e) {
            return startTime;
        }
    }
    private static String readFutureOrTodayDate(Scanner sc, String prompt) {
    while (true) {
        System.out.print(prompt);
        String input = sc.nextLine().trim();

        if (input.isEmpty()) {
            System.out.println("[!] Date cannot be empty.");
            continue;
        }

        try {
            LocalDate date = LocalDate.parse(input, DATE_FMT);
            LocalDate today = LocalDate.now();
            LocalDate maxDate = today.plusYears(1);

            if (date.isBefore(today)) {
                System.out.println("[!] Booking date cannot be in the past.");
                continue;
            }

            if (date.isAfter(maxDate)) {
                System.out.println("[!] Booking date cannot be more than 1 year from today.");
                continue;
            }

            return date.format(DATE_FMT);
        } catch (Exception e) {
            System.out.println("[!] Invalid date format. Please use DD-MM-YYYY.");
        }
    }
}
     private static String readStartTime(Scanner sc, String prompt) {
    while (true) {
        System.out.print(prompt);
        String input = sc.nextLine().trim().toLowerCase();

        if (input.isEmpty()) {
            System.out.println("[!] Time cannot be empty.");
            continue;
        }

        int hour = -1;

        try {
            // Case 1: user enters only hour, e.g. "1" or "14"
            if (input.matches("\\d+")) {
                hour = Integer.parseInt(input);
            }
            // Case 2: user enters hour with minutes, e.g. "1:00" or "14:00"
            else if (input.matches("\\d{1,2}:\\d{2}")) {
                String[] parts = input.split(":");
                hour = Integer.parseInt(parts[0]);
                int minute = Integer.parseInt(parts[1]);

                if (minute != 0) {
                    System.out.println("[!] Minutes must be 00 only. Example: 1:00 or 14:00");
                    continue;
                }
            } else {
                System.out.println("[!] Invalid format. Example: 1, 1:00, 13, 13:00");
                continue;
            }

            // Convert simple 1-8 input into afternoon hours (13:00-20:00)
            if (hour >= 1 && hour <= 8) {
                hour += 12;
            }

            // Allow booking time from 9:00 to 20:00 only
            if (hour < 9 || hour > 18) {
            System.out.println("[!] Last allowed booking start time is 18:00 because each session is fixed for 2 hours.");
            continue;
            }
            return String.format("%02d:00", hour);

        } catch (Exception e) {
            System.out.println("[!] Invalid input.");
        }
    }
}
private static double readWeightKg(Scanner sc, String prompt) {
    while (true) {
        System.out.print(prompt);
        String input = sc.nextLine().trim();

        if (!input.matches("^\\d+(\\.\\d{1,2})?$")) {
            System.out.println("[!] Invalid format. Please enter weight using numbers only, with at most 2 decimal places.");
            continue;
        }

        double value = Double.parseDouble(input);
        if (value < 20 || value > 600) {
            System.out.println("[!] Weight must be between 20.00 and 600.00 KG.");
            continue;
        }

        return value;
    }
}

private static double readHeightCm(Scanner sc, String prompt) {
    while (true) {
        System.out.print(prompt);
        String input = sc.nextLine().trim();

        if (!input.matches("^\\d+(\\.\\d{1,2})?$")) {
            System.out.println("[!] Invalid format. Please enter height using numbers only, with at most 2 decimal places.");
            continue;
        }

        double value = Double.parseDouble(input);
        if (value < 80 || value > 220) {
            System.out.println("[!] Height must be between 80.00 and 220.00 CM.");
            continue;
        }

        return value;
    }
}
         private static int readTrainerAge(Scanner sc, String prompt) {
        while (true) {
        System.out.print(prompt);
        String s = sc.nextLine().trim();

        if (s.isEmpty()) {
            System.out.println("[!] Age cannot be empty.");
            continue;
        }

        try {
            int age = Integer.parseInt(s);
            if (age < 18 || age > 70) {
                System.out.println("[!] Trainer age must be between 18 and 70.");
                continue;
            }
            return age;
        } catch (NumberFormatException e) {
            System.out.println("[!] Age must be a number.");
        }
    }
}
    private static String readAuditDate(Scanner sc, String prompt) {
    while (true) {
        System.out.print(prompt);
        String input = sc.nextLine().trim();

        if (input.isEmpty()) {
            System.out.println("[!] Date cannot be empty.");
            continue;
        }

        try {
            LocalDate date = LocalDate.parse(input); // YYYY-MM-DD
            return date.toString();
        } catch (Exception e) {
            System.out.println("[!] Invalid date format. Please use YYYY-MM-DD.");
        }
    }
}
     private static String readExistingAuditDate(Scanner sc, FitnessClubSystem system, String prompt) {
    while (true) {
        String input = readAuditDate(sc, prompt);
        List<String> filtered = system.filterAuditTrail(input);

        if (filtered.isEmpty()) {
            System.out.println("[!] No audit record found for this date. Please enter another date.");
            continue;
        }

        return input;
    }
}
     private static String readNonEmpty(Scanner sc, String prompt) {
        while (true) {
        System.out.print(prompt);
        String input = sc.nextLine().trim();

        if (input.isEmpty()) {
            System.out.println("[!] Input cannot be empty.");
            continue;
        }

        return input;
    }
}
     private static String readEquipmentId(Scanner sc, String prompt) {
    while (true) {
        System.out.print(prompt);
        String input = sc.nextLine().trim().toUpperCase();

        if (input.isEmpty()) {
            System.out.println("[!] Equipment ID cannot be empty.");
            continue;
        }

        if (!input.matches("E\\d{3}")) {
            System.out.println("[!] Invalid Equipment ID format. Example: E001");
            continue;
        }

        return input;
    }
     }
     private static String readTrainerBookingDate(Scanner sc, String prompt) {
      while (true) {
        System.out.print(prompt);
        String input = sc.nextLine().trim();

        try {
            LocalDate enteredDate = LocalDate.parse(input, DATE_FMT);
            LocalDate today = LocalDate.now();
            LocalDate maxDate = today.plusMonths(3);

            if (enteredDate.isBefore(today)) {
                System.out.println("[!] You cannot book a trainer before today's date.");
                continue;
            }

            if (enteredDate.isAfter(maxDate)) {
                System.out.println("[!] Trainer booking must be within 3 months from today only.");
                continue;
            }

            return input;

        } catch (Exception e) {
            System.out.println("[!] Invalid date format. Please use DD-MM-YYYY.");
        }
    }
}
        
    private static void printHealthProgressTable(Member m) {
    HealthProfile hp = m.getHealthProfile();
    if (hp == null) {
        System.out.println("No health profile set up.");
        return;
    }

    List<String[]> rows = hp.getParsedHistory();
    String[] initial = null;

    for (String[] row : rows) {
        if (row.length == 5 && row[4].equalsIgnoreCase("Initial")) {
            initial = row;
            break;
        }
    }

	//view member initail and current bmi
    System.out.println("=====================================================================");
    System.out.println("INITIAL VS CURRENT");
    System.out.println("=====================================================================");

    if (initial != null) {
    String initialStatus = getBMIStatusFromValue(Double.parseDouble(initial[3]));

    System.out.printf("%-18s %-18s %-18s%n", "", "INITIAL", "CURRENT");
    System.out.printf("%-18s %-18s %-18s%n", "WEIGHT (KG)", initial[1], String.format("%.1f", hp.getCurrentWeightKg()));
    System.out.printf("%-18s %-18s %-18s%n", "HEIGHT (CM)", initial[2], String.format("%.1f", hp.getHeightCm()));
    System.out.printf("%-18s %-18s %-18s%n", "BMI", initial[3], String.format("%.2f", hp.getBMI()));
    System.out.printf("%-18s %-18s %-18s%n", "TARGET (KG)", String.format("%.1f", hp.getTargetWeightKg()), String.format("%.1f", hp.getTargetWeightKg()));
    System.out.printf("%-18s %-18s %-18s%n", "STATUS", initialStatus, hp.getBMIStatus());
    } else {
    System.out.printf("%-18s %-18s%n", "CURRENT WEIGHT", hp.getCurrentWeightKg() + " KG");
    System.out.printf("%-18s %-18s%n", "CURRENT HEIGHT", hp.getHeightCm() + " CM");
    System.out.printf("%-18s %-18.2f%n", "CURRENT BMI", hp.getBMI());
    System.out.println();
    System.out.printf("%-18s %s%n", "CURRENT TARGET", String.format("%.1f KG", hp.getTargetWeightKg()));
    System.out.printf("%-18s %s%n", "CURRENT STATUS", hp.getBMIStatus());
    }

    System.out.println("=====================================================================");
    System.out.println("HEALTH HISTORY");
    System.out.println("=====================================================================");
    System.out.printf("%-4s %-12s %-14s %-14s %-10s %-18s%n",
            "NO", "DATE", "NEW WEIGHT", "NEW HEIGHT", "NEW BMI", "STATUS");
    System.out.println("---------------------------------------------------------------------");

    if (rows.isEmpty()) {
        System.out.println("No history found.");
        return;
    }

    int no = 1;
    for (String[] row : rows) {
        String date = row[0];
        String weight = row[1] + " KG";
        String height = row[2] + " CM";
        String bmi = row[3];
        String status = getBMIStatusFromValue(Double.parseDouble(row[3]));

        System.out.printf("%-4d %-12s %-14s %-14s %-10s %-18s%n",
                no++, date, weight, height, bmi, status);
    }
}
    private static String getBMIStatusFromValue(double bmi) {
    if (bmi < 18.5) return "UNDERWEIGHT";
    if (bmi < 25.0) return "NORMAL WEIGHT";
    if (bmi < 30.0) return "OVERWEIGHT";
    return "OBESE";
}  
    private static double readProductPrice(Scanner sc, String prompt) {
    while (true) {
        System.out.print(prompt);
        String input = sc.nextLine().trim();

        try {
            double price = Double.parseDouble(input);

            if (price < 10.0 || price > 5000.0) {
                System.out.println("[!] Product price must be between RM 10.00 and RM 5000.00.");
                continue;
            }

            return price;

        } catch (NumberFormatException e) {
            System.out.println("[!] Please enter a valid number.");
        }
    }
}
    private static String readTextLength(Scanner sc, String prompt, int min, int max) {
    while (true) {
        System.out.print(prompt);
        String input = sc.nextLine().trim();

        if (input.isEmpty()) {
            System.out.println("[!] Input cannot be empty.");
            continue;
        }

        if (input.length() < min || input.length() > max) {
            System.out.println("[!] Input must be between " + min + " and " + max + " characters.");
            continue;
        }

        if (!input.matches("[A-Za-z0-9&\\- ]+")) {
            System.out.println("[!] Only letters, numbers, spaces, '&' and '-' are allowed.");
            continue;
        }

        return input;
    }
}           
            private static String readProductName(Scanner sc, String prompt) {
    while (true) {
        System.out.print(prompt);
        String input = sc.nextLine().trim();

        if (input.isEmpty()) {
            System.out.println("[!] Product name cannot be empty.");
            continue;
        }

        if (input.length() < 2 || input.length() > 30) {
            System.out.println("[!] Product name must be between 2 and 30 characters.");
            continue;
        }

        return input;
    }
}

private static String readProductBrand(Scanner sc, String prompt) {
    while (true) {
        System.out.print(prompt);
        String input = sc.nextLine().trim();

        if (input.isEmpty()) {
            System.out.println("[!] Brand cannot be empty.");
            continue;
        }

        if (input.length() < 2 || input.length() > 15) {
            System.out.println("[!] Brand must be between 2 and 15 characters.");
            continue;
        }

        return input;
    }
}

private static double readStrictProductPrice(Scanner sc, String prompt) {
    while (true) {
        System.out.print(prompt);
        String input = sc.nextLine().trim();

        if (input.isEmpty()) {
            System.out.println("[!] Price cannot be empty.");
            continue;
        }

        // only digits, optional one dot, max 2 digits after dot
        if (!input.matches("\\d+(\\.\\d{1,2})?")) {
            System.out.println("[!] Invalid price. Use numbers only, with at most 1 decimal point and 2 decimal places.");
            continue;
        }

        try {
            double price = Double.parseDouble(input);

            if (price < 10.00 || price > 5000.00) {
                System.out.println("[!] Product price must be between RM 10.00 and RM 5000.00.");
                continue;
            }

            return price;
        } catch (NumberFormatException e) {
            System.out.println("[!] Please enter a valid price.");
        }
    }
}

private static int readProductStock(Scanner sc, String prompt) {
    while (true) {
        System.out.print(prompt);
        String input = sc.nextLine().trim();

        if (input.isEmpty()) {
            System.out.println("[!] Stock cannot be empty.");
            continue;
        }

        if (!input.matches("\\d+")) {
            System.out.println("[!] Stock must contain digits only.");
            continue;
        }

        try {
            int stock = Integer.parseInt(input);

            if (stock < 10 || stock > 9999) {
                System.out.println("[!] Stock must be between 10 and 9999.");
                continue;
            }

            return stock;
        } catch (NumberFormatException e) {
            System.out.println("[!] Invalid stock value.");
        }
    }
}   
           private static boolean isEquipmentAlreadyBooked(FitnessClubSystem system,
                                                String equipmentId,
                                                String bookDate,
                                                String startTime,
                                                String endTime) {
    List<Member> members = system.getAllMembers();

    if (members == null || members.isEmpty()) {
        return false;
    }

    for (Member member : members) {
        List<EquipmentBooking> bookings = member.getEquipmentBookings();
        if (bookings == null) continue;

        for (EquipmentBooking eb : bookings) {
            if (eb == null) continue;

            if (EquipmentBooking.CANCELLED.equalsIgnoreCase(eb.getStatus())) {
                continue;
            }

            // same equipment + same date
            if (equipmentId.equalsIgnoreCase(eb.getEquipmentId())
                    && bookDate.equals(eb.getDate())) {

                if (isTimeOverlap(startTime, endTime, eb.getStartTime(), eb.getEndTime())) {
                    return true;
                }
            }
        }
    }

    return false;
}
         
}