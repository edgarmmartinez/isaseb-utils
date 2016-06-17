/**
 * 
 */
package org.isaseb.utils;

import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.AbstractMap.SimpleEntry;

/**
 * @author edgar
 *
 */
public class TimedRankQueue<E> extends AbstractQueue<E> {
	LinkedList<Map.Entry<E, Long>>	queueList;
	LinkedHashMap<E,Integer>	freqHashmap;
	int							ttlSeconds;
	long						oldestAllowed;

	public TimedRankQueue (int ttlSeconds) {
		freqHashmap = new LinkedHashMap<E,Integer>();
		queueList = new LinkedList<Map.Entry<E, Long>>();
		this.ttlSeconds = ttlSeconds;
	}
	
	@Override
	public boolean offer(E e) {
		queueList.add(new SimpleEntry<E, Long>(e,System.currentTimeMillis()/1000));
		incrFreq(e);
		
		purgeOld();

		return true;
	}

	private void purgeOld() {
		long currTime = System.currentTimeMillis()/1000;
//		System.out.println ("time elapsed for oldest element: " + (currTime - queueList.peek().getValue()));
		while (queueList.size() != 0 && (currTime - queueList.peek().getValue()) > ttlSeconds) {
//			System.out.println ("Purging. Curr size = " + queueList.size() + ", Removing: " + queueList.peek().getKey());
			decrFreq(queueList.remove().getKey());
		}
	}
	
	@Override
	public E poll() {
		purgeOld();
		
		Map.Entry<E,Long> item = queueList.poll();
		
		if (item != null) {
			decrFreq(item.getKey());
		}
		
		return item.getKey();
	}

	@Override
	public E peek() {
		purgeOld();
		return queueList.peek().getKey();
	}

	@Override
	public Iterator<E> iterator() {
		//TODO why can't I init the LinkedList with an initial capacity
		
		LinkedList<E>	list = new LinkedList<E>();
		
		purgeOld();
		
		for (int i = 0, size = queueList.size(); i < size; i++) {
			list.add(queueList.get(i).getKey());
		}
		
		return list.iterator();
	}

	@Override
	public int size() {
		purgeOld();
		return queueList.size();
	}

	@Override
	public void clear() {
		freqHashmap.clear();
		queueList.clear();
	};

	public List<Map.Entry<E, Integer>> getRank () {
		return sortHashtable(freqHashmap);
	}
	
	public int	getRank (E e) {
		return freqHashmap.get(e).intValue();
	}
	
	public int keyCount() {
		return freqHashmap.size();
	}
	
	private int incrFreq (E element) {
		Integer	value = freqHashmap.get(element);
		
		if (value == null) {
			value = new Integer (1);
		} else {
			value = value.intValue() + 1;
		}

		freqHashmap.put(element, value);
		
		return value.intValue();
	}
	
	private int decrFreq (E	element) {
		Integer	value = freqHashmap.get(element);
		
		if (value != null) {
			value = value.intValue() - 1;
			if (value > 0) {
				freqHashmap.put(element, value);
			} else {
				freqHashmap.remove(element);
			}
			return value.intValue();
		}
		
		return 0;
	}
	
    private List<Map.Entry<E, Integer>> sortHashtable (HashMap<E,Integer> table) {
        List<Map.Entry<E, Integer>> entries = new ArrayList<Map.Entry<E, Integer>>(table.entrySet());
                Collections.sort(entries, new Comparator<Map.Entry<E, Integer>>() {
                  public int compare(Map.Entry<E, Integer> a, Map.Entry<E, Integer> b){
                	  //	sort high to low
                	  return b.getValue().compareTo(a.getValue());
                  }
                });
        
        return entries;
    }
}
