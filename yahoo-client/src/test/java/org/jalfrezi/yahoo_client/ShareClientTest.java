package org.jalfrezi.yahoo_client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import org.jalfrezi.datamodel.Share;
import org.jalfrezi.datamodel.id.IndexId;
import org.jalfrezi.datamodel.id.ShareId;
import org.junit.Test;

import com.google.common.io.Resources;

public class ShareClientTest {
	private final PageFetcher pageFetcher = mock(PageFetcher.class);

	@Test
	public void shouldGetResource() throws IOException {
		IndexId indexId = new IndexId("index1");
		Collection<ShareId> shareIds = Arrays.asList( new ShareId("a"), new ShareId("b"), new ShareId("c") );

		URL url = new URL("http://download.finance.yahoo.com/d/quotes.csv?s=a,b,c&f=xsn&e=.csv");

		when(pageFetcher.getStream(eq(url))).thenReturn(Resources.getResource("ShareClientTest.txt").openStream());

		ShareClient shareClient = new ShareClient(pageFetcher);
		Set<Share> shares = shareClient.getShares(indexId, shareIds);

		assertEquals(3, shares.size());
		assertTrue(shares.contains(new Share().setIndexId(indexId).setName("Agilent Technologies, Inc. Comm").setShareId(new ShareId("a")).setLastFetch(null)));
		assertTrue(shares.contains(new Share().setIndexId(indexId).setName("Barnes Group, Inc. Common Stock").setShareId(new ShareId("b")).setLastFetch(null)));
		assertTrue(shares.contains(new Share().setIndexId(indexId).setName("Citigroup, Inc. Common Stock").setShareId(new ShareId("c")).setLastFetch(null)));
	}
}
