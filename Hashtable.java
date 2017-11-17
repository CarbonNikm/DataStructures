/*  
    Nicholas Misleh
    cssc0134
*/
package data_structures3;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 * @author Nick
 */
public class Hashtable<K extends Comparable<K> ,V> implements DictionaryADT<K, V> {

    private int currentSize;
    private int maxSize;
    private int tableSize;
    private long modCounter;
    private LinkedList<Wrapper<K, V>> list[];
    class Wrapper<K extends Comparable<K>, V> implements Comparable<Wrapper<K, V>>{
        K key;
        V value;
        
        public Wrapper(K k, V v){
        key = k;
        value = v;
        }
        
        public int compareTo(Wrapper<K,V> w){
            return key.compareTo(w.key);
        }
    }
    
    
    public Hashtable(int max){
        currentSize = 0;
        maxSize = max;
        tableSize = (int)(maxSize*1.3f);
        list = new LinkedList[tableSize];
        for(int i = 0; i< tableSize; i++)
            list[i] = new LinkedList();
    }
    
    public boolean contains(K key) {
        for(Wrapper<K,V> w : list[getIndex(key)])
            if(key.equals(w.key)) return true;
        return false;
    }
    
    public boolean insert(K key, V value) {
        if(isFull()) return false;
        if(contains(key)) return false;
        list[getIndex(key)].addLast(new Wrapper(key, value));
        modCounter++;
        currentSize++;
        return true;
    }
    
    
    public boolean remove(K key) {
        Wrapper<K,V> tmp = list[getIndex(key)].remove(new Wrapper(key,null));
        if(tmp == null) return false;
        return true;
    }
    
    public V getValue(K key) {
        Wrapper<K, V> tmp = list[getIndex(key)].get(new Wrapper(key, null));
        if(tmp == null) return null;
        return tmp.value;
    }
    
    public K getKey(V value) {
        for(int i = 0; i < tableSize; i++)
            for(Wrapper<K,V> w: list[i])
                if(((Comparable<V>)value).compareTo(w.value) == 0) return w.key;
        return null;
    }
    
    public int size() {
        return currentSize;
    }
    
    public boolean isFull() {
        return currentSize == maxSize;
    }
    
    public boolean isEmpty() {
        return currentSize == 0;
    }
    
    public void clear() {
        for(int i = 0; i < tableSize; i++)
            list[i].clear();
        modCounter = 0;
        currentSize = 0;
    }
    
    public Iterator<K> keys() {
        return new KeyIteratorHelper<K>();
    }
    
    public Iterator<V> values() {
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
        protected Wrapper<K,V>[] nodes;
        protected int index;
        protected long savedModCounter;
        public IteratorHelper(){
            savedModCounter = modCounter;
            nodes = new Wrapper[currentSize];
            index = 0;
            for(int i = 0; i < tableSize; i++)
                for(Wrapper<K,V> w : list[i])
                    nodes[index++] = w;
            index = 0;
            sort();
        }
        private void sort(){
            int h = 1, out, in;
            int size = nodes.length;
            Wrapper<K,V> temp;
            while(h <= nodes.length/3)
                h = h*3+1;
            while(h > 0){
                for(out = h; out < nodes.length; out++){
                    temp = nodes[out];
                    in = out;
                    while(in > h - 1 && nodes[in - h].compareTo(temp) >= 0){
                        nodes[in] = nodes[in - h];
                        in -= h;
                    }
                    nodes[in] = temp;
                }
                h = (h-1)/3;
            }
        }
        public boolean hasNext() { 
        if(savedModCounter != modCounter){
            throw new ConcurrentModificationException("Iterator failed due to structural change.");
            }
            return index < currentSize;
        }
    }
    private int getIndex(K key){
        return (key.hashCode()&0x7fffffff)%tableSize;
    }
}
