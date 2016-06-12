/**
 * 
 */
package org.isaseb.utils;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.Thread;

/**
 * @author edgar
 *
 */
public class TestTimedRankQueue {
	static TimedRankQueue<String> 	timeQueue;
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testTimedRemoval() throws InterruptedException {
		timeQueue = new TimedRankQueue<String>(4);
		timeQueue.add("first");
		timeQueue.add("second");
		Thread.sleep(1000);
		timeQueue.add("third");
		Thread.sleep(2000);
		assertEquals(3, timeQueue.size());
		assertEquals(3, timeQueue.keyCount());
		Thread.sleep(2000);
		assertEquals(1, timeQueue.size());
		assertEquals(1, timeQueue.keyCount());
		Thread.sleep(1000);
		assertEquals(0, timeQueue.size());
		assertEquals(0, timeQueue.keyCount());
	}

}
