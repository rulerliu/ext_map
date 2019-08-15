package com.mayikt.linkedlistmap;

import com.mayikt.ExtMap;

import java.util.LinkedList;
import java.util.List;

/**
 * @description:
 * @author: liuwq
 * @date: 2019/8/15 0015 下午 2:58
 * @version: V1.0
 */
public class ExtLinkedListHashMap<K, V> implements ExtMap<K, V> {

    private List<Node>[] tables = new LinkedList[100];

    @Override
    public V put(K key, V value) {
        // 计算key的hashCode
        int hash = hash(key);
        // 从tables中获取LinkedList（链表）
        List<Node> table = tables[hash];
        if (table == null) {
            table = new LinkedList<>();
            table.add(new Node(key, value));
            tables[hash] = table;
            return value;
        }

        for (Node node : table) {
            if (node.getKey().equals(key)) {
                node.setValue(value);
                return value;
            }
        }

        table.add(new Node(key, value));
        return value;
    }

    private int hash(K key) {
        return key.hashCode() % tables.length;
    }

    @Override
    public V get(K key) {
        int hash = hash(key);
        List<Node> table = tables[hash];
        if (table == null) {
            return null;
        }

        for (Node node : table) {
            if (node.getKey().equals(key)) {
                return (V) node.getValue();
            }
        }
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    static class Node<K,V> implements ExtMap.Entry<K,V> {
        final K key;
        V value;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public final K getKey() {
            return key;
        }

        public final V getValue() {
            return value;
        }

        public final String toString() {
            return key + "=" + value;
        }

        public final V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }

    }

}
