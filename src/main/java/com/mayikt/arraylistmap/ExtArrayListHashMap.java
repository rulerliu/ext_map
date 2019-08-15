package com.mayikt.arraylistmap;

import com.mayikt.ExtMap;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: liuwq
 * @date: 2019/8/15 0015 上午 11:18
 * @version: V1.0
 */
public class ExtArrayListHashMap<K, V> implements ExtMap<K, V> {
    private List<Node> listEntrys = new ArrayList<Node>();

    @Override
    public V put(K key, V value) {
        for (Node listEntry : listEntrys) {
            if (listEntry.getKey().equals(key)) {
                listEntry.setValue(value);
                return value;
            }
        }

        Node<K, V> node = new Node<>(key, value);
        listEntrys.add(node);
        return value;
    }

    @Override
    public V get(K key) {
        for (Node listEntry : listEntrys) {
            if (listEntry.getKey().equals(key)) {
                return (V) listEntry.getValue();
            }
        }
        return null;
    }

    @Override
    public int size() {
        return listEntrys.size();
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
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
