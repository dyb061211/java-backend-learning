package learning.catalog;

import learning.catalog.demo.FileReadDemo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class FileReadDemoTest {
    @TempDir
    Path tempDir;

    @Test
    void validFileShouldLoadAllProducts() throws IOException {
        Path input=tempDir.resolve("valid.tsv");
        Files.writeString(
                input,
                String.join(
                        "\n",
                        "sku\tname\tstock",
                        "A004\t鼠标垫\t3",
                        "A001\t键盘\t10",
                        "A003\t显示器\t3",
                        "A002\t鼠标\t0"
                ),
                StandardCharsets.UTF_8
        );
        ProductCatalog catalog =FileReadDemo.loadCatalog(input);

        assertEquals(4,catalog.count());
        long totalStock=catalog.listProducts()
                .stream()
                .mapToLong(Product::getStock)
                .sum();
        assertEquals(16,totalStock);
    }

    @Test
    void duplicateSkuShouldFailWithLineNumber() throws IOException {
        Path input=tempDir.resolve("duplicate.tsv");
        Files.writeString(
                input,
                String.join(
                        "\n",
                        "sku\tname\tstock",
                        "A001\t键盘\t10",
                        "A002\t鼠标\t20",
                        "A001\t显示器\t5"
                ),
                StandardCharsets.UTF_8
        );
        IllegalArgumentException exception=assertThrows(
                IllegalArgumentException.class,
                () -> FileReadDemo.loadCatalog(input)
        );
        assertTrue(exception.getMessage().contains("第4行"));
        assertTrue(exception.getMessage().contains("SKU已存在"));
    }
}
