package scraper.main;

import org.junit.Test;
import scraper.MainLogger;
import scraper.Pool;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

public class PoolTest {
	@Test
	public void subPool() throws Exception {
		List<Integer> nums = new ArrayList<>();
		IntStream.range(0,100).forEach(nums::add);
		
		Function<Integer, Integer> rootFunc = (val) -> {
			MainLogger.log(this).debug("{} - {}", val, val*val);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return val*val;
		};
		
		new Pool().sendToInternalSubPool(nums, rootFunc, 1);
	}
	
}