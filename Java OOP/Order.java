/**
 * - Stores order information (ID, date, items, member)
 * - Calculates SST and total payment
 * - Generates formatted receipt
 * - Manages order status (PROCESSING, COMPLETED, CANCELLED)
 * - Supports file serialization
 */
public class Order {

    public static final String PROCESSING = "PROCESSING";
    public static final String COMPLETED  = "COMPLETED";
    public static final String CANCELLED  = "CANCELLED";

    public static final String E_WALLET = "E_WALLET";
    public static final String CASH     = "CASH";

    private static final double SST_RATE = 0.06;  // 6% SST

    // --  ATTRIBUTES ----------------------------------------------
    private String orderId;
    private String date;
    private String itemsSummary;
    private double subTotal;
    private double sstRate;
    private double sstAmount;
    private double totalPaid;
    private String paymentMethod;
    private String status;

    // Extra field: member name (for admin view)
    private String memberName;

    // -- CONSTRUCTOR --------------------------------------------------
    public Order(String orderId, String date, String memberName,String itemsSummary, double subTotal,String paymentMethod) {
        this.orderId        = orderId;
        this.date           = date;
        this.memberName     = memberName;
        this.itemsSummary   = itemsSummary;
        this.subTotal       = subTotal;
        this.sstRate        = SST_RATE;
        this.paymentMethod  = paymentMethod;
        this.status         = PROCESSING;
        calculateTotals();
    }

    /** Constructor for loading from file. */
    public Order(String orderId, String date, String memberName,String itemsSummary, double subTotal, double sstRate,double sstAmount, double totalPaid,String paymentMethod, String status) {
        this.orderId       = orderId;
        this.date          = date;
        this.memberName    = memberName;
        this.itemsSummary  = itemsSummary;
        this.subTotal      = subTotal;
        this.sstRate       = sstRate;
        this.sstAmount     = sstAmount;
        this.totalPaid     = totalPaid;
        this.paymentMethod = paymentMethod;
        this.status        = status;
    }

    // -- OPERATIONS -----------------------------------------------

    /** +calculateTotals() : double - fills sstAmount and totalPaid. */
    public double calculateTotals() {
        sstAmount = subTotal * sstRate;
        totalPaid = subTotal + sstAmount;
        return totalPaid;
    }

    /**
     * +printReceipt() : String
     * Returns a formatted receipt string matching the draft output.
     */
    public String printReceipt() {
        return "=========================================\n"
             + "OFFICIAL RECEIPT\n"
             + "=========================================\n"
             + "ORDER ID : " + orderId + "\n"
             + "DATE     : " + date + "\n"
             + "MEMBER   : " + memberName + "\n"
             + "-----------------------------------------\n"
             + "ITEM     : " + itemsSummary + "\n"
             + String.format("PRICE    : RM %.2f%n", subTotal)
             + String.format("SST (6%%) : RM %.2f%n", sstAmount)
             + "-----------------------------------------\n"
             + String.format("TOTAL PAID : RM %.2f%n", totalPaid)
             + "PAYMENT  : " + paymentMethod + "\n"
             + "STATUS   : PAID - READY FOR PICKUP\n"
             + "=========================================";
    }

    // -- GETTERS / SETTERS --------------------------------------------
    public String getOrderId()      { 
    	return orderId;
    	 }
    public double getTotalPaid()    { 
    	return totalPaid; 
    		}
    public String getStatus()       { 
    	return status; 
    		}
    public void   setStatus(String status) { 
    	this.status = status; 
    		}
    public String getDate()         { 
    	return date; 
    		}
    public String getItemsSummary() { 
    	return itemsSummary; 
    		}
    public double getSubTotal()     { 
    	return subTotal; 
    		}
    public double getSstAmount()    { 
    	return sstAmount; 
    		}
    public String getPaymentMethod(){ 
    	return paymentMethod; 
    		}
    public String getMemberName()   { 
    	return memberName;
    	 }

    // -- FILE SERIALIZATION -------------------------------------------
    /**
     * Serialises to a pipe-delimited line (pipes used because itemsSummary
     * may contain commas).
     */
    public String toFileLine() {
        return orderId + "|" + date + "|" + memberName + "|"
             + itemsSummary + "|" + subTotal + "|" + sstRate + "|"
             + sstAmount + "|" + totalPaid + "|"
             + paymentMethod + "|" + status;
    }
    
    
    //Converts file line into Order object
    public static Order fromFileLine(String line) {
        String[] p = line.split("\\|");
        if (p.length < 10) return null;
        return new Order(
                p[0].trim(), p[1].trim(), p[2].trim(), p[3].trim(),
                Double.parseDouble(p[4].trim()),
                Double.parseDouble(p[5].trim()),
                Double.parseDouble(p[6].trim()),
                Double.parseDouble(p[7].trim()),
                p[8].trim(), p[9].trim());
    }
}