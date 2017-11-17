/*  
    Nicholas Misleh
    cssc0134
*/
package data_structures3;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class BinarySearchTree<K extends Comparable<K>,V> implements DictionaryADT<K,V> {
    
    class Node<K extends Comparable<K>, V> implements Comparable<Node<K, V>>{
        K key;
        V value;
        Node<K, V> left;
        Node<K, V> right;
        public Node(K k, V v){
            key = k;
            value = v;
            left = right = null;
        }
        public int compareTo(Node<K, V> n){
            return (key).compareTo(n.key);
        }
    }
    private Node root;
    private int currentSize;
    private int seqNum;
    private long modCount;
    
    public BinarySearchTree(){
        currentSize = 0;
        seqNum = 0;
        root = null;
    }
    
    public boolean contains(K key) {
        if(root == null) return false;
        return contains(key, root);
    }
    private boolean contains(K key, Node<K,V> n){
        if(n == null) return false;
        int compareResult = n.key.compareTo(key);
        if(compareResult == 0) return true;
        if(compareResult > 0) return contains(key, n.left);
        if(compareResult < 0) return contains(key, n.right);
        return false;
    }
    
    public boolean insert(K key, V value) {
        if(root == null) root = new Node(key,value);
        else insert(key, value, root, null, false);
        currentSize++;
        seqNum++;
        modCount++;
        return true;
    }
    private void insert(K key, V value, Node<K,V> n, Node<K,V> parent, boolean wasLeft){
        if(n == null){
            if(wasLeft) parent.left = new Node<K,V>(key,value);
            else parent.right = new Node<K,V>(key,value);
        }
        else if(key.compareTo(n.key) < 0) insert(key, value, n.left, n, true);
        else insert(key, value, n.right, n, false);
    }
    
    public boolean remove(K key) {
        if(root == null) return false;
        if(!contains(key)) return false;
        if(currentSize == 1){
            clear();
            return true;
        }
        if(!remove(key, root, null)) return false;
        currentSize--;
        modCount++;
        return true;
    }
    private boolean remove(K key, Node<K,V> n, Node<K,V> parentNode){
        int compareResult = n.key.compareTo(key);
        if(compareResult > 0) return remove(key, n.left, n);
        if(compareResult < 0) return remove(key, n.right, n);
        if(n.left == null && n.right == null){ //0 child nodes
            if(parentNode.left == n) parentNode.left = null;
            else if(parentNode.right == n) parentNode.right = null;
            return true;
        }
        else if(n.left == null && n.right != null){//one right child
            if(parentNode == null){
                root = n.right;
                return true;
            }
            if(parentNode.left == n) parentNode.left = n.right;
            else parentNode.right = n.right;
            return true;
        }
        else if(n.left != null && n.right == null){//one left child
            if(parentNode == null){
                root = n.left;
                return true;
            }
            if(parentNode.left ==  n) parentNode.left = n.left;
            else parentNode.right = n.left;
            return true;
        }
        //else 2 children
        Node<K,V> successor;
        Node<K,V> successorParent;
        successor = n.right;
        successorParent = n;
        while(successor.left != null){
            successorParent = successor;
            successor = successor.left;
        }
        if(successorParent == n){
            successor.left =  n.left;
            n.right = successor.right;
            n.key = successor.key;
            n.value = successor.value;
            return true;
        }
        n.value = successor.value;
        n.key = successor.key;
        if(successor.right != null) successorParent.left = successor.right;
        else successorParent.left = null;
        return true;
    }
  

    public V getValue(K key) {
        if(root == null) return null;
        return (V)getValue(key, root);
    }
    private V getValue(K key, Node<K,V> n){
        if(n == null) return null;
        int compareResult = n.key.compareTo(key);
        if(compareResult == 0) return n.value;
        if(compareResult > 0) return getValue(key, n.left);
        if(compareResult < 0) return getValue(key, n.right);
        return null;
    }

    public K getKey(V value) {
        if(root == null) return null;
        return (K)getKey(value, root);
    }
    private K getKey(V value, Node<K,V> n){
        K tmp = null;
        if(n == null) return null;      
        if(n.value.equals(value)) return n.key;  
        if(n.left != null) tmp = getKey(value, n.left);
        if(tmp != null) return tmp;
        if(n.right != null) tmp = getKey(value, n.right);
        if(tmp != null) return tmp;
        return null;
    }

    public int size() { return currentSize; }

    public boolean isFull() { return false; }

    public boolean isEmpty() { return currentSize == 0; }

    public void clear() {
        root = null;
        currentSize = 0;
        seqNum = 0;
    }

    public Iterator keys() {
        return new KeyIteratorHelper<K>();
    }

    public Iterator values() {
        return new ValueIteratorHelper<V>();
    }
    private class KeyIteratorHelper<K> extends IteratorHelper<K>{
        public KeyIteratorHelper() { super(); }
        public K next(){
            if(!hasNext()) throw new NoSuchElementException();
            return (K)nodes[index++].key;
        }
    }
    private class ValueIteratorHelper<V> extends IteratorHelper<V>{
        public ValueIteratorHelper() { super(); }
        public V next(){
            if(!hasNext()) throw new NoSuchElementException();
            return (V)nodes[index++].value;
        }
    }
    abstract class IteratorHelper<E> implements Iterator<E>{
        protected Node[] nodes;
        protected int index;
        protected long savedModCounter;
        public IteratorHelper(){
            savedModCounter = modCount;
            nodes = new Node[currentSize];
            index = 0;
            toArray(root);
            index = 0;
        }
        private void toArray(Node<K,V> n){
            if(n  == null) return;
            toArray(n.left);
            nodes[index++] = n;
            toArray(n.right);
        }
        public boolean hasNext(){
            if(savedModCounter != modCount){
                throw new ConcurrentModificationException("Failed due to structural change");
            }
            return index < currentSize;
        }
    }
}

