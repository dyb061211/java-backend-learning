package learning.web.jdbc;

import learning.web.Week25SpringWebApplication;
import learning.web.product.ProductMapper;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import learning.web.product.Product;

public class MapperDemo {
    public static void main(String[] args) {
        try(ConfigurableApplicationContext context=new SpringApplicationBuilder(Week25SpringWebApplication.class)
                .web(WebApplicationType.NONE)
                .run(args)) {
            ProductMapper mapper=context.getBean(ProductMapper.class);
            Integer stock=mapper.findStockBySku("A001");
            System.out.println("A001 stock: " + stock);

            Product product=mapper.findProductBySku("A001");
            if (product != null) {
                System.out.println("sku: " + product.getSku());
                System.out.println("name: " + product.getName());
                System.out.println("stock: " + product.getStock());
            } else {
                System.out.println("Product not found");
            }
        }
    }
}
