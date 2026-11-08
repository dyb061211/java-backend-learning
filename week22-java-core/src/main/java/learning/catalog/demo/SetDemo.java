package learning.catalog.demo;

import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

public class SetDemo {
    public static void main(String[] args) {
        Set<SkuKey> set = new HashSet<>();

        SkuKey k1 = new SkuKey("A001");
        SkuKey k2 = new SkuKey("A001");
        SkuKey k3 = new SkuKey("A002");

        System.out.println(k1 == k2);
        System.out.println(k1.equals(k2));
        System.out.println(k1.hashCode() == k2.hashCode());

        System.out.println(set.add(k1));
        System.out.println(set.add(k2));
        System.out.println(set.add(k3));

        System.out.println(set.size());

        Set<List<String>> keySet = new HashSet<>();

        List<String> key = new ArrayList<>();
        key.add("A001");

        keySet.add(key);

        System.out.println(keySet.contains(key));

        key.set(0, "A002");

        System.out.println(keySet.contains(key));
        System.out.println(keySet.size());
    }
}