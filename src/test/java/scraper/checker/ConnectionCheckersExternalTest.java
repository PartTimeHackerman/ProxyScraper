package scraper.checker;

import org.junit.Test;
import scraper.main.TestsUtils;

import static org.junit.Assert.assertNull;

public class ConnectionCheckersExternalTest {
	
	private IConnectionCheckers connectionCheckers = new ConnectionCheckersExternal();
	
	@Test
	public void get() throws Exception {
		
		ConnectionCheck check = connectionCheckers.check(TestsUtils.brokenProxy1, 3000);
		
		assertNull(check);
		
		
	}
	
}