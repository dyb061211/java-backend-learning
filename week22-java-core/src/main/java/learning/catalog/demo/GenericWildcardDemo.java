package learning.catalog.demo;

import java.util.ArrayList;
import java.util.List;

public class GenericWildcardDemo {
    public static void main(String[] args) {
        // extends：主要用于读取
        List<Integer> integers =
                new ArrayList<>(List.of(10, 20));

        List<? extends Number> reader = integers;

        Number first = reader.get(0);

        System.out.println(first);

        // super：主要用于写入
        List<Number> numbers = new ArrayList<>();

        List<? super Integer> writer = numbers;

        writer.add(30);
        writer.add(40);

        Object result = writer.get(0);

        System.out.println(result);
        System.out.println(numbers.size());

        // reader.add(50);       // 编译错误
        // writer.add(3.14);     // 编译错误
        // Integer n = writer.get(0); // 编译错误

        List<String> names = new ArrayList<>();
        List<Integer> nums = new ArrayList<>();

        System.out.println(
                names.getClass() == nums.getClass()
        );
        System.out.println(names.getClass());
    }

}
