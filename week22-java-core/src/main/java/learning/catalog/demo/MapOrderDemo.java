package learning.catalog.demo;

import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class MapOrderDemo {
    public static void main(String[] args) {
        Map<String,Integer> hashMap=new HashMap<>();
        Map<String,Integer> linkedMap=new LinkedHashMap<>();
        String[] skus = {"A003", "A001", "A002"};
        for (String sku :skus) {
            hashMap.put(sku,10);
            linkedMap.put(sku,10);
        }
        System.out.println("hashMap:");
        System.out.println(hashMap.keySet());
        System.out.println("linkedMap:");
        System.out.println(linkedMap.keySet());
    }
}
