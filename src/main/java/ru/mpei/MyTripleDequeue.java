package ru.mpei;

import java.util.*;
import java.util.function.Consumer;


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


    private Node<E> first;

    private Node<E> last;

    private int size;

    @Override
    public int size() {
        int size = 0;
        for (Node<E> x = first; x != null; x = x.next) {
            size+=x.size();
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
        final E element = f.getLast();
        final Node<E> next = f.next;
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
        final E element = l.getLast();
        final Node<E> prev = l.prev;
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
        final E element = x.getLast();
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

        size--;
        modCount++;
        return element;
    }

    @Override
    public E getFirst() {
        final Node<E> f = first;
        if (f == null)
            throw new NoSuchElementException();
        return f.getFirst();
    }

    @Override
    public E getLast() {
        final Node<E> l = last;
        if (l == null)
            throw new NoSuchElementException();
        return l.getLast();
    }

    @Override
    public E removeFirst() {
        final Node<E> f = first;
        if (f == null)
            throw new NoSuchElementException();
        if (f.size() == 1) return unlinkFirst(f);
        else return f.removeFirst();
    }

    @Override
    public E removeLast() {
        final Node<E> l = last;
        if (l == null)
            throw new NoSuchElementException();
        if (l.size() == 1) return unlinkLast(l);
        else return l.removeLast();
    }

    @Override
    public void addFirst(E e) {
        if (e == null) throw new NullPointerException();
        final Node<E> f = first;
        if (f != null && f.size() < DEFAULT_TRIPLET_CAPACITY) f.addFirst(e);
        else linkFirst(e);
    }

    @Override
    public void addLast(E e) {
        if (e == null) throw new NullPointerException();
        final Node<E> l = last;
        if (l != null && l.size() < DEFAULT_TRIPLET_CAPACITY) l.addLast(e);
        else linkLast(e);
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public int indexOf(Object o) {
        int index = 0;
        for (Node<E> x = first; x != null; x = x.next) {
            int internalIndex = x.indexOf(o);
            if (internalIndex>=0){
                return index+internalIndex;
            }
            index+=x.size();
        }
        return -1;
    }

//    @Override
//    public int lastIndexOf(Object o) {
//        int index = size();
//        for (Node<E> x = last; x != null; x = x.prev) {
//            index-=x.size();
//            int internalIndex = x.lastIndexOf(o);
//            if (internalIndex >= 0)
//                return index+internalIndex;
//        }
//        return -1;
//    }



    @Override
    public Iterator<E> descendingIterator() {
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
        if (index >= size) return null;
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
            if(x.contains(o)){
                if(x.size() == 1) unlink(x);
                return x.remove(o);
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
        return (f == null) ? null : f.getFirst();
    }

    @Override
    public E peekFirst() { return peek(); }


    @Override
    public E peekLast() {
        final Node<E> l = last;
        return (l == null) ? null : l.getLast();
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
        return node(listIndex(index)).get(arrayIndex(index));
    }

    @Override
    public E set(int index, E element) {
        checkElementIndex(index);
        Node<E> x = node(listIndex(index));
        E oldVal = x.get(arrayIndex(index));
        x.set(arrayIndex(index),element);
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
        if (node(listIndex(index)).size() == 1)
            return unlink(node(listIndex(index)));
        else return node(listIndex(index)).remove(arrayIndex(index));
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
                if(x.isEmpty()) unlink(x);
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
            if(index - x.size() < 0) return counter;
            counter++;
            index -= x.size();
        }
        return -1;
    }

    private int arrayIndex(int index){
        for(Node<E> x = first; x != null; x = x.next){
            if(index - x.size() < 0) return index;
            index -= x.size();
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
        Node<E> n = node(cIndex);
        Object[] ret;
        if(n != null){
            ret = n.elementData;
        } else
            ret = null;

        return ret;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        checkPositionIndex(index);
        return new deqIterator();
    }

    private class deqIterator implements ListIterator<E> {
        private Node<E> currentContainer;
        private int currentIndex;
        private int elementsReturned;

        deqIterator() {
            currentContainer = first;

            if (currentContainer != null && !currentContainer.isEmpty()) {
                currentIndex = 0;
            } else {
                currentContainer = null;
                currentIndex = -1;
            }
            elementsReturned = 0;
        }

        @Override
        public boolean hasNext() {
            return elementsReturned < size;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            if (currentContainer == null) {
                throw new NoSuchElementException();
            }

            E element = currentContainer.get(currentIndex);
            elementsReturned++;

            currentIndex = (currentIndex + 1) % DEFAULT_TRIPLET_CAPACITY;
            if (currentIndex == 0) {
                currentContainer = currentContainer.next;
                if (currentContainer == null) {
                    currentIndex = -1;
                }
            }

            return element;
        }

        @Override
        public boolean hasPrevious() {
            return false;
        }

        @Override
        public E previous() {
            return null;
        }

        @Override
        public int nextIndex() {
            return 0;
        }

        @Override
        public int previousIndex() {
            return 0;
        }

        @Override
        public void remove() {

        }

        @Override
        public void set(E e) {

        }

        @Override
        public void add(E e) {

        }
    }


    private static class Node<E>
        extends AbstractList<E>
        implements List<E>{

        transient Object[] elementData;
        private int size;

        private static final int DEFAULT_CAPACITY = 5;

        private static final Object[] EMPTY_ELEMENTDATA = {};

        private Node<E> next;
        private Node<E> prev;


        public Node(Node<E> prev, E element, Node<E> next, int initialCapacity) {
            if (initialCapacity > 0) {
                this.elementData = new Object[initialCapacity];
            } else if (initialCapacity == 0) {
                this.elementData = new Object[DEFAULT_CAPACITY];
            } else {
                throw new IllegalArgumentException("Illegal Capacity: "+
                        initialCapacity);
            }

            this.addFirst(element);
            this.next = next;
            this.prev = prev;
        }

        public Node(Node<E> prev, E element, Node<E> next ){
            this(prev,element,next,DEFAULT_CAPACITY);
        }

        public Node(Node<E> prev, Collection<? extends E> c, Node<E> next, int initialCapacity) {
            Object[] a = c.toArray();
            if ((size = a.length) != 0) {
                if (c.getClass() == ArrayList.class) {
                    elementData = a;
                } else {
                    elementData = Arrays.copyOf(a, size, Object[].class);
                }
            } else {
                // replace with empty array.
                elementData = EMPTY_ELEMENTDATA;
            }
            this.next = next;
            this.prev = prev;
        }

        public Node(Node<E> prev, Collection<? extends E> c, Node<E> next ){
            this(prev,c,next,DEFAULT_CAPACITY);
        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public boolean isEmpty() {
            return size == 0;
        }

        @Override
        public boolean contains(Object o) {
            return indexOf(o) >= 0;
        }

        @Override
        public int indexOf(Object o) {
            return indexOfRange(o, 0, size);
        }

        int indexOfRange(Object o, int start, int end) {
            Object[] es = elementData;
            if (o == null) {
                for (int i = start; i < end; i++) {
                    if (es[i] == null) {
                        return i;
                    }
                }
            } else {
                for (int i = start; i < end; i++) {
                    if (o.equals(es[i])) {
                        return i;
                    }
                }
            }
            return -1;
        }

        @Override
        public int lastIndexOf(Object o) {
            return lastIndexOfRange(o, 0, size);
        }

        int lastIndexOfRange(Object o, int start, int end) {
            Object[] es = elementData;
            if (o == null) {
                for (int i = end - 1; i >= start; i--) {
                    if (es[i] == null) {
                        return i;
                    }
                }
            } else {
                for (int i = end - 1; i >= start; i--) {
                    if (o.equals(es[i])) {
                        return i;
                    }
                }
            }
            return -1;
        }

        @SuppressWarnings("unchecked")
        public <T> T[] toArray(T[] a) {
            if (a.length < size)
                // Make a new array of a's runtime type, but my contents:
                return (T[]) Arrays.copyOf(elementData, size, a.getClass());
            System.arraycopy(elementData, 0, a, 0, size);
            if (a.length > size)
                a[size] = null;
            return a;
        }

        @SuppressWarnings("unchecked")
        E elementData(int index) {
            return (E) elementData[index];
        }

        @SuppressWarnings("unchecked")
        static <E> E elementAt(Object[] es, int index) {
            return (E) es[index];
        }

        @Override
        public E get(int index) {
            Objects.checkIndex(index, size);
            return elementData(index);
        }

        @Override
        public E getFirst() {
            if (size == 0) {
                throw new NoSuchElementException();
            } else {
                return elementData(0);
            }
        }

        @Override
        public E getLast() {
            int last = size - 1;
            if (last < 0) {
                throw new NoSuchElementException();
            } else {
                return elementData(last);
            }
        }

        @Override
        public E set(int index, E element) {
            Objects.checkIndex(index, size);
            E oldValue = elementData(index);
            elementData[index] = element;
            return oldValue;
        }


        private void add(E e, Object[] elementData, int s) {
            elementData[s] = e;
            size = s + 1;
        }

        @Override
        public boolean add(E e) {
            modCount++;
            add(e, elementData, size);
            return true;
        }

        @Override
        public void add(int index, E element) {
            rangeCheckForAdd(index);
            modCount++;
            final int s = size;
            Object[] elementData  = this.elementData;
            System.arraycopy(elementData, index,
                    elementData, index + 1,
                    s - index);
            elementData[index] = element;
            size = s + 1;
        }

        @Override
        public void addFirst(E element) {
            add(0, element);
        }

        @Override
        public void addLast(E element) {
            add(element);
        }

        @Override
        public E remove(int index) {
            Objects.checkIndex(index, size);
            final Object[] es = elementData;

            @SuppressWarnings("unchecked") E oldValue = (E) es[index];
            fastRemove(es, index);

            return oldValue;
        }

        @Override
        public E removeFirst() {
            if (size == 0) {
                throw new NoSuchElementException();
            } else {
                Object[] es = elementData;
                @SuppressWarnings("unchecked") E oldValue = (E) es[0];
                fastRemove(es, 0);
                return oldValue;
            }
        }

        @Override
        public E removeLast() {
            int last = size - 1;
            if (last < 0) {
                throw new NoSuchElementException();
            } else {
                Object[] es = elementData;
                @SuppressWarnings("unchecked") E oldValue = (E) es[last];
                fastRemove(es, last);
                return oldValue;
            }
        }

        @Override
        public boolean remove(Object o) {
            final Object[] es = elementData;
            final int size = this.size;
            int i = 0;
            found: {
                if (o == null) {
                    for (; i < size; i++)
                        if (es[i] == null)
                            break found;
                } else {
                    for (; i < size; i++)
                        if (o.equals(es[i]))
                            break found;
                }
                return false;
            }
            fastRemove(es, i);
            return true;
        }

        private void fastRemove(Object[] es, int i) {
            modCount++;
            Object[] elementData = this.elementData;
            final int s = size;
            System.arraycopy(elementData, i+1, elementData, i, s-i-1);
            elementData[s-1] = null;
            size--;
        }

        public void clear() {
            modCount++;
            final Object[] es = elementData;
            for (int to = size, i = size = 0; i < to; i++)
                es[i] = null;
        }

        @Override
        public boolean addAll(Collection<? extends E> c) {
            Object[] a = c.toArray();
            modCount++;
            int numNew = a.length;
            if (numNew == 0)
                return false;
            Object[] elementData = this.elementData;
            final int s = size;

            System.arraycopy(a, 0, elementData, s, numNew);
            size = s + numNew;
            return true;
        }

        @Override
        public boolean addAll(int index, Collection<? extends E> c) {
            rangeCheckForAdd(index);

            Object[] a = c.toArray();
            modCount++;
            int numNew = a.length;
            if (numNew == 0)
                return false;
            Object[] elementData = this.elementData;
            final int s = size;
            int numMoved = s - index;
            if (numMoved > 0)
                System.arraycopy(elementData, index,
                        elementData, index + numNew,
                        numMoved);
            System.arraycopy(a, 0, elementData, index, numNew);
            size = s + numNew;
            return true;
        }

        private void rangeCheckForAdd(int index) {
            if (index > size || index < 0)
                throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
        }
        private String outOfBoundsMsg(int index) {
            return "Index: "+index+", Size: "+size;
        }


        public boolean removeLastOccurrence(Object o) {
            Object [] es = elementData;
            final int size = this.size();
            int i = size - 1;
            found: {
                if (o == null) {
                    for (; i != 0; i--)
                        if (es[i] == null)
                            break found;
                } else {
                    for (; i != 0; i--)
                        if (o.equals(es[i]))
                            break found;
                }
                return false;
            }
            es[i] = null;
            return true;
        }

    }

}
