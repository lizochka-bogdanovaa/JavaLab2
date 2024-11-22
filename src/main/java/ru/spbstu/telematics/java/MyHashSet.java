package ru.spbstu.telematics.java;

import java.util.*;

public class MyHashSet<T> implements Set<T>, Iterable<T>
{

    private int size; //текущее количество элементов в множестве
    private static final int INITIAL_CAPACITY = 16; //количество мест в множестве всего
    private static final float LOAD_FACTOR = 0.75f; //когда массив будет заполнен на 75%, произойдет увеличение емкости (resize)
    private Object[] table;

    public MyHashSet(){
        table = new Object[INITIAL_CAPACITY];
        size = 0;
    }

    private int hash(Object key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private int find(Object key) {
        int index = hash(key);
        Object[] sameHash = (Object[]) table[index]; //элементы с одинаковым хеш-кодом
        if (sameHash != null) {
            for (int i = 0; i < sameHash.length; i++) {
                if (sameHash[i] != null && sameHash[i].equals(key)) {
                    return index;
                }
            }
        }
        return -1;
    }

    private void resize() {
        Object[] oldTable = table;
        table = new Object[table.length * 2];
        size = 0;
        for (Object sameHash : oldTable) {
            if (sameHash != null) {
                for (Object key : (Object[]) sameHash) {
                    add((T) key);
                }
            }
        }
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
    public boolean contains(Object key) {
        return find(key) != -1;
    }

    @Override
    public Iterator<T> iterator() {
        return new MyHashSetIterator();
    }

    private class MyHashSetIterator implements Iterator<T>{

        private int currentSameHashIndex = 0;
        private int currentElementIndex = -1;
        private boolean canRemove = false;
        private T nextElement;

        @Override
        public boolean hasNext() {
            findElement();
            return nextElement != null;
        }

        @Override
        public T next() {
            findElement();
            if(nextElement==null){
                throw new NoSuchElementException();
            }
            T result = nextElement;
            nextElement = null;
            return result;
        }

        @Override
        public void remove() {
            if (!canRemove) {
                throw new IllegalStateException("remove() called before next()");
            }
            System.arraycopy(table, currentSameHashIndex, table, currentSameHashIndex-1, table.length - currentSameHashIndex);
            size--;
            table[table.length -1] = null;

            canRemove = false;

        }

        private void findElement(){
            if(nextElement!=null){
                return;
            }
            while (currentSameHashIndex < table.length){
                Object[] sameHash = (Object[])table[currentSameHashIndex];
                currentElementIndex++;
                if(sameHash != null && currentElementIndex < sameHash.length && sameHash[currentElementIndex]!=null){
                    nextElement = (T)sameHash[currentElementIndex];
                    return;
                }
                //если не нашли элемент в массиве, то переходим к следующему
                currentSameHashIndex++;
                currentElementIndex=-1;
            }
        }
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        Iterator<T> it = iterator();
        while (it.hasNext()){
            result[i++]= it.next();
        }
        return result;
    }

    @Override
    public <E> E[] toArray(E[] a) {
        if (a.length < size)
            a = (E[])java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        int i = 0;
        Iterator<T> it = iterator();
        while (it.hasNext()) {
            a[i++] = (E) it.next();
        }
        if (a.length > size)
            a[size] = null;
        return a;
    }

    @Override
    public boolean add(T key) {
        if (contains(key)){
            return false;
        }
        int index = hash(key);
        Object[] sameHash = (Object[])table[index];

        if (sameHash == null){
            table[index] = new Object[]{key};
        }else{
            Object[] newSameHash = new Object[sameHash.length+1];
            System.arraycopy(sameHash,0,newSameHash,0,sameHash.length);
            newSameHash[newSameHash.length-1] = key;
            table[index] = newSameHash;
        }
        size++;
        if((float)size/table.length >= LOAD_FACTOR){
            resize();
        }
        return true;

    }

    @Override
    public boolean remove(Object key) {
        int index = find(key);
        if(index == -1){
            return false;
        }
        Object[] sameHash = (Object[])table[index];
        Object[] newSameHash = new Object[sameHash.length-1];
        int k = 0;
        for (int i = 0; i < sameHash.length;i++){
            if(sameHash[i]!=null && !sameHash[i].equals(key)){
                newSameHash[k++]=sameHash[i];
            }
        }
        table[index] = (newSameHash.length>0)? newSameHash : null;
        size--;
        return true;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for(Object o: c){
            if(!contains((T)o)){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        boolean modified = false;
        for(T e : c){
            if(add(e)){
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Iterator<T> it = iterator();
        while (it.hasNext()) {
            T nextElement = it.next();
            if (!c.contains(nextElement)) {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object o : c){
            if(remove((T)o)){
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public void clear() {
        table= new Object[INITIAL_CAPACITY];
        size = 0;
    }
}
