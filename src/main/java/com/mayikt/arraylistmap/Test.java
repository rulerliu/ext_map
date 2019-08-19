package com.mayikt.arraylistmap;

import com.mayikt.ExtMap;

import java.util.HashMap;

/**
 * @description:
 * @author: liuwq
 * @date: 2019/8/15 0015 上午 9:26
 * @version: V1.0
 */
public class Test {

    public static void main(String[] args) {
        HashMap<String, Object> m = new HashMap<>();

        ExtMap map = new ExtArrayListHashMap<>();
        map.put("a", 1);
        map.put("b", 2);
        map.put("a", 3);
        System.out.println(map.get("a"));
        System.out.println(map.get("b"));
        System.out.println(map.get("c"));

        Integer a = 128;
        Integer b = 128;
        System.out.println(a == b);
        System.out.println(a.equals(b));

        Integer c = 127;
        Integer d = 127;
        System.out.println(c == d);
        System.out.println(c.equals(d));
    }

}
