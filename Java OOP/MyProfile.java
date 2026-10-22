/**
 * - Stores personal data (name, age, phone, gender)
 * - Provides getter methods for accessing profile data
 * - Allows limited updates to profile information
 */
public class MyProfile {

// ========================= ATTRIBUTES =========================
    private String name;
    private int    age;
    private String phone;
    private String gender;   // "M" or "F"

    // -- CONSTRUCTOR --------------------------------------------------
    public MyProfile(String name, int age, String phone, String gender) {
        this.name   = name;
        this.age    = age;
        this.phone  = phone;
        this.gender = gender;
    }

  // ========================= GETTERS =========================
    public String getName()   { 
    	return name; 
    		}
    public int    getAge()    { 
    	return age; 
    		}
    public String getPhone()  { 
    	return phone;
    	 }
    public String getGender() { 
    	return gender; 
    		}

    /** +setPhone(phone:String) : void  - only phone is mutable per UML */
    public void setPhone(String newPhone) { this.phone = newPhone; }

    // Extra setters needed by Admin edit-member functionality
    public void setName(String name)  {
    	 this.name = name; 
    	 	 }
    public void setAge(int age)       { 
    	this.age  = age;   
    		}
}