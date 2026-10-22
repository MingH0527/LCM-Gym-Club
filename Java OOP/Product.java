/**
 * - Stores product details (ID, name, brand, category, price)
 * - Tracks product stock level
 * - Checks stock availability before purchase
 * - Deducts stock after successful purchase
 * - Supports file serialization
 */
public class Product {

    public static final String FITNESS_EQUIPMENT = "FITNESS_EQUIPMENT";
    public static final String SUPPLEMENTS       = "SUPPLEMENTS";

    // -- ATTRIBUTES ----------------------------------------------
    private String productId;
    private String name;
    private String brand;
    private String category;
    private double price;
    private int    stock;

    // -- CONSTRUCTOR --------------------------------------------------
    public Product(String productId, String name, String brand,String category, double price, int stock) {
        this.productId = productId;
        this.name      = name;
        this.brand     = brand;
        this.category  = category;
        this.price     = price;
        this.stock     = stock;
    }

    // -- OPERATIONS -----------------------------------------------
    public boolean isInStock(int qty)   {
    	 return stock >= qty; 
    	 	}
    public void    deductStock(int qty) { 
    	if (stock >= qty) stock -= qty;
    	 }
    public double  getPrice(){ 
    	return price; 
    		}
    public String  getName(){ 
    	return name; 
    		}

    // -- GETTERS / SETTERS --------------------------------------------
    public String getProductId() { 
    	return productId;
    	 }
    public String getBrand(){ 
    	return brand;
    	 }
    public String getCategory()  { 
    	return category; 
    		}
    public int    getStock(){ 
    	return stock; 
    		}
    public void   setPrice(double price) {
    	 this.price = price; 
    	 	}
    public void   setStock(int stock) { 
    	this.stock = stock;
    	 }
    public void   setName(String name){ 
    	this.name  = name; 
    		 }

    public boolean isOutOfStock() { 
    	return stock <= 0; 
    		}

  // ========================= FILE METHOD =========================
    // FUNCTION : Converts product object into file format
    public String toFileLine() {
        return productId + "," + name + "," + brand + ","
             + category + "," + price + "," + stock;
    }

  // ========================= FILE METHOD =========================
    // FUNCTION : Converts file line into Product object
    public static Product fromFileLine(String line) {
        String[] p = line.split(",");
        if (p.length < 6) return null;
        return new Product(p[0].trim(), p[1].trim(), p[2].trim(),
                p[3].trim(),
                Double.parseDouble(p[4].trim()),
                Integer.parseInt(p[5].trim()));
    }
    @Override
public String toString() {
    return productId + " - " + name + " (RM " + price + ") Stock: " + stock;
}

@Override
public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof Product)) return false;
    Product p = (Product) obj;
    return this.productId.equals(p.productId);
}
}