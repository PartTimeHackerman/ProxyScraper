package org.scraper.model.checker;

import org.junit.Test;
import org.scraper.model.TestsUtils;

import static org.junit.Assert.assertNull;

public class ConnectionCheckersExternalTest {
	
	private IConnectionCheckers connectionCheckers = new ConnectionCheckersExternal();
	
	@Test
	public void get() throws Exception {
		
		ConnectionCheck check = connectionCheckers.check(TestsUtils.brokenProxy1, 3000);
		
		assertNull(check);
		
		
	}
	
}