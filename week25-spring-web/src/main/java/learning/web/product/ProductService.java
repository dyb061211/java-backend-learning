package learning.web.product;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Comparator;
import java.util.stream.Stream;

@Service
public class ProductService {
    private final Map<String,Product> products=new LinkedHashMap<>();
    private static final Logger log=LoggerFactory.getLogger(ProductService.class);

    public ProductService() {
        products.put("A001", new Product("A001", "键盘", 10));
        products.put("A002", new Product("A002", "鼠标", 20));
        products.put("A003", new Product("A003", "显示器", 5));
    }

    public synchronized Product findBySku(String sku) {
        Product product = products.get(sku);
        if (product == null) {
            throw new ProductNotFoundException(sku);
        }
        return product;
    }

    public synchronized List<Product>listProducts(int limit,boolean sortBySku) {
        Stream<Product> stream=products.values().stream();
        if(sortBySku){
            stream=stream.sorted(Comparator.comparing(Product::getSku));
        }
        return stream
                .limit(limit)
                .toList();
    }

    public synchronized Product createProduct(String sku,String name,int stock) {
        if(sku==null||sku.isBlank()) {
            throw new IllegalArgumentException("Invalid SKU");
        }
        if(name==null||name.isBlank()||name.length()>20) {
            throw new IllegalArgumentException("Invalid name");
        }
        if(stock<0) {
            throw new IllegalArgumentException("Invalid stock");
        }
        if(products.containsKey(sku)) {
            log.warn("Created reject, duplicate sku={}",sku);
            throw new DuplicateSkuException(sku);
        }
        Product product = new Product(sku,name,stock);
        products.put(sku,product);
        log.info("Product created, sku={}",sku);
        return product;
    }

    public synchronized Product replaceProduct(String sku,String name,int stock) {
        if (sku == null || sku.isBlank()) {
            throw new IllegalArgumentException("Invalid SKU");
        }
        if (name == null || name.isBlank() || name.length() > 20) {
            throw new IllegalArgumentException("Invalid name");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("Invalid stock");
        }
        if (!products.containsKey(sku)) {
            throw new ProductNotFoundException(sku);
        }
        Product updated = new Product(sku, name, stock);
        products.put(sku, updated);
        log.info("Product updated, sku={}",sku);
        return updated;
    }

    public synchronized void deleteProduct(String sku) {
        if (products.remove(sku) == null) {
            throw new ProductNotFoundException(sku);
        }
        log.info("Product deleted, sku={}", sku);
    }
}
