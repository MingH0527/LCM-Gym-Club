/**
 * - Stores appointment details (trainer, date, time)
 * - Tracks appointment status (UPCOMING, CONFIRMED, CANCELLED)
 * - Supports cancellation operation
 * - Provides data for display (trainer info)
 * - Supports file serialization
 */
public class TrainerAppointment {

    public static final String UPCOMING  = "UPCOMING";
    public static final String CONFIRMED = "CONFIRMED";
    public static final String CANCELLED = "CANCELLED";

    // -- ATTRIBUTES ----------------------------------------------
    private int    appointmentNo;
    private String date;
    private String startTime;
    private String endTime;
    private String status;

    // Extra display fields
    private String trainerId;
    private String trainerName;
    private String specialty;

// ========================= CONSTRUCTOR =========================
// FUNCTION : Creates a new trainer appointment with default status
// INPUT    : appointmentNo, trainerId, trainerName, specialty,
//            date, startTime, endTime
// OUTPUT   : Initializes appointment with UPCOMING status
    public TrainerAppointment(int appointmentNo, String trainerId,String trainerName, String specialty,String date, String startTime, String endTime) {
        this.appointmentNo = appointmentNo;
        this.trainerId     = trainerId;
        this.trainerName   = trainerName;
        this.specialty     = specialty;
        this.date          = date;
        this.startTime     = startTime;
        this.endTime       = endTime;
        this.status        = UPCOMING;
    }
    
// ========================= CONSTRUCTOR =========================
// FUNCTION : Restores appointment data from file
// INPUT    : includes status
// OUTPUT   : Loads existing appointment state
    /** Constructor for loading from file. */
    public TrainerAppointment(int appointmentNo, String trainerId,String trainerName, String specialty,String date, String startTime,String endTime, String status) {
        this(appointmentNo, trainerId, trainerName, specialty,date, startTime, endTime);
        this.status = status;
    }


// FUNCTION : Cancels appointment
// OUTPUT   : true = success, false = already cancelled
// RULE     : Cannot cancel if status is already CANCELLED
    public boolean cancel() {
        if (CANCELLED.equals(status)) return false;
        status = CANCELLED;
        return true;
    }

    // -- GETTERS ------------------------------------------------------
    public int    getAppointmentNo() { 
    	return appointmentNo; 
    		}
    public String getDate()          { 
    	return date; 
    		}
    public String getStartTime()     { 
    	return startTime; 
    		}
    public String getEndTime()       {
    	 return endTime; 
    	 	}
    public String getStatus()        { 
    	return status; 
    		}
    public String getTrainerId()     { 
    	return trainerId;
    	 }
    public String getTrainerName()   { 
    	return trainerName; 
    		}
    public String getSpecialty()     { 
    	return specialty;
    	 }

// ========================= FILE METHOD =========================
// FUNCTION : Converts appointment object into file format
// OUTPUT   : Returns comma-separated string
    public String toFileLine() {
        return appointmentNo + "," + trainerId + "," + trainerName + ","
             + specialty + "," + date + "," + startTime + ","
             + endTime + "," + status;
    }
    
// ========================= FILE METHOD =========================
// FUNCTION : Converts file line into TrainerAppointment object
// INPUT    : line = one line from file
// OUTPUT   : TrainerAppointment object or null if invalid
    public static TrainerAppointment fromFileLine(String line) {
        String[] p = line.split(",");
        if (p.length < 8) return null;
        return new TrainerAppointment(
                Integer.parseInt(p[0].trim()),
                p[1].trim(), p[2].trim(), p[3].trim(),
                p[4].trim(), p[5].trim(), p[6].trim(), p[7].trim());
    }
}