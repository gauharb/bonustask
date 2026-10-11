import java.util.function.IntUnaryOperator;

public class HashTable {
    private static class Entry {
        final int key;
        int value;
        Entry next;

        Entry(int key, int value, Entry next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private static final double MAX_LOAD = 0.75;

    private Entry[] buckets;
    private int size;
    private final IntUnaryOperator hashFn;

    public HashTable() {
        this(8, HashTable::defaultHash);
    }

    public HashTable(int capacity, IntUnaryOperator hashFn) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        if (hashFn == null) {
            throw new IllegalArgumentException("Hash function must not be null");
        }
        this.buckets = new Entry[capacity];
        this.hashFn = hashFn;
    }

    private static int defaultHash(int key) {
        int h = key * 0x9E3779B9;
        return h ^ (h >>> 16);
    }

    private int index(int key, int capacity) {
        return Math.floorMod(hashFn.applyAsInt(key), capacity);
    }

    private void resize(int newCapacity) {
        Entry[] old = buckets;
        Entry[] fresh = new Entry[newCapacity];
        for (Entry head : old) {
            Entry e = head;
            while (e != null) {
                Entry next = e.next;
                int i = index(e.key, newCapacity);
                e.next = fresh[i];
                fresh[i] = e;
                e = next;
            }
        }
        buckets = fresh;
    }

    public void insert(int key, int value) {
        int i = index(key, buckets.length);
        for (Entry e = buckets[i]; e != null; e = e.next) {
            if (e.key == key) {
                e.value = value;
                return;
            }
        }
        buckets[i] = new Entry(key, value, buckets[i]);
        size++;
        if ((double) size / buckets.length > MAX_LOAD) {
            resize(buckets.length * 2);
        }
    }

    public Integer search(int key) {
        for (Entry e = buckets[index(key, buckets.length)]; e != null; e = e.next) {
            if (e.key == key) return e.value;
        }
        return null;
    }

    public boolean contains(int key) {
        return search(key) != null;
    }

    public boolean delete(int key) {
        int i = index(key, buckets.length);
        Entry prev = null;
        for (Entry e = buckets[i]; e != null; prev = e, e = e.next) {
            if (e.key == key) {
                if (prev == null) buckets[i] = e.next;
                else prev.next = e.next;
                size--;
                return true;
            }
        }
        return false;
    }

    public int longestChain() {
        int max = 0;
        for (Entry head : buckets) {
            int len = 0;
            for (Entry e = head; e != null; e = e.next) len++;
            max = Math.max(max, len);
        }
        return max;
    }

    public int size() {
        return size;
    }
}

