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
    }

}
