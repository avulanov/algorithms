import java.util.Iterator;

import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] data = null;
    private int n = 0;
    public RandomizedQueue() {
        // construct an empty randomized queue
        data = (Item[]) new Object[2];
    }
    public boolean isEmpty() {
        // is the queue empty?
        return (n == 0);
    }
    public int size() {
        // return the number of items on the queue
        return n;
    }
    public void enqueue(Item item) throws java.lang.NullPointerException {
        // add the item
        if (item == null) throw new NullPointerException("Null item");
        // double the queue
        if (n == data.length) {
            Item[] newData = (Item[]) new Object[2 * n];
            for (int i = 0; i < n; i++) {
                newData[i] = data[i];
            }
            data = newData;
        }
        data[n] = item;
        n += 1;
    }
    public Item dequeue() throws java.util.NoSuchElementException {
        // remove and return a random item
        if (isEmpty()) throw new java.util.NoSuchElementException("Queue is empty");
        // reduce the queue
        if (n == data.length / 4) {
            Item[] newData = (Item[]) new Object[n * 2];
            for (int i = 0; i < n; i++) {
                newData[i] = data[i];
            }
            data = newData;            
        }
        int index = StdRandom.uniform(n);
        Item returningItem = data[index];
        n -= 1;
        data[index] = data[n];
        data[n] = null;
        return returningItem;
    }
    public Item sample() {
        // return (but do not remove) a random item
        int index = StdRandom.uniform(n);
        return data[index];
    }
    public Iterator<Item> iterator() {
        // return an independent iterator over items in random order
        return new RandomizedQueueIterator();
    }
    private class RandomizedQueueIterator implements Iterator<Item> {
        private int[] order = new int[n];
        private int current = 0;
        public RandomizedQueueIterator() {
            for (int i = 0; i < n; i++) {
                order[i] = i;
            }
            for (int i = 0; i < n; i++) {
                int p = StdRandom.uniform(n);
                int q = StdRandom.uniform(n);
                int temp = order[p];
                order[p] = order[q];
                order[q] = temp;
            }            
        }
        public boolean hasNext() { 
            return (current < n); 
        }
        public void remove() throws java.lang.UnsupportedOperationException {
        /* not supported */ 
            throw new UnsupportedOperationException("Unsupported");
        }
        public Item next() throws java.util.NoSuchElementException {
            if (!hasNext()) throw new java.util.NoSuchElementException("Empty");
            Item returningItem = data[order[current]];
            current += 1;
            return returningItem;
        }
    }
    public static void main(String[] args) {
        // unit testing
    }
}