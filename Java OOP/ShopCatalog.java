import java.util.ArrayList;
import java.util.List;

/**
 * - Stores list of products
 * - Filters products by category
 * - Retrieves products by index or ID
 * - Handles stock reduction after purchase
 * - Supports admin product management (add/remove)
 */
public class ShopCatalog {

    // -- ATTRIBUTES ----------------------------------------------
    private List<Product> products;

    // -- CONSTRUCTORS -------------------------------------------------
    public ShopCatalog() {
        this.products = new ArrayList<>();
    }

    public ShopCatalog(List<Product> products) {
        this.products = products;
    }

    // -- OPERATIONS -----------------------------------------------

    /** +listByCategory(category:String) : List<Product> */
    public List<Product> listByCategory(String category) {
        List<Product> result = new ArrayList<>();
        for (Product p : products) {
            if (p.getCategory().equalsIgnoreCase(category)) {
                result.add(p);
            }
        }
        return result;
    }

    /**
     * +getProduct(productNo:int) : Product
     * productNo is the global 1-based index across ALL products.
     */
    public Product getProduct(int productNo) {
        if (productNo < 1 || productNo > products.size()) return null;
        return products.get(productNo - 1);
    }

    /**
     * Returns a product by its productId string (e.g. "P001").
     */
    public Product getProductById(String productId) {
        for (Product p : products) {
            if (p.getProductId().equalsIgnoreCase(productId.trim())) return p;
        }
        return null;
    }

    /** +reduceStock(productNo:int, qty:int) : boolean */
    public boolean reduceStock(int productNo, int qty) {
        Product p = getProduct(productNo);
        if (p == null || !p.isInStock(qty)) return false;
        p.deductStock(qty);
        return true;
    }

    // -- ADMIN METHODS ------------------------------------------------

    /** Adds a product to the catalog. */
    public void addProduct(Product p) {
        products.add(p);
    }

    /** Removes a product by productId. Returns true if found and removed. */
    public boolean removeProduct(String productId) {
        return products.removeIf(
            p -> p.getProductId().equalsIgnoreCase(productId.trim()));
    }

    /** Returns ALL products. */
    public List<Product> getAllProducts() { 
    	return products; 
    		}

    /** Generates next productId like P001, P002 ... */
    public String generateNextProductId() {
        int max = 0;
        for (Product p : products) {
            String id = p.getProductId();
            if (id.startsWith("P")) {
                try {
                    int n = Integer.parseInt(id.substring(1));
                    if (n > max) max = n;
                } catch (Exception ignored) {}
            }
        }
        return String.format("P%03d", max + 1);
    }
}