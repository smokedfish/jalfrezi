package org.jalfrezi.share_scrape;

import java.io.IOException;

import org.jalfrezi.share_scrape.Jalfrezi;
import org.junit.Assert;
import org.junit.Test;

public class JalfreziTest {

	@Test
	public void testBadArgs() throws InterruptedException, IOException {
		assertFail("missing arguments", (String[])null);
		assertFail("-dir missing argument", "");
		assertFail("-dir missing argument", "-dir");
		assertFail("-dir missing argument", "-url");
		assertFail("-url missing argument", "-dir", ".");
		assertFail("-dir  i do not exist is unreadable", "-dir", " i do not exist", "-url", "u");
		assertFail("-url missing argument", "-dir", ".", "-url");
	}
	
	@Test
	public void testArgs() throws InterruptedException, IOException {
		Assert.assertNotNull(Jalfrezi.parseArgs("-dir", ".", "-url", "u"));
	}
	
	private void assertFail(String message, String... args) {
		try {
			Jalfrezi.parseArgs(args);
			Assert.fail();
		}
		catch(Exception e) {
			Assert.assertTrue(e.toString(), e instanceof IllegalArgumentException);
			Assert.assertEquals(message, e.getMessage());
		}
	}
}
