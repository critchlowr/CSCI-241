package heap;
/*
 * Author: Ryan Critchlow
 * Date: 03/03/23
 * Purpose: A3 Assignment for CSCI 241
 */

import java.util.NoSuchElementException;

/**
 * An instance is a min-heap of distinct values of type V with
 * priorities of type P. Since it's a min-heap, the value
 * with the smallest priority is at the root of the heap.
 */
public final class Heap<V, P extends Comparable<P>> {

    /**
     * The contents of c represent a complete binary tree. We use square-bracket
     * shorthand to denote indexing into the AList (which is actually
     * accomplished using its get method. In the complete tree,
     * c[0] is the root; c[2i+1] is the left child of c[i] and c[2i+2] is the
     * right child of i.  If c[i] is not the root, then c[(i-1)/2] (using
     * integer division) is the parent of c[i].
     * <p>
     * Class Invariants:
     * <p>
     * The tree is complete:
     * 1. `c[0..c.size()-1]` are non-null
     * <p>
     * The tree satisfies the heap property:
     * 2. if `c[i]` has a parent, then `c[i]`'s parent's priority
     * is smaller than `c[i]`'s priority
     * <p>
     * In Phase 3, the following class invariant also must be maintained:
     * 3. The tree cannot contain duplicate *values*; note that dupliate
     * *priorities* are still allowed.
     * 4. map contains one entry for each element of the heap, so
     * map.size() == c.size()
     * 5. For each value v in the heap, its map entry contains in the
     * the index of v in c. Thus: map.get(c[i]) = i.
     */
    protected AList<Entry> c;
    protected HashTable<V, Integer> map;

    /**
     * Constructor: an empty heap with capacity 10.
     */
    public Heap() {
        c = new AList<Entry>(10);
        map = new HashTable<V, Integer>();
    }

    /**
     * An Entry contains a value and a priority.
     */
    class Entry {
        public V value;
        public P priority;

        /**
         * An Entry with value v and priority p
         */
        Entry(V v, P p) {
            value = v;
            priority = p;
        }

        public String toString() {
            return value.toString();
        }
    }

    /**
     * Add v with priority p to the heap.
     * The expected time is logarithmic and the worst-case time is linear
     * in the size of the heap. Precondition: p is not null.
     * In Phase 3 only:
     *
     * @throws IllegalArgumentException if v is already in the heap.
     */
    public void add(V v, P p) throws IllegalArgumentException {
        // invariant 3 - no two same key, priorities are allowed
        if (map.containsKey(v)) {
            throw new IllegalArgumentException(v + "value for v is already in heap");
        } else {
            //invariant 4 - 5
            map.put(v, size());
            // phase 1
            c.append(new Entry(v, p));
            bubbleUp(size()-1);
        }
    }

    /**
     * Return the number of values in this heap.
     * This operation takes constant time.
     */
    public int size() {
        return c.size();
    }

    /**
     * Swap c[h] and c[k].
     * precondition: h and k are >= 0 and < c.size()
     */
    protected void swap(int h, int k) {
        //copy c[h]
        Heap.Entry temp = c.get(h);
        //swap the two values
        c.put(h, c.get(k));
        c.put(k, temp);
        // invariants 3-5 by updating the map field.
        // (put the value from c.c[k] as key, k as priority)
        map.put(c.get(k).value, k);
        map.put(c.get(h).value, h);
    }

    /**
     * Bubble c[k] up in heap to its right place.
     * Precondition: Priority of every c[i] >= its parent's priority
     * except perhaps for c[k]
     */
    protected void bubbleUp(int k) {
        while (k > 0) {
            if (compareTwoNodes(k, kaysParent(k)) >= 0) {
                return;
            } else {
                swap(kaysParent(k), k);
                k = kaysParent(k);
            }
        }
    }
    // return the parent of a node
    private int kaysParent(int k) {
        return (k - 1) / 2;
    }

    /**
     * Return the value of this heap with lowest priority. Do not
     * change the heap. This operation takes constant time.
     *
     * @throws NoSuchElementException if the heap is empty.
     */
    public V peek() throws NoSuchElementException {
        if (c.size() == 0) {
            throw new NoSuchElementException();
        } else {
            // lowest priority first element in heap
            return c.get(0).value;
        }
    }

    /**
     * Remove and return the element of this heap with lowest priority.
     * The expected time is logarithmic and the worst-case time is linear
     * in the size of the heap.
     *
     * @throws NoSuchElementException if the heap is empty.
     */
    public V poll() throws NoSuchElementException {
        if (c.size() == 0) {
            throw new NoSuchElementException();
        } else {
            swap(0, c.size() - 1);
            // remove value and store in popped
            V popped = c.pop().value;
            // take it out of the map
            map.remove(popped);
            bubbleDown(0);
            return popped;
        }
    }

    /**
     * Bubble c[k] down in heap until it finds the right place.
     * If there is a choice to bubble down to both the left and
     * right children (because their priorities are equal), choose
     * the right child.
     * Precondition: Each c[i]'s priority <= its childrens' priorities
     * except perhaps for c[k]
     */
    protected void bubbleDown(int k) {
        while (leftChild(k) < c.size()) {
            int smChild = smallerChild(k);
            if (compareTwoNodes(smChild, k) >= 0) {
                return;
            }
            swap(k, smChild);
            k = smChild;
        }
    }

    private int leftChild(int k) {
        return 2 * k + 1;
    }

    private int rightChild(int k) {
        return 2 * k + 2;
    }
    // created to make if statements smaller in parent methods
    // checks the priority between two nodes
    private int compareTwoNodes(int i, int j) {
        return (c.get(i).priority.compareTo(c.get(j).priority));
    }

    /**
     * Return true if the value v is in the heap, false otherwise.
     * The average case runtime is O(1).
     */
    public boolean contains(V v) {
        return map.containsKey(v);
    }

    /**
     * Change the priority of value v to p.
     * The expected time is logarithmic and the worst-case time is linear
     * in the size of the heap.
     *
     * @throws IllegalArgumentException if v is not in the heap.
     */
    public void changePriority(V v, P p) throws IllegalArgumentException {
        if (map.containsKey(v) == false) {
            throw new IllegalArgumentException(v + " value for v is not in heap");
        } else {
            int index = map.get(v);
            // set priority of index to new pri
            c.get(index).priority = p;
            // bubble down to position
            bubbleDown(index);
            // bubbleup as needed
            bubbleUp(index);
        }
    }

    // Recommended helper method spec:
    /* Return the index of the child of k with smaller priority.
     * if only one child exists, return that child's index
     * Precondition: at least one child exists.*/
    private int smallerChild (int k) {
        if (rightChild(k) < c.size()) {
            if (compareTwoNodes(leftChild(k), rightChild(k)) < 0) {
                return  leftChild(k);
            } else {
                return rightChild(k);
            }
        }
        if (leftChild(k) < c.size()) {
            return leftChild(k);
        } else {
            return -1;
        }
    }
}
