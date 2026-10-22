import java.util.ArrayList;
import java.util.List;

/**
 * - Stores health data such as weight, height, target weight, BMI, and goal
 * - Calculates BMI
 * - Updates height, weight, and target weight
 * - Records progress history
 * - Supports file-safe encoding and decoding of history data
 */
public class HealthProfile {
    // ========================= ATTRIBUTES =========================
    private double heightCm;
    private double currentWeightKg;
    private double targetWeightKg;
    private double bmi;
    private String historyTable;     // newline-separated log entries
    private String goal;

    // Allowed goal values 
    public static final String GOAL_WEIGHT_LOSS   = "Weight Loss";
    public static final String GOAL_GENERAL       = "General Health & Fitness";
    public static final String GOAL_PHYSIQUE      = "Maintain Physique/Bodybuilding";

    // -- CONSTRUCTOR --------------------------------------------------
    // Constructor 1: for first-time profile setup
    /** Creates a new HealthProfile and records the initial weight entry.*/
    public HealthProfile(double weightKg, double heightCm,double targetKg, String goal, String date) {
        this.currentWeightKg = weightKg;
        this.heightCm        = heightCm;
        this.targetWeightKg  = targetKg;
        this.goal            = goal;
        this.bmi             = calculateBMI(weightKg, heightCm);
        this.historyTable    = buildEntry(date, weightKg, heightCm, bmi, "Initial");
    }
     
    // Constructor 2: for loading existing profile from file
    /** Constructor for loading from file (historyTable already stored). */
    public HealthProfile(double weightKg, double heightCm,double targetKg, String goal,double bmi, String historyTable) {
        this.currentWeightKg = weightKg;
        this.heightCm        = heightCm;
        this.targetWeightKg  = targetKg;
        this.goal            = goal;
        this.bmi             = bmi;
        this.historyTable    = historyTable;
    }

    // ========================= CALCULATION METHOD =========================
    // FUNCTION : Calculates BMI using weight and height
    // INPUT    : weightKg = body weight in kilograms
    //            heightCm = body height in centimeters
    // OUTPUT   : Returns BMI value
    // RULE     : Returns 0.0 if height is invalid
    public double calculateBMI(double weightKg, double heightCm) {
        if (heightCm <= 0) return 0.0;
        double hm = heightCm / 100.0;
        return weightKg / (hm * hm);
    }

  // FUNCTION : Updates member height and recalculates BMI
    // INPUT    : newHeightCm, date
    // OUTPUT   : No return
    // RULE     : Adds a HEIGHT record into history table
    public void updateHeight(double newHeightCm, String date) {
        this.heightCm = newHeightCm;
        this.bmi      = calculateBMI(currentWeightKg, heightCm);
        historyTable  = historyTable + buildEntry(date, currentWeightKg, heightCm, bmi, "HEIGHT");
    }

    /** +updateWeight(newWeightKg:double) : void - recalculates BMI after change */
    public void updateWeight(double newWeightKg, String date) {
        this.currentWeightKg = newWeightKg;
        this.bmi             = calculateBMI(currentWeightKg, heightCm);
        historyTable         = historyTable + buildEntry(date, currentWeightKg, heightCm, bmi, "WEIGHT");
    }

    /** +updateTargetWeight(newTargetKg:double) : void */
    public void updateTargetWeight(double newTargetKg, String date) {
        this.targetWeightKg = newTargetKg;
        historyTable        = historyTable + buildEntry(date, currentWeightKg, heightCm, bmi, "TARGET");
    }

    /** +getBMI() : double */
    public double getBMI() {
    	 return bmi; 
    	 	}

    /** +getHistory() : String  - returns newline-separated history entries */
    public String getHistory() { 
    	return historyTable; 
    		}

    // -- GETTERS ------------------------------------------------------
    public double getHeightCm()        { 
    	return heightCm; 
    		}
    public double getCurrentWeightKg() { 
    	return currentWeightKg; 
    		}
    public double getTargetWeightKg()  { 
    	return targetWeightKg; 
    		}
    public String getGoal()            { 
    	return goal; 
    		}

    /** Returns a printable BMI status label. */
    public String getBMIStatus() {
        if (bmi < 18.5)       return "UNDERWEIGHT";
        else if (bmi < 25.0)  return "NORMAL WEIGHT";
        else if (bmi < 30.0)  return "OVERWEIGHT";
        else                  return "OBESE";
    }

    // -- PRIVATE HELPERS ----------------------------------------------
    /**
     * Builds one pipe-delimited history entry line:
     *   DATE|WEIGHT|HEIGHT|BMI|UPDATE_TYPE\n
     */
    private String buildEntry(String date, double weight, double height, double bmi, String type) {
        return date + "|" + weight + "|" + height + "|"
             + String.format("%.2f", bmi) + "|" + type + "\n";
    }

    /**
     * Parses the historyTable string into displayable rows.
     * Each row is: [date, weight, height, bmi, type]
     */
    public List<String[]> getParsedHistory() {
        List<String[]> rows = new ArrayList<>();
        if (historyTable == null || historyTable.isEmpty()) return rows;
        for (String line : historyTable.split("\n")) {
            String[] parts = line.split("\\|");
            if (parts.length == 5) rows.add(parts);
        }
        return rows;
    }

    // -- FILE SERIALIZATION -------------------------------------------
    /**
     * Encodes historyTable to a single-line safe string for file storage.
     * Newlines replaced with "~~".
     */
    public String encodeHistory() {
        if (historyTable == null) return "";
        return historyTable
        .replace("|", "~p~")   
        .replace("\n", "~~");
    }

    /** Decodes the stored single-line string back to newline-separated history. */
    public static String decodeHistory(String encoded) {
        if (encoded == null) return "";
        return encoded
        .replace("~~", "\n")
        .replace("~p~", "|");   
    }
}