package learning.catalog.demo;

import learning.catalog.Product;
import learning.catalog.ProductCatalog;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.util.Optional;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProductStatisticsDemo {
    private List<Product> lowStockProducts=new ArrayList<>();
    private List<String> names=new ArrayList<>();

    public static void main(String[] args) {
        ProductCatalog catalog = new ProductCatalog();

        catalog.addProduct(new Product("A004", "鼠标垫", 3));
        catalog.addProduct(new Product("A001", "键盘", 10));
        catalog.addProduct(new Product("A003", "显示器", 3));
        catalog.addProduct(new Product("A002", "鼠标", 0));

        ProductStatisticsDemo statistics = new ProductStatisticsDemo();

        statistics.get_lowStockProducts(3,catalog);
        statistics.get_names();
        System.out.println(statistics.names);
        long sum=statistics.get_sum_low_stock();
        System.out.println(sum);
        long total = statistics.getTotalStock(catalog);
        System.out.println("全部库存：" + total);

        List<Product>sorted=catalog.listProducts();
        Comparator<Product> comparator=(a,b)->{
            int res=Integer.compare(a.getStock(),b.getStock());
            if(res!=0)  return res;
            return a.getSku().compareTo(b.getSku());
        };
        sorted.sort(comparator);
        for (Product p : sorted) {
            System.out.println(p.getSku() + " : " + p.getStock());
        }

        List<String> lowStockNames=catalog.listProducts()
                .stream()
                .filter(p->p.getStock()<=3)
                .map(Product::getName)
                .toList();
        System.out.println(lowStockNames);

        List<Product> lowestTwo= catalog.listProducts()
                .stream()
                .sorted(
                        (a,b)-> {
                            int res = Integer.compare(a.getStock(), b.getStock());
                            if (res != 0) return res;
                            return a.getSku().compareTo(b.getSku());
                        }
                )
                .limit(2)
                .toList();
        System.out.println(lowestTwo);

        long lowStockCount=catalog.listProducts()
                .stream()
                .filter(p->p.getStock()<=3)
                .count();

        long streamTotal=catalog.listProducts()
                .stream()
                .mapToLong(Product::getStock)
                .sum();
        System.out.println("低库存商品数量：" + lowStockCount);
        System.out.println("Stream库存总量：" + streamTotal);

        Optional<Product>found=catalog.listProducts()
                .stream()
                .filter(p->p.getStock()==0)
                .findFirst();
        System.out.println(found.orElseThrow().getSku());

        Optional<Product>missing=catalog.listProducts()
                .stream()
                .filter(p->p.getStock()>100)
                .findFirst();
        System.out.println(
                missing.map(Product::getName).orElse("未找到")
        );

        Map<Boolean,Long> stockCounts=catalog.listProducts()
                .stream()
                .collect(
                        Collectors.partitioningBy(
                                p->p.getStock()>0,
                                Collectors.counting()
                        )
                );
        System.out.println(stockCounts.get(true));
        System.out.println(stockCounts.get(false));

        Stream<Product> stream=catalog.listProducts().stream();
        System.out.println("第一次：" + stream.count());
        try{
            System.out.println("第二次：" + stream.count());
        }
        catch(IllegalStateException e){
            System.out.println("Stream不能重复消费：" + e.getClass().getSimpleName());
        }
    }

    void get_lowStockProducts(int threshold,ProductCatalog catalog) {
        for(Product product:catalog.listProducts()) {
            if(product.getStock()<=threshold) {
                lowStockProducts.add(product);
            }
        }
    }
    void get_names() {
        for(Product product:lowStockProducts) {
            names.add(product.getName());
        }
    }

    long get_sum_low_stock() {
        long sum=0;
        for(Product product:lowStockProducts) {
            sum+=product.getStock();
        }
        return sum;
    }

    long getTotalStock(ProductCatalog catalog) {
        long sum = 0;
        for (Product product : catalog.listProducts()) {
            sum += product.getStock();
        }
        return sum;
    }
}
