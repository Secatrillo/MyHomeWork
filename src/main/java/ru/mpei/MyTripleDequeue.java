package ru.mpei;

import java.util.*;



public  class MyTripleDequeue<E>
        extends AbstractSequentialList<E>
        implements List<E>, Deque<E>, Containerable {

    private static final int DEFAULT_TRIPLET_CAPACITY = 5;

    private static int DEFAULT_CAPACITY = 1000;

    public MyTripleDequeue(){
    }

    public MyTripleDequeue(Collection<? extends E> c) {
        this();
        addAll(c);
    }

    public MyTripleDequeue(int nodesCap) {
        this();
        DEFAULT_CAPACITY = nodesCap;
    }


    transient Node<E> first;

    transient Node<E> last;

    transient int size;

    @Override
    public int size() {
        int size = 0;
        for (Node<E> x = first; x != null; x = x.next) {
            size+=x.elementData.size();
        }
        return size;
    }


    private void linkFirst(E e) {
        if (isLinkedListFull()) throw new ArrayStoreException("Liked list overflow");
        final Node<E> f = first;
        final Node<E> newNode = new Node<>(null, e, f,DEFAULT_TRIPLET_CAPACITY);
        first = newNode;
        if (f == null)
            last = newNode;
        else
            f.prev = newNode;
        size++;
        modCount++;
    }

    void linkLast(E e) {
        if (isLinkedListFull()) throw new ArrayStoreException("Liked list overflow");
        final Node<E> l = last;
        final Node<E> newNode = new Node<>(l, e, null,DEFAULT_TRIPLET_CAPACITY);
        last = newNode;
        if (l == null)
            first = newNode;
        else
            l.next = newNode;
        size++;
        modCount++;
    }

    void linkBefore(E e, Node<E> succ) {
        // assert succ != null;
        if (isLinkedListFull()) throw new ArrayStoreException("Liked list overflow");
        final Node<E> pred = succ.prev;
        final Node<E> newNode = new Node<>(pred, e, succ,DEFAULT_TRIPLET_CAPACITY);
        succ.prev = newNode;
        if (pred == null)
            first = newNode;
        else
            pred.next = newNode;
        size++;
        modCount++;
    }

    private E unlinkFirst(Node<E> f) {
        // assert f == first && f != null;
        final E element = f.elementData.getLast();
        final Node<E> next = f.next;
        f.elementData = null;
        f.next = null; // help GC
        first = next;
        if (next == null)
            last = null;
        else
            next.prev = null;
        size--;
        modCount++;
        return element;
    }

    private E unlinkLast(Node<E> l) {
        // assert l == last && l != null;
        final E element = l.elementData.getLast();
        final Node<E> prev = l.prev;
        l.elementData = null;
        l.prev = null; // help GC
        last = prev;
        if (prev == null)
            first = null;
        else
            prev.next = null;
        size--;
        modCount++;
        return element;
    }

    E unlink(Node<E> x) {
        // assert x != null;
        final E element = x.elementData.getLast();
        final Node<E> next = x.next;
        final Node<E> prev = x.prev;

        if (prev == null) {
            first = next;
        } else {
            prev.next = next;
            x.prev = null;
        }

        if (next == null) {
            last = prev;
        } else {
            next.prev = prev;
            x.next = null;
        }

        x.elementData = null;
        size--;
        modCount++;
        return element;
    }

    @Override
    public E getFirst() {
        final Node<E> f = first;
        if (f == null)
            throw new NoSuchElementException();
        return f.elementData.getFirst();
    }

    @Override
    public E getLast() {
        final Node<E> l = last;
        if (l == null)
            throw new NoSuchElementException();
        return l.elementData.getLast();
    }

    @Override
    public E removeFirst() {
        final Node<E> f = first;
        if (f == null)
            throw new NoSuchElementException();
        if (f.elementData.size() == 1) return unlinkFirst(f);
        else return f.elementData.removeFirst();
    }

    @Override
    public E removeLast() {
        final Node<E> l = last;
        if (l == null)
            throw new NoSuchElementException();
        if (l.elementData.size() == 1) return unlinkLast(l);
        else return l.elementData.removeLast();
    }

    @Override
    public void addFirst(E e) {
        if (e == null) throw new NullPointerException();
        final Node<E> f = first;
        if (f != null && f.elementData.size() < DEFAULT_TRIPLET_CAPACITY) f.elementData.addFirst(e);
        else linkFirst(e);
    }

    @Override
    public void addLast(E e) {
        if (e == null) throw new NullPointerException();
        final Node<E> l = last;
        if (l != null && l.elementData.size() < DEFAULT_TRIPLET_CAPACITY) l.elementData.addLast(e);
        else linkLast(e);
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

//    @Override
//    public int indexOf(Object o) {
//        int index = 0;
//        for (Node<E> x = first; x != null; x = x.next) {
//            int internalIndex = x.elementData.indexOf(o);
//            if (internalIndex>=0){
//                return index+internalIndex;
//            }
//            index+=x.elementData.size();
//        }
//        return -1;
//    }

//    @Override
//    public int lastIndexOf(Object o) {
//        int index = size();
//        for (Node<E> x = last; x != null; x = x.prev) {
//            index-=x.elementData.size();
//            int internalIndex = x.elementData.lastIndexOf(o);
//            if (internalIndex >= 0)
//                return index+internalIndex;
//        }
//        return -1;
//    }



    @Override
    public Iterator<E> descendingIterator() {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        throw new UnsupportedOperationException("Метод не реализован");
    }




    Node<E> node(int index) {
        // assert isElementIndex(index);
        Node<E> x;
        if (index < (size >> 1)) {
            x = first;
            for (int i = 0; i < index; i++)
                x = x.next;
        } else {
            x = last;
            for (int i = size - 1; i > index; i--)
                x = x.prev;
        }
        return x;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E remove() {
        return removeFirst();
    }

    @Override
    public boolean add(E e) {
        linkLast(e);
        return true;
    }

    @Override
    public boolean remove(Object o) {
        for (Node<E> x = first; x != null; x = x.next) {
            if(x.elementData.remove(o)){
                if(x.elementData.isEmpty()) unlink(x);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean offer(E e) {
        return add(e);
    }

    @Override
    public boolean offerFirst(E e) {
        addFirst(e);
        return true;
    }

    @Override
    public boolean offerLast(E e) {
        addLast(e);
        return true;
    }

    @Override
    public E peek() {
        final Node<E> f = first;
        return (f == null) ? null : f.elementData.getFirst();
    }

    @Override
    public E peekFirst() { return peek(); }


    @Override
    public E peekLast() {
        final Node<E> l = last;
        return (l == null) ? null : l.elementData.getLast();
    }

    @Override
    public E poll() {
        final Node<E> f = first;
        return (f == null) ? null : unlinkFirst(f);
    }

    @Override
    public E pollFirst() {
        final Node<E> f = first;
        return (f == null) ? null : unlinkFirst(f);
    }

    @Override
    public E pollLast() {
        final Node<E> l = last;
        return (l == null) ? null : unlinkLast(l);
    }

    @Override
    public void clear() {
        // Clearing all the links between nodes is "unnecessary", but:
        // - helps a generational GC if the discarded nodes inhabit
        //   more than one generation
        // - is sure to free memory even if there is a reachable Iterator
        for (Node<E> x = first; x != null; ) {
            Node<E> next = x.next;
            x.elementData = null;
            x.next = null;
            x.prev = null;
            x = next;
        }
        first = last = null;
        size = 0;
        modCount++;
    }

    @Override
    public E get(int index) {
        checkElementIndex(index);
        return node(listIndex(index)).elementData.get(arrayIndex(index));
    }

    @Override
    public E set(int index, E element) {
        checkElementIndex(index);
        Node<E> x = node(listIndex(index));
        E oldVal = x.elementData.get(arrayIndex(index));
        x.elementData.set(arrayIndex(index),element);
        return oldVal;
    }

//    @Override
//    public void add(int index, E element) {
//        checkPositionIndex(index);
//        if (index == size())
//            linkLast(element);
//        else {
//            if (node(listIndex(index)).size == DEFAULT_TRIPLET_CAPACITY)
//                if (node(listIndex(index)).prev.size == DEFAULT_TRIPLET_CAPACITY)
//                    linkBefore(element, node(listIndex(index)));
//                else
//                    node(listIndex(index)).prev.addLast(element);
//            else node(listIndex(index)).addFirst(element);
//        }
//
//    }

    @Override
    public E remove(int index) {
        checkElementIndex(index);
        if (node(listIndex(index)).elementData.size() == 1)
            return unlink(node(listIndex(index)));
        else return node(listIndex(index)).elementData.remove(arrayIndex(index));
    }

    public void push(E e) {
        addFirst(e);
    }

    public E pop() {
        return removeFirst();
    }

    public boolean removeFirstOccurrence(Object o) {
        return remove(o);
    }

    public boolean removeLastOccurrence(Object o) {
        for (Node<E> x = last; x != null; x = x.prev) {
            if(x.removeLastOccurrence(o)){
                if(x.elementData.isEmpty()) unlink(x);
                return true;
            }
        }
        return false;
    }

    public LinkedList<E> reversed() {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    private int listIndex(int index){
        int counter = 0;
        for(Node<E> x = first; x != null; x = x.next){
            if(index - x.elementData.size() < 0) return counter;
            counter++;
            index -= x.elementData.size();
        }
        return -1;
    }

    private int arrayIndex(int index){
        for(Node<E> x = first; x != null; x = x.next){
            if(index - x.elementData.size() < 0) return index;
            index -= x.elementData.size();
        }
        return -1;
    }

    private boolean isLinkedListFull(){
        return size >= DEFAULT_CAPACITY;
    }

    private boolean isElementIndex(int index) {
        return index >= 0 && index < size();
    }

    private boolean isPositionIndex(int index) {
        return index >= 0 && index <= size();
    }

    private String outOfBoundsMsg(int index) {
        return "Index: "+index+", Size: "+size;
    }

    private void checkElementIndex(int index) {
        if (!isElementIndex(index))
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    private void checkPositionIndex(int index) {
        if (!isPositionIndex(index))
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    @Override
    public Object[] getContainerByIndex(int cIndex) {
        return node(cIndex).elementData.toArray();
    }


    private static class Node<E>{


        transient ArrayList<E> elementData;

        private static final int DEFAULT_CAPACITY = 5;

        Node<E> next;
        Node<E> prev;


        Node(Node<E> prev, E element, Node<E> next, int initialCapacity) {
            if (initialCapacity > 0) {
                this.elementData = new ArrayList<>(initialCapacity);
            } else if (initialCapacity == 0) {
                this.elementData = new ArrayList<>(DEFAULT_CAPACITY);
            } else {
                throw new IllegalArgumentException("Illegal Capacity: "+
                        initialCapacity);
            }

            this.elementData.addFirst(element);
            this.next = next;
            this.prev = prev;
        }

        public boolean removeLastOccurrence(Object o) {
            final ArrayList<E> es = elementData;
            final int size = this.elementData.size();
            int i = size - 1;
            found: {
                if (o == null) {
                    for (; i != 0; i--)
                        if (es.get(i) == null)
                            break found;
                } else {
                    for (; i != 0; i--)
                        if (o.equals(es.get(i)))
                            break found;
                }
                return false;
            }
            es.remove(i);
            return true;
        }

    }

}
