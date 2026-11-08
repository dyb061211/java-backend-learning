package learning.catalog.demo;

import java.util.List;
import learning.catalog.Product;

public class GenericMethodDemo {
    public static <T> T getFirst(List<T>list,T defaultValue) {
        if(list.isEmpty()) {
            return defaultValue;
        }
        return list.get(0);
    }

    public static void main(String[] args) {
        List<String> names = List.of("键盘", "鼠标");

        String name = getFirst(names, "未知");
        System.out.println(name);

        List<Integer> numbers = List.of(10, 20, 30);

        Integer number = getFirst(numbers, 0);
        System.out.println(number);

        List<Product> products = List.of(
                new Product("A001", "显示器", 5)
        );

        Product product = getFirst(products, null);
        System.out.println(product.getName());

        List<String> empty = List.of();

        System.out.println(getFirst(empty, "默认名称"));
    }
}
