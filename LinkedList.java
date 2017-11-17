/*  Nicholas Misleh
    cssc0134
    
    The linked list is one-based--the first element is at position #1 and the last element is
    at position currentSize.  Although the linked list is not in sorted order, the ordering
    does matter. Order must be preserved if insertion/deletion happens in other than the last 
    position.  All of the elements in the linked list must be contiguous. 
*/

package data_structures3;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;

public class LinkedList<E extends Comparable<E>> implements UnorderedListADT<E> {
    
    class Node<T>{
        T data;
        Node<T> next;
        public Node(T obj){
            data = obj;
            next = null;
        }
    }
    private int currentSize;
    private int timesModified; // Used for fail-fast iterator
    private Node<E> head,tail;
    
    public LinkedList(){
        head = null;
        tail = null;
        currentSize = 0;
        timesModified = 0;
    }
    
    
//  Adds the Object obj to the list in first position.
    public void addFirst(E obj){
        Node<E> newNode = new Node<E>(obj);
        if(head==null) {
            head = tail = newNode;
            newNode.next = null;
        }
        else newNode.next = head;
        head = newNode;
        currentSize++;
        timesModified++;
    }
    
//  Adds the Object obj to the end of the list.
    public void addLast(E obj){
        Node<E> newNode = new Node<E>(obj);
        if(head == null) {
            head = tail = newNode;
            newNode.next = null;
        }
        else tail.next = newNode;
        tail = newNode;
        currentSize++;
        timesModified++;
    }
    
//  Adds the Object obj to the list in the position indicated.  The list is one based, and
//  the first element is at position #1 (not zero).  If the position is currently occupied
//  existing elements must be shifted over to make room for the insertion.
    public void add(E obj, int position){
        Node<E> newNode = new Node<E>(obj);
        if (position > currentSize + 1 || position <= 0) {
            throw new RuntimeException("Invalid position");
        }
        if(position == 1){
            addFirst(obj);
            return;
        }
        if(position == currentSize + 1){
            addLast(obj);
            return;
        }
        Node<E> prev = null, current = head;
        for(int i = 1; i< position; i++){
            prev = current;
            current = current.next;
        }
        newNode.next = current;
        prev.next = newNode;
        currentSize++;
        timesModified++;
    }

//  Removes and returns the object located at the parameter position.
//  Throws a RuntimeException if the position does not map to a valid position within the list.
    public E remove(int position){
        if (position > currentSize || position <= 0) {
            throw new RuntimeException("Invalid position");
        }
        if(position == 1){
            return removeFirst();
        }
        if(position == currentSize){
            return removeLast();
        }
        Node<E> prev = null, current = head;
        for(int i = 1; i< position; i++){
            prev = current;
            current = current.next;
        }
        E obj = current.data;
        prev.next = current.next;
        currentSize--;
        timesModified++;
        return obj;
    }
    
//  Removes and returns the parameter object obj from the list if the list contains it, 
//  null otherwise.  If more than one element matches, the element is lowest position is removed
//  and returned.
    public E remove(E obj){
        int objPosition = find(obj); //removes the need for using find() twice
        return (objPosition == -1) ? null : remove(objPosition);
    } //fun ternary operator for no particular reason or advantage
    
//  Removes and returns the first element in the list and null if the it is empty.
    public E removeFirst(){
        if(head == null) return null;
        E obj = head.data;
        head = head.next;
        currentSize--;
        timesModified++;
        return obj;
    }
    
//  Removes and returns the last element in the list and null if the list is empty.
    public E removeLast(){
        if(head == null) return null;
        if(currentSize == 1){
            E obj = get(1);
            clear();
            return obj;
        }
        Node<E> prev = null, current = head;
        for(int i = 1; i< currentSize; i++){
            prev = current;
            current = current.next;
        }
        E obj = tail.data;
        tail = prev;
        prev.next = null;
        currentSize--;
        timesModified++;
        return obj;
    }     

//  Returns the object located at the parameter position.
//  Throws a RuntimeException if the position does not map to a valid position within 
//  the list.
    public E get(int position){
        if (position > currentSize || position <= 0) {
            throw new RuntimeException("Invalid position");
        }
        if(isEmpty()) return null;
        Node<E> current = head;
        for(int i = 1; i < position; i++){
            current = current.next;
        }
        return current.data;
    }
    
//  Returns the list object that matches the parameter, and null if the list is empty
//  or if the element is not in the list.  If obj matches more than one element, 
//  the element with the lowest position is returned.
    public E get(E obj){
        Node<E> current = head;
        while(current != null && obj.compareTo(current.data) != 0){
            current = current.next;
        }
        if(current == null) return null;
        obj = current.data;
        return obj;
    } 
    
//  Returns the position of the first element that matches the parameter obj
//  and -1 if the item is not in the list.
    public int find(E obj){
        Node<E> current = head;
        int currentPosition = 1;
        while(current != null && obj.compareTo(current.data) != 0){
            current = current.next;
            currentPosition++;
        }
        if(current == null) return -1;
        return currentPosition;
    }   

//  Returns true if the parameter object obj is in the list, false otherwise.
    public boolean contains(E obj){
        Node<E> current = head;
        while(current != null && obj.compareTo(current.data) != 0){
            current = current.next;
        }
        return(current != null);
    }   

//  The list is returned to an empty state.
    public void clear(){
        head = null;
        tail = null;
        currentSize = 0;
        timesModified = 0;
    }

//  Returns true if the list is empty, otherwise false
    public boolean isEmpty() { return currentSize == 0; }
    
//  Returns true if the list is full, otherwise false.  
    public boolean isFull() { return false; }

//  Returns the number of Objects currently in the list.
    public int size() { return currentSize; }
    
//  Returns an Iterator of the values in the list, presented in
//  the same order as the list.  The list iterator MUST be
//  fail-fast.
    public Iterator<E> iterator() { return new IteratorHelper(); }

    private class IteratorHelper implements Iterator<E> {

        private int index;
        private int savedTimesModified;
        Node<E> current = head;
        
        public IteratorHelper() { 
            index = 0; 
            savedTimesModified = timesModified;
        }

        public boolean hasNext() { 
            if(savedTimesModified != timesModified){
                throw new ConcurrentModificationException("Iterator failed due to structural change.");
            }
            return index < currentSize;
        }

        public E next() {
            if(savedTimesModified != timesModified){
                throw new ConcurrentModificationException("Iterator failed due to structural change.");
            }
            if (!hasNext()) { throw new NoSuchElementException(); }
            index++;
            E objData = current.data;
            current = current.next;
            return objData;
        }

        public void remove() { throw new UnsupportedOperationException(); }
    }

}