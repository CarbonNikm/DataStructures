/*  Your name
    Your edoras class account number
    
    The list is one-based--the first element is at position #1 and the last element is
    at position currentSize.  Although the vector is not in sorted order, the ordering
    does matter. Order must be preserved if insertion/deletion happens in other than the last 
    position.  All of th elements in the list must be contiguous. 
*/

package data_structures3;

import java.util.Iterator;
import java.util.NoSuchElementException;

public interface UnorderedListADT<E extends Comparable<E>> extends Iterable<E> {

//  Adds the Object obj to the list in first position.
    public void addFirst(E obj);
    
//  Adds the Object obj to the end of the list.
    public void addLast(E obj);
    
//  Adds the Object obj to the list in the position indicated.  The list is one based, and
//  the first element is at position #1 (not zero).  If the position is currently occupied
//  existing elements must be shifted over to make room for the insertion.
    public void add(E obj, int position);        

//  Removes and returns the object located at the parameter position.
//  Throws a RuntimeException if the position does not map to a valid position within the list.
    public E remove(int position);
    
//  Removes and returns the parameter object obj from the list if the list contains it, 
//  null otherwise.  If more than one element matches, the element is lowest position is removed
//  and returned.
    public E remove(E obj);  
    
//  Removes and returns the first element in the list and null if the it is empty.
    public E removeFirst(); 
    
//  Removes and returns the last element in the list and null if the it is empty.
    public E removeLast();      

//  Returns the object located at the parameter position.
//  Throws a RuntimeException if the position does not map to a valid position within 
//  the list.
    public E get(int position);
    
//  Returns the list object that matches the parameter, and null if the list is empty
//  or if the element is not in the list.  If obj matches more than one element, 
//  the element with the lowest position is returned.
    public E get(E obj); 
    
//  Returns the position of the first element that matches the parameter obj
//  and -1 if the item is not in the list.
    public int find(E obj);   

//  Returns true if the parameter object obj is in the list, false otherwise.
    public boolean contains(E obj);    

//  The list is returned to an empty state.
    public void clear();

//  Returns true if the list is empty, otherwise false
    public boolean isEmpty();
    
//  Returns true if the list is full, otherwise false.  
    public boolean isFull();    

//  Returns the number of Objects currently in the list.
    public int size();
    
//  Returns an Iterator of the values in the list, presented in
//  the same order as the list.  The list iterator MUST be
//  fail-fast.
    public Iterator<E> iterator();

}