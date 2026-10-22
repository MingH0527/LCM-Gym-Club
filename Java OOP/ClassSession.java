/**
 * ========================= CLASS TYPE =========================
 * TYPE     : DATA MODEL + SLOT CONTROL CLASS
 * PURPOSE  : Stores class session details and manages slot availability
 *
 * FUNCTION :
 * - Stores class session information (day, time, trainer, location)
 * - Tracks slot usage (slotUsed vs slotLimit)
 * - Controls booking capacity (reserve / release slot)
 * - Supports file storage (save/load)
 */
public class ClassSession {

   // ========================= ATTRIBUTES =========================
   // FUNCTION : Stores class session data and slot information
    private int    classNo;
    private String day;
    private String time;
    private String className;
    private String location;
    private int    slotUsed;
    private int    slotLimit;

    // Extra display fields
    private String trainerId;
    private String trainerName;

    // ========================= CONSTRUCTOR =========================
    // FUNCTION : Initializes a class session with full data
    public ClassSession(int classNo, String day, String time, String className,String location, int slotUsed, int slotLimit,String trainerId, String trainerName) {
        this.classNo     = classNo;
        this.day         = day;
        this.time        = time;
        this.className   = className;
        this.location    = location;
        this.slotUsed    = slotUsed;
        this.slotLimit   = slotLimit;
        this.trainerId   = trainerId;
        this.trainerName = trainerName;
    }
// FUNCTION : Checks if there are available slots
// OUTPUT   : true = slot available, false = full
    public boolean hasAvailableSlot() { 
    	return slotUsed < slotLimit; 
    		}

   // FUNCTION : Reserves a slot if available
  // OUTPUT   : true = success, false = no slot available
  // RULE     : Only increases slotUsed if slot is available
    public boolean reserveSlot() {
        if (!hasAvailableSlot()) return false;
        slotUsed++;
        return true;
    }

   // FUNCTION : Releases a slot (e.g., when booking is cancelled)
   // OUTPUT   : No return
   // RULE     : slotUsed cannot go below 0
    public void releaseSlot() {
        if (slotUsed > 0) slotUsed--;
    }

    // -- GETTERS ------------------------------------------------------
    public int    getClassNo() { 
    	return classNo; 
    		}
    public String getDay(){ 
    	return day;
    	}
    public String getTime(){ 
    	return time;
    	 }
    public String getClassName(){ 
    	return className; 
    		}
    public String getLocation(){ 
    	return location; 
    		}
    public int    getSlotUsed(){ 
    	return slotUsed; 
    		}
    public int    getSlotLimit(){ 
    	return slotLimit; 
    		}
    public String getTrainerId(){ 
    	return trainerId;
    	 }
    public String getTrainerName() { 
    	return trainerName; 
    		}

 // FUNCTION : Converts class session object into file format (CSV)
// OUTPUT   : Returns a comma-separated string
    public String toFileLine() {
        return classNo + "," + day + "," + time + "," + className + ","
             + location + "," + slotUsed + "," + slotLimit + ","
             + trainerId + "," + trainerName;
    }
   
   
     // FUNCTION : Converts a file line into ClassSession object
     // INPUT    : line = one line from file
    // OUTPUT   : ClassSession object (or null if invalid)
    public static ClassSession fromFileLine(String line) {
        String[] p = line.split(",");
        if (p.length < 9) return null;
        return new ClassSession(Integer.parseInt(p[0].trim()),
                p[1].trim(), p[2].trim(), p[3].trim(),
                p[4].trim(),
                Integer.parseInt(p[5].trim()),
                Integer.parseInt(p[6].trim()),
                p[7].trim(), p[8].trim());
    }
}