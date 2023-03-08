package heap;

/**
 * A hash table modeled after java.util.Map. It uses chaining for collision
 * resolution and grows its underlying storage by a factor of 2 when the load
 * factor exceeds 0.8.
 */
public class HashTable<K, V> {

    protected Pair[] buckets; // array of list nodes that store K,V pairs
    protected int size; // how many items currently in the map


    /**
     * class Pair stores a key-value pair and a next pointer for chaining
     * multiple values together in the same bucket, linked-list style
     */
    public class Pair {
        protected K key;
        protected V value;
        protected Pair next;

        /**
         * constructor: sets key and value
         */
        public Pair(K k, V v) {
            key = k;
            value = v;
            next = null;
        }

        /**
         * constructor: sets key, value, and next
         */
        public Pair(K k, V v, Pair nxt) {
            key = k;
            value = v;
            next = nxt;
        }

        /**
         * returns (k, v) String representation of the pair
         */
        public String toString() {
            return "(" + key + ", " + value + ")";
        }
    }

    /**
     * constructor: initialize with default capacity 17
     */
    public HashTable() {
        this(17);
    }

    /**
     * constructor: initialize the given capacity
     */
    public HashTable(int capacity) {
        buckets = createBucketArray(capacity);
    }

    /**
     * Return the size of the map (the number of key-value mappings in the
     * table)
     */
    public int getSize() {
        return size;
    }

    /**
     * Return the current capacity of the table (the size of the buckets
     * array)
     */
    public int getCapacity() {
        return buckets.length;
    }

    /**
     * Return the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     * Runtime: average case O(1); worst case O(size)
     */
    public V get(K key) {
        int hashDex = hash(key);
        // loop node and node.next values at hashDex
        for (Pair node = buckets[hashDex]; node != null; node = node.next) {
           if (node.key == key) {
               return node.value;
           }
        }
        return null;
    }

    // hash method that uses hashCode() to get a hashCode
    // hashCode is modded by the capacity of our array to avoid index bound issues
    // abs is used to avoid a negative index
    private int hash(K key) {
        return Math.abs(key.hashCode()) % getCapacity();
    }

    /**
     * Associate the specified value with the specified key in this map. If
     * the map previously contained a mapping for the key, the old value is
     * replaced. Return the previous value associated with key, or null if
     * there was no mapping for key. If the load factor exceeds 0.8 after this
     * insertion, grow the array by a factor of two and rehash.
     * Precondition: val is not null.
     * Runtime: average case O(1); worst case O(size + a.length)
     */
    public V put(K key, V val) {
        int hashDex = hash(key);
        // check if the node exists
        // loop node and node.next values at hashDex, return old value if overwritten
        for (Pair node = buckets[hashDex]; node != null; node = node.next) {
            if (node.key == key) {
                V old = node.value;
                node.value = val;
                return old;
            }
        }
        // create a new Pair and grow if needed
        buckets[hashDex] = new Pair(key, val, buckets[hashDex]);
        size++;
        growIfNeeded();
        return null;
    }

    /**
     * Return true if this map contains a mapping for the specified key.
     * Runtime: average case O(1); worst case O(size)
     */
    public boolean containsKey(K key) {
        int hashDex = hash(key);
        // loop each node and node.next at hashDex to search for k
        for (Pair node = buckets[hashDex]; node != null; node = node.next) {
            if (node.key == key) {
                return true;
            }
        }
        return false;
    }

    /**
     * Remove the mapping for the specified key from this map if present.
     * Return the previous value associated with key, or null if there was no
     * mapping for key.
     * Runtime: average case O(1); worst case O(size)
     */
    public V remove(K key) {
        int hashDex = hash(key);
        // set first node at hashDex
        Pair specKey = buckets[hashDex];
        // loop all nodes at hashDex, initializing previous to null for chaining
        for (Pair previous = null; specKey != null; specKey = specKey.next) {
            if (specKey.key == key) {
                V result = specKey.value;
                // if previous is empty, meaning no chaining
                if (previous == null) {
                    // remove node by setting it to be next in sequence
                    buckets[hashDex] = specKey.next;
                    } else {
                        // shift nodes to the right
                        previous.next = specKey.next;
                    }
                    --size;
                    return result;
                }
            // more than one node exists at hashDex, look at the next
            previous = specKey;
        }
        // null if no mapping for specified key
        return null;
    }


    // suggested helper method:
    /* check the load factor; if it exceeds 0.8, double the array size
     * (capacity) and rehash values from the old array to the new array */
    private void growIfNeeded() {
        double loadFactor = (double)size / (double)getCapacity();
        // only grow if loadFactor is not <= 0.8
        if (loadFactor > 0.8) {
            Pair[] old = buckets;
            int length = old.length * 2;
            buckets = createBucketArray(length);
            size = 0;
            reHash(length, old);
        }
    }

    // method to re-hash() every value from old array into a new one
    private void reHash(int length, Pair[] old) {
        // for each index in old
        for (int i = 0; i < old.length; i++) {
            // associate each element in that bucket (chaining) to a new index and increment size
            for (Pair newNode = old[i]; newNode != null; size++) {
                int hashDex = hash(newNode.key);
                // if chaining exists, save .next value, null newNode.next at index
                Pair nxtNode = newNode.next;
                newNode.next = buckets[hashDex];
                // always set our node to our new one
                buckets[hashDex] = newNode;
                // increment node with our saved value
                newNode = nxtNode;
            }
        }
    }

    /* useful method for debugging - prints a representation of the current
     * state of the hash table by traversing each bucket and printing the
     * key-value pairs in linked-list representation */
    protected void dump() {
        System.out.println("Table size: " + getSize() + " capacity: " +
                getCapacity());
        for (int i = 0; i < buckets.length; i++) {
            System.out.print(i + ": --");
            Pair node = buckets[i];
            while (node != null) {
                System.out.print(">" + node + "--");
                node = node.next;

            }
            System.out.println("|");
        }
    }

    /*  Create and return a bucket array with the specified size, initializing
     *  each element of the bucket array to be an empty LinkedList of Pairs.
     *  The casting and warning suppression is necessary because generics and
     *  arrays don't play well together.*/
    @SuppressWarnings("unchecked")
    protected Pair[] createBucketArray(int size) {
        return (Pair[]) new HashTable<?, ?>.Pair[size];
    }
}
