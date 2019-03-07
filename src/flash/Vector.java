package flash;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

/**
 * A wrapper for ConcurrentHashMap that simulates AS3's Vector
 * Simulates a List that can be used as <Index, Value>
 */
public class Vector<T> implements Iterable<T> {

    public int length;
    private ConcurrentHashMap<Integer, T> map = new ConcurrentHashMap<>();

    public Vector() {
        this(0);
    }

    public Vector(int initialCapacity) {
        this.map = new ConcurrentHashMap<>(initialCapacity);
    }

    @SafeVarargs
    public Vector(T... addAll) {
        this.add(addAll);
    }

    public Vector(List<T> addAll) {
        for (T t : addAll) {
            this.push(t);
        }
    }

    public Vector(int numMembers, boolean fixedLength) {
        this(numMembers);
    }

    private void updateLength() {
        this.length = this.map.size();
    }

    /**
     * Used remove instead
     */
    @Deprecated
    public void splice(double start, double deleteCount) {
    }

    public void sort(BiFunction<T, T, Integer> sort) {

    }

    /**
     * Removes object at index
     */
    public void remove(int i) {
        this.map.remove(i);
    }

    /**
     * Removes object
     */
    public void remove(T t) {
        Iterator<T> e = this.iterator();

        for (Iterator<T> it = e; it.hasNext(); ) {
            T a = it.next();
            if (a.equals(t)) {
                it.remove();
            }
        }
    }

    public boolean contains(T t) {
        return this.map.containsValue(t);
    }

    public void push(T t) {
        int newSize = this.map.size() == 0 ? 0 : this.map.size() + 1;
        this.put(newSize, t);
    }

    public void set(int index, T t) {
        this.put(index, t);
    }

    public T put(int index, T t) {
        this.map.put(index, t);
        this.updateLength();

        return t;
    }

    public T get(int index) {
        return this.map.get(index);
    }

    //Removes the last element from the Vector and returns that element.
    public T pop() {
        if (this.length > 0) {
            this.length--;
        }
        this.updateLength();
        return this.map.remove(this.map.size());
    }

    @Override
    public Iterator<T> iterator() {
        return this.map.values().iterator();
    }

    public void add(T t) {
        this.push(t);
    }

    public void add(T... list) {
        for (T t : list) {
            this.push(t);
        }
    }

    /*
     * Concatenates the Vectors specified in the parameters list with the elements in this Vector and creates a new Vector.
     */
    public final Vector<T> concat(Vector<T>... vectors) {
        List<T> data = new ArrayList<>();

        for (Vector<T> vec : vectors) {
            for (T t : vec) {
                data.add(t);
            }
        }

        this.updateLength();

        return new Vector<>(data);
    }


    public void clear() {
        this.map.clear();
    }

    public void splice(T i, T i1, T lineTo, T lineTo1, T lineTo2) {
    }

    public void splice(int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
    }

    public boolean hasOwnProperty(T i) {
        return this.contains(i);
    }

    public int indexOf(T loc2) {
        for (int i : this.map.keySet()) {
            if (this.map.get(i).equals(loc2)) {
                return i;
            }
        }
        return -1;
    }

    public void set(T set) {
        this.clear();
        this.add(set);
    }

    public int size() {
        return this.length;
    }


    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("'");
        for (T t : this.map.values()) {
            s.append(t);
            s.append(", ");
        }
        s.append("'");
        return s.toString();
    }

    public Vector<T> slice() {
        return this;
    }


    public void sortOn(Vector<?> visibleSortFields, Vector<?> visibleSortParams) {
    }
}
