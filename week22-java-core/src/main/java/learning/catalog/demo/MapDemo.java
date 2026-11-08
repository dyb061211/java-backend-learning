package learning.catalog.demo;

import java.util.Map;
import java.util.HashMap;
import learning.catalog.Product;
import java.util.LinkedHashMap;

public class MapDemo {
    public static void main(String[] args) {
//        Map<String,Product> products=new HashMap<>();
//        Product p1=new Product("A001", "键盘", 10);
//        Product p2=new Product("A002", "鼠标", 20);
//        products.put(p1.getSku(),p1);
//        products.put(p2.getSku(),p2);
//        Product found=products.get("A001");
//        System.out.println(found.getName());
//        System.out.println(products.containsKey("A003"));
//        System.out.println(products.get("A999"));
//        System.out.println(products.size());

        Map<String, Product> products = new LinkedHashMap<>();

        products.put("A001", new Product("A001", "键盘", 10));
        products.put("A002", new Product("A002", "鼠标", 20));

        for (Map.Entry<String, Product> entry : products.entrySet()) {
            String sku = entry.getKey();
            Product product = entry.getValue();

            System.out.println(sku + " → " + product.getName());
        }
    }
}
