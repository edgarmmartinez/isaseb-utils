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

import org.isaseb.utils.HashKey;

/**
 * @author Edgar Martinez
 *
 */
public class TimedRankQueue<Key,Elem> extends AbstractQueue<Elem> {

	class RankElem {
		Elem	element;
		Integer	rank;
		
		public RankElem (Elem element, Integer rank) {
			this.element = element;
			this.rank = rank;
		}
	}
	
	//List of "Elem" objects, and a Long for the expiration time in seconds
	LinkedList<Map.Entry<Elem,Long>>	queueList;
	
	// Hashmap of key (String) and frequency
	LinkedHashMap<String,RankElem>	freqHashmap;
	HashKey<Key,Elem>	hashKey = null;
	
	int		ttlSeconds;
	long	oldestAllowed;

	public TimedRankQueue (HashKey<Key,Elem> key, int ttlSeconds) {
		queueList = new LinkedList<Map.Entry<Elem,Long>>();
		freqHashmap = new LinkedHashMap<String,RankElem>();
		this.hashKey = key;
		this.ttlSeconds = ttlSeconds;
	}
	
	@Override
	public boolean offer(Elem element) {
		queueList.add(new SimpleEntry<Elem,Long>(element,System.currentTimeMillis()/1000));
		incrFreq(element);
		
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
	public Elem poll() {
		purgeOld();
		
		Map.Entry<Elem,Long> item = queueList.poll();
		
		if (item != null) {
			decrFreq(item.getKey());
			return item.getKey();
		}
		return null;
	}

	@Override
	public Elem peek() {
		purgeOld();
		return queueList.peek() != null ? queueList.peek().getKey() : null;
	}

	@Override
	public Iterator<Elem> iterator() {
		LinkedList<Elem>	list = new LinkedList<Elem>();
		
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
		queueList.clear();
		freqHashmap.clear();
	};

	public List<Map.Entry<Elem, Integer>> getRank () {
		return sortHashtable(freqHashmap);
	}
	
	public int	getRank (Elem e) {
		return freqHashmap.get(getRankKey(e)).rank.intValue();
	}
	
	public int keyCount() {
		return freqHashmap.size();
	}
	
	private String getRankKey(Elem element) {
		return hashKey.getKey(element);
	}
	
	private int incrFreq (Elem element) {
		String elemKey = getRankKey(element);
		RankElem rankElem = freqHashmap.get(elemKey);
		
		if (rankElem == null) {
			rankElem = new RankElem(element, new Integer (1));
		} else {
			rankElem.rank = rankElem.rank.intValue() + 1;
		}

		freqHashmap.put(elemKey, rankElem);
		
		return rankElem.rank.intValue();
	}
	
	private int decrFreq (Elem	element) {
		String elemKey = getRankKey(element);
		RankElem rankElem = freqHashmap.get(elemKey); 
		
		if (rankElem != null) {
			rankElem.rank = rankElem.rank.intValue() - 1;
			if (rankElem.rank > 0) {
				freqHashmap.put(elemKey, rankElem);
			} else {
				freqHashmap.remove(elemKey);
			}
			return rankElem.rank.intValue();
		}
		
		return 0;
	}

	private List<Map.Entry<Elem, Integer>> sortHashtable (HashMap<String,RankElem> table) {
        List<Map.Entry<Elem, Integer>> entries = new ArrayList<Map.Entry<Elem,Integer>>(table.size());
        for ( RankElem rankElem : table.values()) {
        	entries.add(new SimpleEntry<Elem,Integer>(rankElem.element, rankElem.rank));
        }
        Collections.sort(entries, new Comparator<Map.Entry<Elem, Integer>>() {
                  					public int compare(Map.Entry<Elem, Integer> a, Map.Entry<Elem, Integer> b){
                  						//	sort high to low
                  						return b.getValue().compareTo(a.getValue());
                  					}
                				  }
        				);
        
        return entries;
    }
}
