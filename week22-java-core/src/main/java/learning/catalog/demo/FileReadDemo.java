package learning.catalog.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import learning.catalog.Product;
import learning.catalog.ProductCatalog;
import java.io.BufferedWriter;
import java.nio.file.StandardOpenOption;
import java.util.Comparator;
import java.util.List;

public class FileReadDemo {
    public static void main(String[] args) throws IOException{
        Path path=Path.of("week22-java-core","samples", "products.tsv");
        ProductCatalog catalog = loadCatalog(path);

        System.out.println("商品数量："+catalog.count());
        long totalStock=catalog.listProducts()
                .stream()
                .mapToLong(Product::getStock)
                .sum();

        long inStockCount=catalog.listProducts()
                .stream()
                .filter(p->p.getStock()>0)
                .count();

        System.out.println("库存总量：" + totalStock);
        System.out.println("有库存商品：" + inStockCount);
        System.out.println("无库存商品：" + (catalog.count() - inStockCount));

        Product found = catalog.findBySku("A001");
        Product missing = catalog.findBySku("A999");

        System.out.println(found.getName());
        if(missing==null) {
            System.out.println("商品不存在");
        }
        Comparator<Product> comparator=(a,b)->{
            int res=Integer.compare(a.getStock(),b.getStock());
            if(res!=0) return res;
            return a.getSku().compareTo(b.getSku());
        };
        List<Product> sorted = catalog.listProducts()
                .stream()
                .sorted(comparator)
                .toList();

        for(Product product:sorted) {
            System.out.println(
                    product.getSku() + " | " + product.getStock()
            );
        }
        System.out.println("=====================");

        List<Product> lowStock3 = getLowStock(sorted, 3);
        for(Product product:lowStock3) {
            System.out.println(product.getSku() + " | " + product.getStock());
        }
        System.out.println("=====================");
        List<Product> lowStock0 = getLowStock(sorted, 0);
        for(Product product:lowStock0) {
            System.out.println(product.getSku() + " | " + product.getStock());
        }

        Path output = Path.of(
                "week22-java-core",
                "target",
                "day7-output",
                "products-final.tsv"
        );

        exportCatalog(catalog, output);

        System.out.println("导出成功：" + output.toAbsolutePath());

        ProductCatalog reloaded = loadCatalog(output);

        System.out.println("回读商品数量：" + reloaded.count());

        long reloadedTotal = reloaded.listProducts()
                .stream()
                .mapToLong(Product::getStock)
                .sum();

        System.out.println("回读库存总量：" + reloadedTotal);

        for (Product p : reloaded.listProducts()) {
            System.out.println(
                    p.getSku() + " | "
                            + p.getName() + " | "
                            + p.getStock()
            );
        }
    }

    public static ProductCatalog loadCatalog(Path path) throws IOException{
        ProductCatalog catalog = new ProductCatalog();
        try(BufferedReader reader=Files.newBufferedReader(path,StandardCharsets.UTF_8)) {
            String line;
            int lineNumber=0;
            while((line=reader.readLine())!=null) {
                lineNumber++;
                if(lineNumber==1) {
                    if(!line.equals("sku\tname\tstock")) {
                        throw new IllegalArgumentException("文件表头错误");
                    }
                    continue;
                }
                String[] fields=line.split("\t",-1);
                if (fields.length != 3) {
                    throw new IllegalArgumentException(
                            "第" + lineNumber + "行：列数不是3"
                    );
                }
                String sku=fields[0];
                String name=fields[1];
                int stock;
                try{
                    stock=Integer.parseInt(fields[2]);
                }
                catch(NumberFormatException e) {
                    throw new IllegalArgumentException(
                            "第" + lineNumber + "行：库存不是整数", e
                    );
                }
                try{
                    Product product=new Product(sku,name,stock);
                    catalog.addProduct(product);
                }
                catch(IllegalArgumentException e) {
                    throw new IllegalArgumentException(
                            path + " 第" + lineNumber + "行：" + e.getMessage(),
                            e
                    );
                }
            }
            if (lineNumber == 0) {
                throw new IllegalArgumentException(
                        path + "：文件为空，缺少表头"
                );
            }
        }
        return catalog;
    }

    public static void exportCatalog(ProductCatalog catalog,Path output) throws IOException{
        List<Product>products=catalog.listProducts();
        Comparator<Product> comparator=(a,b)->{
            int res=Integer.compare(a.getStock(),b.getStock());
            if(res!=0) return res;
            return a.getSku().compareTo(b.getSku());
        };
        products.sort(comparator);
        Files.createDirectories(output.getParent());
        try(
                BufferedWriter writer=Files.newBufferedWriter(
                        output,
                        StandardCharsets.UTF_8,
                        StandardOpenOption.CREATE_NEW,
                        StandardOpenOption.WRITE
                )
        ){
            writer.write("sku\tname\tstock");
            writer.newLine();
            for(Product p:products) {
                writer.write(p.getSku()+"\t"+p.getName()+"\t"+p.getStock());
                writer.newLine();
            }
        }
    }

    public static List<Product> getLowStock(List<Product>sortedProducts,int threshold) {
        List<Product> lowStock=sortedProducts
                .stream()
                .filter(p->p.getStock()<=threshold)
                .toList();
        return lowStock;
    }
}
