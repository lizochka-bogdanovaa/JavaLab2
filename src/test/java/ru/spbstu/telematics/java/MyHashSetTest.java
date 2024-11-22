package ru.spbstu.telematics.java;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

public class MyHashSetTest{
    private MyHashSet<Integer> myHashSet;
    private HashSet<Integer> regHashSet;

    @Before
    public void setUp(){
        myHashSet = new MyHashSet<>();
        regHashSet = new HashSet<>();
    }

    @Test
    public void testSize(){
        assertEquals(0,myHashSet.size());
        myHashSet.add(1);
        myHashSet.add(2);
        assertEquals(2,myHashSet.size());
    }

    @Test
    public void testIsEmpty(){
        assertEquals(myHashSet.isEmpty(),regHashSet.isEmpty());
        myHashSet.add(1);
        myHashSet.add(2);
        regHashSet.add(1);
        regHashSet.add(2);
        assertEquals(myHashSet.isEmpty(),regHashSet.isEmpty());
    }

    @Test
    public void testContains() {
        myHashSet.add(1);
        myHashSet.add(2);
        regHashSet.add(1);
        regHashSet.add(2);
        assertEquals(myHashSet.contains(1),regHashSet.contains(1));
        assertEquals(myHashSet.contains(3),regHashSet.contains(3)); //такого объекта нет
    }

    @Test
    public void testIterator() {
        myHashSet.add(1);
        myHashSet.add(2);
        regHashSet.add(1);
        regHashSet.add(2);
        Iterator<Integer> my_iter = myHashSet.iterator();
        Iterator<Integer> reg_iter = regHashSet.iterator();
        //Проверяем наличия следующего элемента и сравниваем элементы
        while (my_iter.hasNext() && reg_iter.hasNext()) {
            assertEquals(reg_iter.next(), my_iter.next());
        }
        assertEquals(my_iter.hasNext(), reg_iter.hasNext());
        //Удаляем первый элемент
        if (my_iter.hasNext()) {
            my_iter.remove();
            reg_iter.remove();
        }
        //Проверяем, что размеры контейнеров обновились
        assertEquals(regHashSet.size(), myHashSet.size());
        assertEquals(myHashSet.contains(1), regHashSet.contains(1));
        //Повторяем обход для оставшихся элементов
        while (my_iter.hasNext() && reg_iter.hasNext()) {
            assertEquals(reg_iter.next(), my_iter.next());
        }
        //Удаляем второй элемент
        if (my_iter.hasNext()) {
            my_iter.remove();
            reg_iter.remove();
        }
        assertEquals(regHashSet.isEmpty(), myHashSet.isEmpty());
    }

    @Test
    public void testToArray() {
        myHashSet.add(1);
        myHashSet.add(2);
        regHashSet.add(1);
        regHashSet.add(2);
        Object[] rightArray = {1, 2};
        assertArrayEquals(rightArray, myHashSet.toArray());
        assertArrayEquals(regHashSet.toArray(), myHashSet.toArray());
    }

    @Test
    public void testToArrayParameterized() {
        myHashSet.add(1);
        myHashSet.add(2);
        myHashSet.add(3);
        regHashSet.add(1);
        regHashSet.add(2);
        regHashSet.add(3);
        assertArrayEquals(regHashSet.toArray(new Integer[0]),myHashSet.toArray(new Integer[0]));
        assertArrayEquals(regHashSet.toArray(new Integer[5]),myHashSet.toArray(new Integer[5]));
    }

    @Test
    public void testAdd(){
        assertEquals(myHashSet.add(1),regHashSet.add(1));
        assertEquals(myHashSet.add(1),regHashSet.add(1)); //проверка, что дубликат не добавится
        assertEquals(myHashSet.add(2),regHashSet.add(2));
        assertEquals(myHashSet.size(),regHashSet.size());
    }

    @Test
    public void testRemove(){
        myHashSet.add(1);
        myHashSet.add(2);
        regHashSet.add(1);
        regHashSet.add(2);
        assertEquals(myHashSet.remove(1),regHashSet.remove(1));
        assertEquals(myHashSet.remove(3),regHashSet.remove(3)); //такого объекта нет
        assertEquals(myHashSet.size(),regHashSet.size());
    }

    @Test
    public void testContainsAll() {
        HashSet<Integer> newElements = new HashSet<>(Arrays.asList(4,5,6));
        myHashSet.addAll(newElements);
        regHashSet.addAll(newElements);
        assertEquals(myHashSet.containsAll(newElements),regHashSet.containsAll(newElements));
    }

    @Test
    public void testAddAll() {
        HashSet<Integer> newElements = new HashSet<>(Arrays.asList(4,5,6));
        myHashSet.addAll(newElements);
        regHashSet.addAll(newElements);
        assertEquals(regHashSet.size(),myHashSet.size());
        assertEquals(myHashSet.containsAll(newElements),regHashSet.containsAll(newElements));
    }

    @Test
    public void testRetainAll() {
        myHashSet.add(1);
        myHashSet.add(2);
        myHashSet.add(3);
        regHashSet.add(1);
        regHashSet.add(2);
        regHashSet.add(3);
        HashSet<Integer> newElements = new HashSet<>(Arrays.asList(1,2,3));
        assertEquals(myHashSet.retainAll(newElements),regHashSet.retainAll(newElements));
    }

    @Test
    public void testRemoveAll() {
        HashSet<Integer> newElements = new HashSet<>(Arrays.asList(1, 2, 3, 4, 5));
        HashSet<Integer> removeElements = new HashSet<>(Arrays.asList(1, 3, 5));
        myHashSet.addAll(newElements);
        regHashSet.addAll(newElements);

        assertEquals(myHashSet.removeAll(removeElements), regHashSet.removeAll(removeElements));
        assertEquals(myHashSet.size(), regHashSet.size());
        assertEquals(myHashSet.containsAll(removeElements), regHashSet.containsAll(removeElements));
    }

    @Test
    public void testClear() {
        HashSet<Integer> newElements = new HashSet<>(Arrays.asList(1, 2, 3));
        myHashSet.addAll(newElements);
        regHashSet.addAll(newElements);
        myHashSet.clear();
        regHashSet.clear();
        assertTrue(myHashSet.isEmpty());
        assertEquals(myHashSet.size(), regHashSet.size());
    }
}