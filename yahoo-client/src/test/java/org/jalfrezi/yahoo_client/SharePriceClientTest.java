package org.jalfrezi.yahoo_client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.jalfrezi.datamodel.SharePrice;
import org.jalfrezi.datamodel.id.ShareId;
import org.jalfrezi.datamodel.id.SharePriceId;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import com.google.common.io.Resources;

public class SharePriceClientTest {
	private final PageFetcher pageFetcher = mock(PageFetcher.class);


	@Test
	public void shouldPreventDownloadsAtWeekends() {
		DateTime fri0 = new DateTime(2015, 9, 4, 0, 0);
		DateTime sat0 = new DateTime(2015, 9, 5, 0, 0);
		DateTime sun1 = new DateTime(2015, 9, 6, 0, 0);
		DateTime mon1 = new DateTime(2015, 9, 7, 0, 0);
		DateTime tue1 = new DateTime(2015, 9, 8, 0, 0);
		DateTime wed1 = new DateTime(2015, 9, 9, 0, 0);
		DateTime thu1 = new DateTime(2015, 9, 10, 0, 0);
		DateTime fri1 = new DateTime(2015, 9, 11, 0, 0);
		DateTime sat1 = new DateTime(2015, 9, 12, 0, 0);

		assertFalse(SharePriceClient.inRange(fri0, fri0));
		assertFalse(SharePriceClient.inRange(fri0, sat0));
		assertFalse(SharePriceClient.inRange(fri0, sun1));
		assertTrue(SharePriceClient.inRange(fri0, mon1));
		assertTrue(SharePriceClient.inRange(fri0, tue1));
		assertTrue(SharePriceClient.inRange(fri0, wed1));
		assertTrue(SharePriceClient.inRange(fri0, thu1));
		assertTrue(SharePriceClient.inRange(fri0, fri1));
		assertTrue(SharePriceClient.inRange(fri0, sat1));

		assertFalse(SharePriceClient.inRange(sat0, fri0));
		assertFalse(SharePriceClient.inRange(sat0, sat0));
		assertFalse(SharePriceClient.inRange(sat0, sun1));
		assertTrue(SharePriceClient.inRange(sat0, mon1));
		assertTrue(SharePriceClient.inRange(sat0, tue1));
		assertTrue(SharePriceClient.inRange(sat0, wed1));
		assertTrue(SharePriceClient.inRange(sat0, thu1));
		assertTrue(SharePriceClient.inRange(sat0, fri1));
		assertTrue(SharePriceClient.inRange(sat0, sat1));

		assertFalse(SharePriceClient.inRange(sun1, fri0));
		assertFalse(SharePriceClient.inRange(sun1, sat0));
		assertFalse(SharePriceClient.inRange(sun1, sun1));
		assertTrue(SharePriceClient.inRange(sun1, mon1));
		assertTrue(SharePriceClient.inRange(sun1, tue1));
		assertTrue(SharePriceClient.inRange(sun1, wed1));
		assertTrue(SharePriceClient.inRange(sun1, thu1));
		assertTrue(SharePriceClient.inRange(sun1, fri1));
		assertTrue(SharePriceClient.inRange(sun1, sat1));
	}

	@Test
	public void shouldGetResource() throws IOException {
		ShareId shareId = new ShareId("a");

		URL url = new URL("http://ichart.yahoo.com/table.csv?s=a&a=0&b=1&c=2015&d=5&e=30&f=2015");

		when(pageFetcher.getStream(eq(url))).thenReturn(Resources.getResource("SharePriceClient.txt").openStream());

		SharePriceClient sharePriceClient = new SharePriceClient(pageFetcher);
		DateTime startDate = new DateTime(2015, 1, 1, 0, 0);
		DateTime endDate = new DateTime(2015, 6, 30, 0, 0);
		List<SharePrice> sharePrices = sharePriceClient.getSharePrices(shareId, startDate, endDate);

		assertEquals(3, sharePrices.size());
		assertTrue(sharePrices.contains(new SharePrice().setShareId(shareId).setSharePriceId(new SharePriceId("2015-06-30")).setDate(new DateTime(2015, 6, 30, 0, 0, DateTimeZone.UTC)).setOpen(39.080002).setHigh(39.189999).setLow(38.48).setClose(38.580002).setVolume(3027900).setAdjClose(38.580002)));
		assertTrue(sharePrices.contains(new SharePrice().setShareId(shareId).setSharePriceId(new SharePriceId("2015-06-29")).setDate(new DateTime(2015, 6, 29, 0, 0, DateTimeZone.UTC)).setOpen(39.599998).setHigh(39.799999).setLow(38.740002).setClose(38.740002).setVolume(2514000).setAdjClose(38.740002)));
		assertTrue(sharePrices.contains(new SharePrice().setShareId(shareId).setSharePriceId(new SharePriceId("2015-06-26")).setDate(new DateTime(2015, 6, 26, 0, 0, DateTimeZone.UTC)).setOpen(40.00).setHigh(40.169998).setLow(39.779999).setClose(40.02).setVolume(3350500).setAdjClose(40.02)));
	}
}
