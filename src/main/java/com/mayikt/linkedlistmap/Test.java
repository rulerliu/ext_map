package com.mayikt.linkedlistmap;

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

        // 两个对象的hashCode相同, equals不一定为true
        // 但是equals为true, 那么hashCode一定相同
        Integer a = 97;
        String b = "a";
        System.out.println(a.hashCode());
        System.out.println(b.hashCode());
        System.out.println(a.equals(b));
        System.out.println(b.equals(a));

        ExtMap map = new ExtLinkedListHashMap<>();
        map.put("a", 1);
        map.put("b", 2);
        map.put("a", 3);
        System.out.println(map);
    }

}
