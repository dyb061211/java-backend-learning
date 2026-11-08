package learning.catalog;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.List;

public class ProductCatalogTest {
    @Test
    void addProductShouldIncreaseCount(){
        ProductCatalog catalog= new ProductCatalog();
        Product product=new Product("A001", "键盘", 10);
        catalog.addProduct(product);
        assertEquals(1,catalog.count());
    }

    @Test
    void duplicateSkuShouldBeRejected() {
        ProductCatalog catalog= new ProductCatalog();
        catalog.addProduct(new Product("A001", "键盘", 10));
        assertThrows(
                IllegalArgumentException.class,
                () -> catalog.addProduct(
                        new Product("A001", "重复商品", 5)
                )
        );
        assertEquals(1,catalog.count());
    }

    @Test
    void invalidStockShouldKeepOriginalValue() {
        ProductCatalog catalog= new ProductCatalog();
        Product product = new Product("A001", "键盘", 10);
        catalog.addProduct(product);
        assertThrows(
                IllegalArgumentException.class,
                () -> catalog.updateProductStock("A001", -5)
        );
        assertEquals(10, product.getStock());
    }

    @Test
    void invalidSetName() {
        Product p;
        String longName="A".repeat(21);
        assertThrows(
                IllegalArgumentException.class,
                () -> new Product("A001",longName,20)
        );
    }

    @Test
    void invalidUpdateName() {
        Product p =new Product("A001","A".repeat(20),20);
        String longName="A".repeat(21);
        assertThrows(
                IllegalArgumentException.class,
                () -> p.updateName(longName)
        );
        assertEquals("A".repeat(20),p.getName());
    }

    @Test
    void listProductsShouldKeepOrderAndProtectStructure() {
        ProductCatalog catalog=new ProductCatalog();
        catalog.addProduct(new Product("A003", "显示器", 5));
        catalog.addProduct(new Product("A001", "键盘", 10));
        catalog.addProduct(new Product("A002", "鼠标", 20));

        List<Product> list =catalog.listProducts();

        assertEquals("A003",list.get(0).getSku());
        assertEquals("A001", list.get(1).getSku());
        assertEquals("A002", list.get(2).getSku());

        list.clear();

        assertEquals(3, catalog.count());
        assertEquals("A003", catalog.listProducts().get(0).getSku());
    }
}
