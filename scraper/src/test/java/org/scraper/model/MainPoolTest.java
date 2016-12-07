package org.scraper.model;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

public class MainPoolTest {
	@Test
	public void subPool() throws Exception {
		List<Integer> nums = new ArrayList<>();
		IntStream.range(0,100).forEach(nums::add);
		
		Function<Integer, Integer> rootFunc = (val) -> {
			MainLogger.log().debug("{} - {}", val, val*val);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return val*val;
		};
		
		MainPool.getInstance().subPool(nums, rootFunc, 1);
	}
	
}