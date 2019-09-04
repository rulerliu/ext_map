package com.mayikt.hashmap7;

import com.mayikt.ExtMap;

import java.util.Map;
import java.util.Objects;

/**
 * @description:
 * @author: liuwq
 * @date: 2019/8/26 0026 下午 3:14
 * @version: V1.0
 */
public class ExtHashMap<K, V> implements ExtMap<K, V> {

    /**
     * 初始化长度
     */
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // aka 16

    /**
     * 默认的扩容因子（map容量达到3/4的长度时，进行扩容）
     */
    static final float DEFAULT_LOAD_FACTOR = 0.75f;

    /**
     * map的最大长度
     */
    static final int MAXIMUM_CAPACITY = 1 << 30;

    /**
     * 扩容因子
     */
    final float loadFactor;

    /**
     * The number of key-value mappings contained in this map.
     */
    transient int size;

    /**
     * 扩容阀值（capacity * loadFactor）
     */
    int threshold;

    /**
     * An empty table instance to share when the table is not inflated.
     */
    static final Entry<?, ?>[] EMPTY_TABLE = {};

    /**
     * The table, resized as necessary. Length MUST Always be a power of two.
     */
    transient Entry<K, V>[] table = (Entry<K, V>[]) EMPTY_TABLE;

    transient int hashSeed = 0;

    transient int modCount;

    public ExtHashMap() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    /**
     * 默认初始化容量16，加载因子0.75f
     *
     * @param initialCapacity
     * @param loadFactor
     */
    public ExtHashMap(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illegal initial capacity: " +
                    initialCapacity);
        }

        if (initialCapacity > MAXIMUM_CAPACITY) {
            initialCapacity = MAXIMUM_CAPACITY;
        }

        if (loadFactor <= 0 || Float.isNaN(loadFactor)) {
            throw new IllegalArgumentException("Illegal load factor: " +
                    loadFactor);
        }

        this.loadFactor = loadFactor;
        threshold = initialCapacity;
        init();
    }

    /**
     * 给子类实现，用于初始化
     */
    void init() {
    }

    @Override
    public V put(K key, V value) {
        // 如果map是空的，进行初始化
        if (table == EMPTY_TABLE) {
            inflateTable(threshold);
        }
        // 如果map的key是null，就放到map的第一个位置
        if (key == null) {
            return putForNullKey(value);
        }

        // 计算key的hash值
        int hash = hash(key);
        // 根据hash值计算存放到数组的下标
        int index = indexFor(hash, table.length);

        for (Entry<K, V> entry = table[index]; entry != null; entry = entry.next) {
            // map中当前下标有链表，并且有这个key，进行修改
            if (entry.hash == hash && (entry.getKey().equals(key) || entry.getKey() == key)) {
                V oldValue = entry.getValue();
                entry.setValue(value);
                entry.recordAccess(this);
                return oldValue;
            }
        }

        modCount++;
        // map中
        addEntry(hash, key, value, index);
        return null;

    }

    void addEntry(int hash, K key, V value, int bucketIndex) {
        // 如果size>闸值12，扩容2倍的容量
        if ((size >= threshold) && (null != table[bucketIndex])) {
            resize(2 * table.length);
            hash = (null != key) ? hash(key) : 0;
            bucketIndex = indexFor(hash, table.length);
        }

        createEntry(hash, key, value, bucketIndex);
    }

    void resize(int newCapacity) {
        System.out.println("newCapacity:" + newCapacity);
        // 获取原来的table
        Entry[] oldTable = table;
        int oldCapacity = oldTable.length;
        if (oldCapacity == MAXIMUM_CAPACITY) {
            threshold = Integer.MAX_VALUE;
            return;
        }

        // 创建新的容量的数组
        Entry[] newTable = new Entry[newCapacity];
        // 重新计算新的index，放入到新的数组中
        transfer(newTable, false);
        table = newTable;
        threshold = (int) Math.min(newCapacity * loadFactor, MAXIMUM_CAPACITY + 1);
    }

    void transfer(Entry[] newTable, boolean rehash) {
        int newCapacity = newTable.length;
        for (Entry<K, V> e : table) {
            while (null != e) {
                Entry<K, V> next = e.next;
                if (rehash) {
                    e.hash = null == e.key ? 0 : hash(e.key);
                }
                // 重新计算新的index
                int newIndex = indexFor(e.hash, newCapacity);
                e.next = newTable[newIndex];
                newTable[newIndex] = e;
                e = next;
            }
        }
    }

    /*final boolean initHashSeedAsNeeded(int capacity) {
        boolean currentAltHashing = hashSeed != 0;
        boolean useAltHashing = sun.misc.VM.isBooted() &&
                (capacity >= HashMap.Holder.ALTERNATIVE_HASHING_THRESHOLD);
        boolean switching = currentAltHashing ^ useAltHashing;
        if (switching) {
            hashSeed = useAltHashing
                    ? sun.misc.Hashing.randomHashSeed(this)
                    : 0;
        }
        return switching;
    }*/

    /**
     * 这里是单向链表
     *
     * @param hash
     * @param key
     * @param value
     * @param bucketIndex
     */
    void createEntry(int hash, K key, V value, int bucketIndex) {
        // 获取链表的第一个元素
        Entry<K, V> next = table[bucketIndex];
        // 把新添加的Entry放到链表的第一个位置
        table[bucketIndex] = new Entry<>(hash, key, value, next);
        size++;
    }

    private void inflateTable(int toSize) {
        // 计算长度
        int capacity = roundUpToPowerOf2(toSize);

        // 计算阀值
        threshold = (int) Math.min(capacity * loadFactor, MAXIMUM_CAPACITY + 1);
        // 初始化数组
        table = new Entry[capacity];
//        initHashSeedAsNeeded(capacity);
    }

    private V putForNullKey(V value) {
        for (Entry<K, V> e = table[0]; e != null; e = e.next) {
            if (e.key == null) {
                V oldValue = e.value;
                e.value = value;
                e.recordAccess(this);
                return oldValue;
            }
        }
        modCount++;
        addEntry(0, null, value, 0);
        return null;
    }

    final int hash(Object k) {
        int h = hashSeed;
        if (0 != h && k instanceof String) {
            return sun.misc.Hashing.stringHash32((String) k);
        }

        h ^= k.hashCode();

        // This function ensures that hashCodes that differ only by
        // constant multiples at each bit position have a bounded
        // number of collisions (approximately 8 at default load factor).
        h ^= (h >>> 20) ^ (h >>> 12);
        return h ^ (h >>> 7) ^ (h >>> 4);
    }

    /**
     * Returns index for hash code h.
     */
    static int indexFor(int h, int length) {
        // assert Integer.bitCount(length) == 1 : "length must be a non-zero power of 2";
        return h & (length - 1);
    }

    private static int roundUpToPowerOf2(int number) {
        // assert number >= 0 : "number must be non-negative";
        return number >= MAXIMUM_CAPACITY
                ? MAXIMUM_CAPACITY
                : (number > 1) ? Integer.highestOneBit((number - 1) << 1) : 1;
    }

    @Override
    public V get(K key) {
        if (key == null) {
            return getForNullKey();
        }
        Entry<K, V> entry = getEntry(key);

        return null == entry ? null : entry.getValue();
    }

    final Entry<K, V> getEntry(Object key) {
        if (size == 0) {
            return null;
        }

        // 计算hash值
        int hash = (key == null) ? 0 : hash(key);
        // 计算下标
        int index = indexFor(hash, table.length);
        for (Entry<K, V> e = table[index]; e != null; e = e.next) {
            Object k;
            if (e.hash == hash && ((k = e.key) == key || (key != null && key.equals(k)))) {
                return e;
            }
        }
        return null;
    }

    private V getForNullKey() {
        if (size == 0) {
            return null;
        }
        for (Entry<K, V> e = table[0]; e != null; e = e.next) {
            if (e.key == null) {
                return e.value;
            }
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    static class Entry<K, V> implements ExtMap.Entry<K, V> {
        final K key;
        V value;
        Entry<K, V> next;
        int hash;

        /**
         * Creates new entry.
         */
        Entry(int h, K k, V v, Entry<K, V> n) {
            value = v;
            next = n;
            key = k;
            hash = h;
        }

        public final K getKey() {
            return key;
        }

        public final V getValue() {
            return value;
        }

        public final V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }

        public final boolean equals(Object o) {
            if (!(o instanceof Map.Entry))
                return false;
            ExtMap.Entry e = (ExtMap.Entry) o;
            Object k1 = getKey();
            Object k2 = e.getKey();
            if (k1 == k2 || (k1 != null && k1.equals(k2))) {
                Object v1 = getValue();
                Object v2 = e.getValue();
                if (v1 == v2 || (v1 != null && v1.equals(v2)))
                    return true;
            }
            return false;
        }

        public final int hashCode() {
            return Objects.hashCode(getKey()) ^ Objects.hashCode(getValue());
        }

        public final String toString() {
            return getKey() + "=" + getValue();
        }

        /**
         * This method is invoked whenever the value in an entry is
         * overwritten by an invocation of put(k,v) for a key k that's already
         * in the
         */
        void recordAccess(ExtHashMap<K, V> m) {
        }

        /**
         * This method is invoked whenever the entry is
         * removed from the table.
         */
        void recordRemoval(ExtHashMap<K, V> m) {
        }
    }

    public static void main(String[] args) {
        // 16
        System.out.println(roundUpToPowerOf2(16));
    }

}
