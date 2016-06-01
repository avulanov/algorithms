import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
    private Node first = null;
    private Node last = first;
    private int mySize = 0;
    private class Node {
        public Node prev = null;
        public Node next = null;
        public Item item = null;
    }
    public Deque() {
        // construct an empty deque
    }
    public boolean isEmpty() {
        // is the deque empty?
        return first == null;
    }
    public int size() {
        // return the number of items on the deque
        return mySize;
    }
    public void addFirst(Item item) throws java.lang.NullPointerException {
        // add the item to the front
        if (item == null) throw new NullPointerException("Cannot add null");
        Node oldFirst = first;
        Node newNode = new Node();
        newNode.prev = null;
        newNode.next = oldFirst;
        newNode.item = item;
        if (oldFirst == null) {
            last = newNode;
        } else {
            oldFirst.prev = newNode;
        }
        first = newNode;
        mySize += 1;
    }
    public void addLast(Item item) throws java.lang.NullPointerException {
        // add the item to the end
        if (item == null) throw new NullPointerException("Cannot add null");
        Node oldLast = last;
        Node newNode = new Node();
        newNode.prev = oldLast;
        newNode.next = null;
        newNode.item = item;
        if (oldLast == null) { 
            first = newNode; 
        } else {
            oldLast.next = newNode;
        }
        last = newNode;
        mySize += 1;
    }
    public Item removeFirst() throws java.util.NoSuchElementException {
        // remove and return the item from the front
        if (first == null) throw new java.util.NoSuchElementException("Empty deque");
        Item returningItem = first.item;
        first = first.next;
        if (first == null) {
            last = null;
        } else {
            first.prev = null;
        }
        mySize -= 1;
        return returningItem;
    }
    public Item removeLast() throws java.util.NoSuchElementException {
        // remove and return the item from the end
        if (last == null) throw new java.util.NoSuchElementException("Empty deque");
        Item returningItem = last.item;
        last = last.prev;
        if (last == null) {
            first = null;
        } else {
            last.next = null;
        }
        mySize -= 1;
        return returningItem;
    }
    public Iterator<Item> iterator() { 
        return new DequeIterator(); 
    }
    private class DequeIterator implements Iterator<Item> {
        private Node current = first;
        public boolean hasNext() { 
            return current != null; 
        }
        public void remove() throws java.lang.UnsupportedOperationException {
        /* not supported */ 
            throw new UnsupportedOperationException("Unsupported");
        }
        public Item next() throws java.util.NoSuchElementException {
            if (!hasNext()) throw new java.util.NoSuchElementException("Empty");
            Item item = current.item;
            current = current.next;
            return item;
        }
    }
    public static void main(String[] args) {
    }
}