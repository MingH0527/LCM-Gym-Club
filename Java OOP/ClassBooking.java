/**
 * - Holds booking details (class, trainer, time, location)
 * - Tracks booking status (UPCOMING, CONFIRMED, CANCELLED)
 * - Supports cancellation operation
 * - Supports file save/load (serialization)
 */
public class ClassBooking {
 
    // ========================= ATTRIBUTES =========================
    public static final String UPCOMING  = "UPCOMING";
    public static final String CONFIRMED = "CONFIRMED";
    public static final String CANCELLED = "CANCELLED";

    private int    bookingNo;
    private String status;

    // Extra display data (not core UML but needed for UI/output)
    private int    classNo;
    private String className;
    private String day;
    private String time;
    private String trainerName;
    private String location;

    // -- CONSTRUCTOR --------------------------------------------------
    // FUNCTION : Creates a new booking with default status = UPCOMING
    public ClassBooking(int bookingNo, int classNo, String className,String day, String time,String trainerName, String location) {
        this.bookingNo   = bookingNo;
        this.classNo     = classNo;
        this.className   = className;
        this.day         = day;
        this.time        = time;
        this.trainerName = trainerName;
        this.location    = location;
        this.status      = UPCOMING;
    }

    /** Constructor for loading from file. */
    // FUNCTION : Creates booking from file data (with existing status)
    public ClassBooking(int bookingNo, int classNo, String className,String day, String time,String trainerName, String location, String status) {
        this(bookingNo, classNo, className, day, time, trainerName, location);
        this.status = status;
    }


    public boolean cancel() {
        if (CANCELLED.equals(status)) return false;
        status = CANCELLED;
        return true;
    }

    // -- GETTERS ------------------------------------------------------
    public int    getBookingNo() { 
    	return bookingNo; 
    		}
    public String getStatus() { 
    	return status; 
    		}
    public int    getClassNo() { 
    	return classNo; 
    		}
    public String getClassName() {
    	 return className; 
    	 	}
    public String getDay(){ 
    	return day; 
    		}
    public String getTime(){ 
    	return time; 
    		}
    public String getTrainerName(){ 
    	return trainerName;
    	}
    public String getLocation(){
    	 return location; 
    	 	}

    // -- FILE SERIALIZATION -------------------------------------------
    //Converts booking object into a single line for file storage
    public String toFileLine() {
        return bookingNo + "," + classNo + "," + className + ","
             + day + "," + time + "," + trainerName + ","
             + location + "," + status;
    }

      // FUNCTION : Converts a file line into a ClassBooking object
     // INPUT    : line = one line from text file
     // OUTPUT   : ClassBooking object (or null if invalid)
    public static ClassBooking fromFileLine(String line) {
        String[] p = line.split(",");
        if (p.length < 8) return null;
        return new ClassBooking(
                Integer.parseInt(p[0].trim()),
                Integer.parseInt(p[1].trim()),
                p[2].trim(), p[3].trim(), p[4].trim(),
                p[5].trim(), p[6].trim(), p[7].trim());
    }
}