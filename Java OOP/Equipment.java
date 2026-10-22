/**
 * - Stores equipment details (ID, name, zone, category)
 * - Tracks equipment status (AVAILABLE, IN_USE, MAINTENANCE)
 * - Allows status updates
 * - Provides helper methods for status checking
 */
public class Equipment {

    // ========================= CONSTANTS =========================
    // FUNCTION : Defines valid equipment status values
    public static final String AVAILABLE   = "AVAILABLE";
    public static final String IN_USE      = "IN_USE";
    public static final String MAINTENANCE = "MAINTENANCE";
    
   // ========================= ATTRIBUTES =========================
    private String equipmentId;
    private String name;
    private String zone;
    private String category;   // CARDIO | STRENGTH
    private String status;     // AVAILABLE | IN_USE | MAINTENANCE

    // -- CONSTRUCTOR --------------------------------------------------
    public Equipment(String equipmentId, String name, String zone,String category, String status) {
        this.equipmentId = equipmentId;
        this.name        = name;
        this.zone        = zone;
        this.category    = category;
        this.status      = status;
    }

       // -- GETTERS ------------------------------------------------------
    public void   setStatus(String status) { 
    	this.status = status; 
    		}
    public String getStatus(){
    	 return status; 
    	 	}
    public String getCategory(){ 
    	return category; 
    		}
    public String getName(){ 
    	return name; 
    		}
    public String getEquipmentId() { 
    	return equipmentId;
    	 }
    public String getZone(){ 
    	return zone;
    	 }

    // ========================= HELPER METHOD =========================
    // FUNCTION : Checks if equipment is available
     // OUTPUT   : true = available, false = not available
    public boolean isAvailable() {
    	 return AVAILABLE.equals(status);
    	  }
    	  
     // FUNCTION : Checks if equipment is under maintenance
     // OUTPUT   : true = under maintenance, false otherwise
    public boolean isMaintenance() { 
    	return MAINTENANCE.equals(status);
    	 }
}