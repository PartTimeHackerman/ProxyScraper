package scraper.main;

import org.junit.BeforeClass;
import org.junit.Test;
import scraper.Interval;
import rx.functions.Action1;

import java.util.concurrent.Callable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class IntervalTest {
	
	private static Integer accumulator = 0;
	
	private static final Callable<Integer> test = () -> 1;
	private static final Action1<Integer> testAction = var -> accumulator += var;
	private static final String name = "test";
	
	@BeforeClass
	public static void setUpClass() throws Exception {
		Interval.addFunc(name, test);
		Interval.addAction(name, testAction);
	}
	
	@Test
	public void all() throws Exception {
		Long interval = 10L;
		Interval.setInterval(interval);
		
		assertEquals(interval, Interval.getInterval());
		
		Integer presentNum = accumulator;
		Thread.sleep(110);
		
		assertTrue(accumulator - presentNum > 10);
		
		Interval.setInterval(500L);
	}
	
}