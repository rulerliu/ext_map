package com.mayikt.hashmap7;

import com.mayikt.ExtMap;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: liuwq
 * @date: 2019/8/26 0026 下午 3:15
 * @version: V1.0
 */
public class Test {

    public static void main(String[] args) {
        Map<String, Object> m = new HashMap<>();
        m.put("a", 1);

        String a = "a";
        Integer b = 97;
        System.out.println(a.hashCode());
        System.out.println(b.hashCode());

        ExtMap<Object, Object> map = new ExtHashMap<>();
        map.put(a, 1);
        map.put(b, 2);
        map.put(null, 3);
        System.out.println(map.get(a));
        System.out.println(map.get(b));
        System.out.println(map.get(null));


        ExtMap<Object, Object> map2 = new ExtHashMap<>();
        for (int i = 0; i < 12; i++) {
            map2.put(i, i);
        }
        map2.put("A", "A");
        System.out.println(map2);
        System.out.println(map2.size());
    }

}
