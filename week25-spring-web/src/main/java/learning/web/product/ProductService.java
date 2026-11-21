package learning.web.product;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Comparator;
import java.util.stream.Stream;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    private static final Logger log=LoggerFactory.getLogger(ProductService.class);
    private final ProductMapper productMapper;

    public ProductService(ProductMapper productMapper) {
        this.productMapper=productMapper;
    }

    public synchronized Product findBySku(String sku) {
        Product product = productMapper.findProductBySku(sku);
        if (product == null) {
            throw new ProductNotFoundException(sku);
        }
        return product;
    }

    public synchronized List<Product>listProducts(int limit,boolean sortBySku) {
        if(sortBySku){
            return productMapper.listProductBySku(limit);
        }
        return productMapper.listProductById(limit);
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
        try{
            int rows=productMapper.insertProduct(sku,name,stock);
            if(rows!=1) {
                throw new IllegalStateException("Insert failed");
            }
        }catch(DuplicateKeyException e){
            if(productMapper.findProductBySku(sku)!=null) {
                throw new DuplicateSkuException(sku);
            }
            throw e;
        }
        return new Product(sku,name,stock);
    }

    @Transactional
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
        Product old=productMapper.findProductForUpdate(sku);
        if(old==null) {
            throw new ProductNotFoundException(sku);
        }
        int rows=productMapper.updateProduct(sku,name,stock);
        boolean sameValues=old.getName().equals(name)&&old.getStock()==stock;
        if(rows!=1&&!(rows==0&&sameValues)) {
            throw new IllegalStateException("Product updated failed");
        }
        int logRows=productMapper.insertChangeLog(
                sku,
                old.getName(),
                name,
                old.getStock(),
                stock
        );
        if(logRows!=1) {
            throw new IllegalStateException("Change log insert failed");
        }
        return new Product(sku,name,stock);
    }

    public synchronized void deleteProduct(String sku) {
        int rows=productMapper.deleteProduct(sku);
        if(rows==0) {
            throw new ProductNotFoundException(sku);
        }
        log.info("Product deleted, sku={}", sku);
    }
}
