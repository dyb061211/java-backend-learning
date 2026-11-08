package learning.catalog;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;

public class ProductCatalog {
    private final Map<String,Product>products=new LinkedHashMap<>();
    public void addProduct(Product p) {
        if(p==null) {
            throw new IllegalArgumentException("商品不能为空");
        }
        if(products.containsKey(p.getSku())) {
            throw new IllegalArgumentException("SKU已存在");
        }
        products.put(p.getSku(),p);
    }
    public int count() {
        return products.size();
    }
    public Product findBySku(String sku) {
        if (sku == null || sku.isBlank()) {
            throw new IllegalArgumentException("SKU不能为空");
        }
        return products.get(sku);
    }
    public void updateProductName(String sku,String newName) {
        Product p=findBySku(sku);
        if(p==null) {
            throw new IllegalArgumentException("商品不存在：" + sku);
        }
        p.updateName(newName);
    }
    public void updateProductStock(String sku, int newStock) {
        Product product = findBySku(sku);

        if (product == null) {
            throw new IllegalArgumentException("商品不存在：" + sku);
        }

        product.updateStock(newStock);
    }
    public List<Product> listProducts() {
        return new ArrayList<>(products.values());
    }
}
