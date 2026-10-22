/**
 * - Stores booking details (equipment, date, time)
 * - Tracks booking status (UPCOMING, COMPLETED, CANCELLED)
 * - Supports cancellation
 * - Supports file storage (save/load)
 */
public class EquipmentBooking {

    // -- CONSTANTS ----------------------------------------------------
    // FUNCTION : Defines valid booking status values
    public static final String UPCOMING   = "UPCOMING";
    public static final String COMPLETED  = "COMPLETED";
    public static final String CANCELLED  = "CANCELLED";

    // ========================= ATTRIBUTES =========================
    private int    bookingNo;
    private String date;
    private String startTime;
    private String endTime;
    private String status;

    // Extra display fields
    private String equipmentId;
    private String equipmentName;

    // -- CONSTRUCTOR --------------------------------------------------
    public EquipmentBooking(int bookingNo, String equipmentId, String equipmentName,String date, String startTime, String endTime) {
        this.bookingNo     = bookingNo;
        this.equipmentId   = equipmentId;
        this.equipmentName = equipmentName;
        this.date          = date;
        this.startTime     = startTime;
        this.endTime       = endTime;
        this.status        = UPCOMING;
    }

// FUNCTION : Creates booking from file data (with existing status)
    public EquipmentBooking(int bookingNo, String equipmentId, String equipmentName,String date, String startTime, String endTime, String status) {
        this(bookingNo, equipmentId, equipmentName, date, startTime, endTime);
        this.status = status;
    }

   // FUNCTION : Cancels a booking
  // OUTPUT   : true = success, false = already cancelled
  // RULE     : Cannot cancel if already CANCELLED
    public boolean cancel() {
        if (CANCELLED.equals(status)) return false;
        status = CANCELLED;
        return true;
    }

   
    public String getStatus() {
    	 return status; 
    	 	}

    /** Allow system to refresh booking status automatically. */
    public void setStatus(String status) {
        this.status = status;
    }

    // -- GETTERS ------------------------------------------------------
    public int    getBookingNo(){ 
    	return bookingNo; 
    		}
    public String getDate(){
    	 return date; 
    	 	}
    public String getStartTime(){ 
    	return startTime; 
    		}
    public String getEndTime(){
    	 return endTime; 
    	 	}
    public String getEquipmentId(){ 
    	return equipmentId; 
    		}
    public String getEquipmentName() { 
    	return equipmentName;
    	 }

    /** Serialises to one CSV line for file storage. */
    // FUNCTION : Converts booking object into file format (CSV)
   // OUTPUT   : Returns a comma-separated string
    public String toFileLine() {
        return bookingNo + "," + equipmentId + "," + equipmentName + ","
             + date + "," + startTime + "," + endTime + "," + status;
    }

    /** Parses one CSV line from file. */
    // FUNCTION : Converts file line into EquipmentBooking object
    // INPUT    : line = one line from file
    // OUTPUT   : EquipmentBooking object (or null if invalid)
    public static EquipmentBooking fromFileLine(String line) {
        String[] p = line.split(",");
        if (p.length < 7) return null;
        return new EquipmentBooking(
                Integer.parseInt(p[0].trim()),
                p[1].trim(),
                p[2].trim(),
                p[3].trim(),
                p[4].trim(),
                p[5].trim(),
                p[6].trim()
        );
    }
}