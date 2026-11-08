package learning.catalog.demo;

import learning.catalog.Product;
import java.util.List;
import java.util.ArrayList;

public class GenericBasicsDemo {
    public static void main(String[] args) {
        List<Product> products=new ArrayList<>();
        products.add(new Product("A001", "键盘", 10));

        Product p = products.get(0);
        System.out.println(p.getName());

        List<Object> mixed=new ArrayList<>();
        mixed.add("A002");
        mixed.add(new Product("A003", "鼠标", 5));
        Object value = mixed.get(0);

        System.out.println(value.getClass().getSimpleName());

        // products.add("A004");
    }
}
