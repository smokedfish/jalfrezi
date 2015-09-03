package org.jalfrezi.yahoo_client;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URL;
import java.util.Set;

import org.jalfrezi.datamodel.id.IndexId;
import org.jalfrezi.datamodel.id.ShareId;
import org.junit.Test;

import com.google.common.io.Resources;

public class IndexClientTest {
	private final PageFetcher pageFetcher = mock(PageFetcher.class);

	@Test
	public void shouldFindByIndexId() throws IOException {
		IndexId indexId = new IndexId("index1");
		URL url = new URL("https://uk.finance.yahoo.com/q/cp?s=" + indexId);

		when(pageFetcher.getStream(eq(url))).thenReturn(Resources.getResource("IndexClientTest.txt").openStream());

		IndexClient indexClient = new IndexClient(pageFetcher);
		Set<ShareId> shares = indexClient.findByIndexId(indexId);
		assertEquals(1, shares.size());
		assertEquals(new ShareId("WTB.L"), shares.iterator().next());
	}

}
