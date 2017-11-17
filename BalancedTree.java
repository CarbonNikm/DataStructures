/*  
    Nicholas Misleh
    cssc0134
*/
package data_structures3;
import java.util.Iterator;
import java.util.TreeMap;
public class BalancedTree<K extends Comparable<K>,V> implements DictionaryADT<K, V>{
    
    
    private TreeMap tree;
    
    public BalancedTree(){
        tree = new TreeMap();
    }
    
    public boolean contains(K key) {
        return tree.containsKey(key);
    }
    
    public boolean insert(K key, V value) {
        return tree.put(key, value) != null;
    }
    
    public boolean remove(K key) {
       return tree.remove(key) != null;
    }
    
    public V getValue(K key) {
       V tmp = (V)tree.get(key);
       if(tmp == null) return null;
       return tmp;
    }

    public K getKey(V value){
        Iterator<K> kIter = tree.keySet().iterator();
        Iterator<V> vIter = tree.values().iterator();
        while(kIter.hasNext()){
            K tmpK = kIter.next();
            V tmpV = vIter.next();
            if(tmpV.equals(value)) return tmpK;
           }
        return null;
    }

    @Override
    public int size() {
        return tree.size();
    }
    
    public boolean isFull() {
        return false;
    }

    public boolean isEmpty() {
        return tree.isEmpty();
    }
    
    public void clear() {
        tree.clear();
    }

    public Iterator<K> keys() {
        return tree.keySet().iterator();
    }

    public Iterator<V> values() {
        return tree.values().iterator();
    }
}
