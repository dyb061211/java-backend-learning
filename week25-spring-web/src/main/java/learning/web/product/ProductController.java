package learning.web.product;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.web.util.UriComponentsBuilder;
import learning.web.config.CatalogProperties;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    private final CatalogProperties catalogProperties;
    public ProductController(ProductService productService, CatalogProperties catalogProperties) {
        this.catalogProperties = catalogProperties;
        this.productService = productService;
    }

    @GetMapping("/{sku}")
    public ResponseEntity<Product> getProduct(@PathVariable("sku") String sku) {
        Product product=productService.findBySku(sku);
        return ResponseEntity.ok(product);
    }

    @GetMapping
    public List<Product> getProducts(@RequestParam(name="limit",required=false) Integer limit,@RequestParam(name = "sortBySku", defaultValue = "false")boolean sortBySku) {
        int actualLimit=(limit==null)?catalogProperties.getDefaultLimit():limit;
        if(actualLimit<1||actualLimit>100) {
            throw new InvalidLimitException();
        }
        return productService.listProducts(actualLimit,sortBySku);
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody CreateProductRequest request) {
        Product created=productService.createProduct(
                request.getSku(),
                request.getName(),
                request.getStock()
        );
        URI location=UriComponentsBuilder
                .fromPath("/products")
                .pathSegment("{sku}")
                .encode()
                .buildAndExpand(created.getSku())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{sku}")
    public ResponseEntity<Product> updateProduct(@PathVariable("sku") String sku,@Valid @RequestBody UpdateProductRequest request) {
        Product updated=productService.replaceProduct(
                sku,
                request.getName(),
                request.getStock()
        );
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{sku}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("sku") String sku) {
        productService.deleteProduct(sku);
        return ResponseEntity.noContent().build();
    }

}
