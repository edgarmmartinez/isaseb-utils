package org.isaseb.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.AbstractQueue;

public class RankQueue<T> extends AbstractQueue<T> {
	LinkedList<T>				queueList;
	LinkedHashMap<T,Integer>	freqHashmap;
	int maxSize;
	
	public RankQueue(int	maxSize) {
		freqHashmap = new LinkedHashMap<T,Integer>();
		queueList = new LinkedList<T>();
		this.maxSize = maxSize;
	}
	
	@Override
	public boolean offer(T element) {
		queueList.add(element);
		incrFreq(element);
		
		if (size() > maxSize) {
			// remove the oldest element if we've reached capacity
			remove();
		}
		
		return true;
	}

	@Override
	public T poll() {
		T item = queueList.poll();
		
		if (item != null) {
			decrFreq(item);
		}
		
		return item;
	}

	@Override
	public T peek() {
		return queueList.peek();
	}

	@Override
	public Iterator<T> iterator() {
		return queueList.iterator();
	}

	@Override
	public int size() {
		return queueList.size();
	}
	
	@Override
	public void clear() {
		freqHashmap.clear();
		queueList.clear();
	};
	
	public List<Map.Entry<T, Integer>> getRank () {
		return sortHashtable(freqHashmap);
	}
	
	public int keyCount() {
		return freqHashmap.size();
	}
	
	public int maxSize () {
		return maxSize;
	}
	
	public void maxSize (int maxSize) {
		this.maxSize = maxSize;
	}
	
	private int incrFreq (T item) {
		Integer	value = freqHashmap.get(item);
		
		if (value == null) {
			value = new Integer (1);
		} else {
			value = value.intValue() + 1;
		}

		freqHashmap.put(item, value);
		
		return value.intValue();
	}
	
	private int decrFreq (T item) {
		Integer	value = freqHashmap.get(item);
		
		if (value != null) {
			value = value.intValue() - 1;
			if (value > 0) {
				freqHashmap.put(item, value);
			} else {
				freqHashmap.remove(item);
			}
			return value.intValue();
		}
		
		return 0;
	}
	
    private List<Map.Entry<T, Integer>> sortHashtable (HashMap<T,Integer> table) {
        List<Map.Entry<T, Integer>> entries = new ArrayList<Map.Entry<T, Integer>>(table.entrySet());
                Collections.sort(entries, new Comparator<Map.Entry<T, Integer>>() {
                  public int compare(Map.Entry<T, Integer> a, Map.Entry<T, Integer> b){
                	  //	sort high to low
                	  return b.getValue().compareTo(a.getValue());
                  }
                });
        
        return entries;
    }

}
