/**
 * - Stores current loyalty points
 * - Records earn and redeem transactions
 * - Provides reward catalogue constants
 * - Supports file-safe encoding and decoding of history
 * - Normalizes history format for display and storage
 */
public class LoyaltyPoints {

    // ========================= ATTRIBUTES =========================
    private int    points;
    private String history;   // newline-separated log, encoded with "~~" for file storage
    // Rewards catalogue (rewardNo --> {name, points})
    public static final String[] REWARD_NAMES = {"", "Fitness Club T-Shirt", "Shaker Bottle", "Fitness Club Towel", "Premium Gym Bag"};

    public static final int[] REWARD_POINTS = {0, 500, 800, 1500, 3000};

    // -- CONSTRUCTORS -------------------------------------------------

    /** New member starts with 0 points and empty history. */
    public LoyaltyPoints() {
        this.points  = 0;
        this.history = "";
    }

    /** Load from file. */
    public LoyaltyPoints(int points, String encodedHistory) {
        this.points  = points;
        this.history = decodeHistory(encodedHistory);
    }

    // -- OPERATIONS ---------------------------------------------------

    /** Add points and record the entry. */
    public void earn(int amount, String reason, String date) {
        points  += amount;
        history += date + "|EARN|+" + amount + "|" + reason + "\n";
    }

    /** Deduct points and record the entry. Returns false if not enough points. */
    public boolean spend(int amount, String reason, String date) {
        if (points < amount) return false;
        points  -= amount;
        history += date + "|REDEEM|-" + amount + "|" + reason + "\n";
        return true;
    }

    // -- GETTERS ------------------------------------------------------
    public int    getPoints()  { 
    	return points; 
    		}
    public String getHistory() { 
    	return history; 
    		}
// ========================= FILE METHOD =========================
// FUNCTION : Encodes history into single-line safe text for file storage
// OUTPUT   : Returns encoded history string
// RULE     : Replaces '|' with '~p~' and newline with '~~'
/** File-safe encoding: replace newlines with "~~". */
    public String encodeHistory() {
    if (history == null) return "";
    return history
            .replace("|", "~p~")
            .replace("\n", "~~");
}
// ========================= FILE METHOD =========================
// FUNCTION : Decodes stored history text back into original format
// INPUT    : encoded = encoded history string
// OUTPUT   : Returns decoded history
private static String decodeHistory(String encoded) {
    if (encoded == null) return "";
    return encoded
            .replace("~~", "\n")
            .replace("~p~", "|");
}
// ========================= HELPER METHOD =========================
// FUNCTION : Cleans and normalizes history format
// OUTPUT   : No return
// RULE     : Ensures each history record is stored as one valid line
    public void normalizeHistory() {
    if (history == null || history.trim().isEmpty()) {
        history = "";
        return;
    }

   
    String raw = history.replace("~~", "\n").replace("\r", "");

   
    raw = raw.replaceAll(
        "(\\d{2}-\\d{2}-\\d{4})(\\d{2}-\\d{2}-\\d{4}\\|)",
        "$1\n$2"
    );

    String[] lines = raw.split("\n");
    StringBuilder fixed = new StringBuilder();

    for (String line : lines) {
        line = line.trim();
        if (line.isEmpty()) continue;

        String[] p = line.split("\\|");
        if (p.length < 4) continue;

        String date = p[0].trim();
        String type = p[1].trim();
        String pts  = p[2].trim();
        String reason = p[3].trim();

        fixed.append(date)
             .append("|")
             .append(type)
             .append("|")
             .append(pts)
             .append("|")
             .append(reason)
             .append("\n");
    }

    history = fixed.toString();
}
}