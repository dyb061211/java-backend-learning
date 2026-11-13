package learning.catalog;

public class Product {
    private String sku;
    private String name;
    private int stock;
    private static int createdCount=0;
    public Product(String sku, String name, int stock) {
        if(stock<0) {
            throw new IllegalArgumentException("库存数不能为负数");
        }
        if(sku==null||sku.isBlank()) {
            throw new IllegalArgumentException("SKU不能为空");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("商品名称不能为空");
        }
        if(name.length()>20) {
            throw new IllegalArgumentException("商品名称不能大于20个字符");
        }
        this.sku = sku;
        this.name = name;
        this.stock = stock;
        createdCount++;
    }
    public String getName() {
        return name;
    }
    public int getStock() {
        return stock;
    }
    public String getSku() {
        return sku;
    }
    public void updateStock(int newStock) {
        if (newStock < 0) {
            throw new IllegalArgumentException("库存不能为负数");
        }
        this.stock = newStock;
    }
    public void updateName(String newName) {
        if (newName == null || newName.isBlank()) {
            throw new IllegalArgumentException("商品名称不能为空");
        }
        if(newName.length()>20) {
            throw new IllegalArgumentException("商品名称不能大于20个字符");
        }
        this.name = newName;
    }
    public static int getCreatedCount() {
        return createdCount;
    }
}
