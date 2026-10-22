import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * - Stores membership tier, fee, and expiry date
 * - Calculates membership fee based on tier
 * - Handles membership upgrade logic
 * - Manages membership validity period
 */
public class Membership {
	
// ========================= CONSTANTS =========================
// FUNCTION : Defines membership tiers and corresponding fees
    public static final String BASIC  = "BASIC";
    public static final String SILVER = "SILVER";
    public static final String GOLD   = "GOLD";

    public static final double FEE_BASIC  = 60.0;
    public static final double FEE_SILVER = 150.0;
    public static final double FEE_GOLD   = 250.0;

    private static final DateTimeFormatter FMT =DateTimeFormatter.ofPattern("dd-MM-yyyy");

    // -- ATTRIBUTES ----------------------------------------------
    private String tier;
    private double monthlyFee;
    private String expiryDate;

    // -- CONSTRUCTORS -------------------------------------------------

    /** Creates a new membership at the given tier, sets fee and 30-day expiry. */
    public Membership(String tier) {
        this.tier       = tier;
        this.monthlyFee = calculateFee(tier);
        this.expiryDate = LocalDate.now().plusMonths(1).format(FMT);
    }

    /** Constructor for loading from file with all fields known. */
    public Membership(String tier, double monthlyFee, String expiryDate) {
        this.tier       = tier;
        this.monthlyFee = monthlyFee;
        this.expiryDate = expiryDate;
    }

    // -- OPERATIONS -----------------------------------------------

    /** +calculateFee(tier:String) : double */
    public double calculateFee(String tier) {
        switch (tier) {
            case SILVER: return FEE_SILVER;
            case GOLD:   return FEE_GOLD;
            default:     return FEE_BASIC;
        }
    }

    /**
     * +upgradeTo(newTier:String) : void
     * Updates tier, recalculates fee, and extends expiry by 1 month.
     */
    public void upgradeTo(String newTier) {
        this.tier       = newTier;
        this.monthlyFee = calculateFee(newTier);
        this.expiryDate = LocalDate.now().plusMonths(1).format(FMT);
    }

    // -- GETTERS ------------------------------------------------------
    public String getTier(){ 
    	return tier;
    	 }
    public double getMonthlyFee() { 
    	return monthlyFee; 
    		}
    public String getExpiryDate() { 
    	return expiryDate;
    	 }

    // Extra setters for Admin use
    public void setTier(String tier){ 
    	this.tier = tier;
    	 }
    public void setFee(double fee){ 
    	this.monthlyFee = fee;
    	 }
}