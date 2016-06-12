package org.isaseb.utils;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.isaseb.utils.RankQueue;

import java.util.*;

public class TestRankQueue {
	static RankQueue<String>	ldrQue = null;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ldrQue = new RankQueue<String>(20);
	}

	@Before
	public void setUp() throws Exception {
		System.out.println ("test setUp");
		
		// add 19 entries to the queue
	    ldrQue.add("whoa");		//	7
	    ldrQue.add("whoa");
	    ldrQue.add("world");	//	3
	    ldrQue.add("whoa");
	    ldrQue.add("copycat");	//	3 - 3
	    ldrQue.add("little");	//	1 - 6
	    ldrQue.add("hello");	//	5 - 5
	    ldrQue.add("hello");
	    ldrQue.add("hello");
	    ldrQue.add("whoa");		// x - 4
	    ldrQue.add("hello");
	    ldrQue.add("copycat");
	    ldrQue.add("world");	// x - 2
	    ldrQue.add("hello");
	    ldrQue.add("whoa");
	    ldrQue.add("copycat");
	    ldrQue.add("whoa");
	    ldrQue.add("whoa");
	    ldrQue.add("world");
	}

	@Test
	public final void freqTest() {
	    
	    List<Map.Entry<String,Integer>>	sortedList = ldrQue.getRank();

	    for (Map.Entry<String, Integer> item : sortedList) {
	    	System.out.println (item.getKey() + " : " + item.getValue());
	    }
	    
	    assertEquals(5, sortedList.size());
	    
	    assertTrue("whoa".equals(sortedList.get(0).getKey()));
	    assertEquals (7, sortedList.get(0).getValue().longValue());

	    assertTrue("hello".equals(sortedList.get(1).getKey()));
	    assertEquals (5, sortedList.get(1).getValue().longValue());

	    assertTrue("world".equals(sortedList.get(2).getKey()));
	    assertEquals (3, sortedList.get(2).getValue().longValue());

	    assertTrue("copycat".equals(sortedList.get(3).getKey()));
	    assertEquals (3, sortedList.get(3).getValue().longValue());

	    assertTrue("little".equals(sortedList.get(4).getKey()));
	    assertEquals (1, sortedList.get(4).getValue().longValue());
	}
	
	@Test
	public final void addTest() {
		ldrQue.add("little");
		ldrQue.add("little");
		ldrQue.add("little");
		ldrQue.add("little");
		ldrQue.add("little");

	    List<Map.Entry<String,Integer>>	sortedList = ldrQue.getRank();

	    for (Map.Entry<String, Integer> item : sortedList) {
	    	System.out.println (item.getKey() + " : " + item.getValue());
	    }
	    
		assertEquals(20, ldrQue.size());
		assertEquals(5, sortedList.size());
		
	    assertTrue("little".equals(sortedList.get(0).getKey()));
	    assertEquals (6, sortedList.get(0).getValue().longValue());

	    assertTrue("hello".equals(sortedList.get(1).getKey()));
	    assertEquals (5, sortedList.get(1).getValue().longValue());

	    assertTrue("whoa".equals(sortedList.get(2).getKey()));
	    assertEquals (4, sortedList.get(2).getValue().longValue());

	    assertTrue("copycat".equals(sortedList.get(3).getKey()));
	    assertEquals (3, sortedList.get(3).getValue().longValue());

	    assertTrue("world".equals(sortedList.get(4).getKey()));
	    assertEquals (2, sortedList.get(4).getValue().longValue());
	}

	@Test
	public void removeTest() {
		assertTrue("whoa".equals(ldrQue.remove()));
	    assertTrue("whoa".equals(ldrQue.remove()));
	    assertTrue("world".equals(ldrQue.remove()));
	    assertTrue("whoa".equals(ldrQue.remove()));
	    assertTrue("copycat".equals(ldrQue.remove()));
	    assertTrue("little".equals(ldrQue.remove()));
	    
	    assertEquals(13, ldrQue.size());

	    List<Map.Entry<String,Integer>>	sortedList = ldrQue.getRank();

	    for (Map.Entry<String, Integer> item : sortedList) {
	    	System.out.println (item.getKey() + " : " + item.getValue());
	    }
	    

	    assertEquals(4, sortedList.size());
	    
	    assertTrue("hello".equals(sortedList.get(0).getKey()));
	    assertEquals (5, sortedList.get(0).getValue().longValue());

	    assertTrue("whoa".equals(sortedList.get(1).getKey()));
	    assertEquals (4, sortedList.get(1).getValue().longValue());

	    //	The order of "world" and "copycat" could be either since they're both "2"
	    assertTrue("world".equals(sortedList.get(2).getKey()));
	    assertEquals (2, sortedList.get(2).getValue().longValue());

	    assertTrue("copycat".equals(sortedList.get(3).getKey()));
	    assertEquals (2, sortedList.get(3).getValue().longValue());
	}

	@Test
	public void accessingEmpty() {
		int i = ldrQue.size();
		
		for (; i > 0; i--) {
			ldrQue.remove();
		}
		
		// Now remove one more
		try {
			ldrQue.remove();
		}
		catch (Exception exception) {
			assertTrue (exception instanceof NoSuchElementException);
		}
		
		// check that we removed all of the entries in the rank hashmap
		List<Map.Entry<String,Integer>>	sortedList = ldrQue.getRank();
		assertTrue(sortedList.size() == 0);
		
		assertTrue (ldrQue.poll() == null);
		
		assertTrue (ldrQue.peek() == null);
	}
	
	@After
	public void tearDown() throws Exception {
		System.out.println ("test tearDown");
		ldrQue.clear();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
}
