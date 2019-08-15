package com.mayikt;

public interface ExtMap<K, V> {

    V put(K key, V value);

    V get(K key);

    int size();

    boolean isEmpty();

    interface Entry<K, V> {
        // 获取key
        K getKey();

        // 获取value
        V getValue();

        // 获取value
        V setValue(V value);
    }

}
