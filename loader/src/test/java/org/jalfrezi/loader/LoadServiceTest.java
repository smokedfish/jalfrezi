package org.jalfrezi.loader;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.sql.SQLException;

import org.jalfrezi.dao.IndexDao;
import org.jalfrezi.dao.ShareDao;
import org.jalfrezi.dao.SharePriceDao;
import org.jalfrezi.datamodel.Share;
import org.jalfrezi.datamodel.SharePrice;
import org.jalfrezi.datamodel.id.IndexId;
import org.jalfrezi.datamodel.id.ShareId;
import org.jalfrezi.datamodel.id.SharePriceId;
import org.jalfrezi.loader.service.LoaderService;
import org.jalfrezi.yahoo_client.IndexClient;
import org.jalfrezi.yahoo_client.ShareClient;
import org.jalfrezi.yahoo_client.SharePriceClient;
import org.joda.time.DateTime;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

public class LoadServiceTest {

	@Test
	public void shouldResolveShares() throws IOException, SQLException {
		ShareClient shareClient = mock(ShareClient.class);
		IndexClient indexClient = mock(IndexClient.class);
		ShareDao shareDao = mock(ShareDao.class);
		IndexId index1 = new IndexId("index1");
		IndexId index2 = new IndexId("index2");

		Share aInIdx = new Share().setIndexId(index1).setName("a").setShareId(new ShareId("a"));
		Share cInIdx = new Share().setIndexId(index1).setName("c").setShareId(new ShareId("c"));

		Share bInIDb = new Share().setIndexId(index1).setName("b").setShareId(new ShareId("b"));
		Share cInIDb = new Share().setIndexId(index2).setName("c").setShareId(new ShareId("c"));
		Share dInIDb = new Share().setIndexId(index1).setName("d").setShareId(new ShareId("d"));

		when(indexClient.findByIndexId(eq(index1))).thenReturn(new ImmutableSet.Builder<ShareId>().add(aInIdx.getShareId()).add(cInIdx.getShareId()).build());
		when(shareDao.findByIndexId(eq(index1))).thenReturn(new ImmutableList.Builder<Share>().add(bInIDb).add(dInIDb).build());
		when(shareDao.read(eq(bInIDb.getShareId()))).thenReturn(bInIDb);
		when(shareDao.read(eq(cInIDb.getShareId()))).thenReturn(cInIDb);
		when(shareDao.read(eq(dInIDb.getShareId()))).thenReturn(dInIDb);
		when(shareClient.getShares(eq(index1), eq(new ImmutableList.Builder<ShareId>().add(aInIdx.getShareId()).build()))).thenReturn(new ImmutableSet.Builder<Share>().add(aInIdx).build());

		LoaderService loaderService = new LoaderService(mock(IndexDao.class), shareDao, mock(SharePriceDao.class), indexClient, shareClient, mock(SharePriceClient.class));
		loaderService.resolveShares(index1);

		verify(shareDao).update(eq(cInIdx));
		verify(shareDao).create(eq(aInIdx));
		verify(shareDao).update(eq(new Share().setIndexId(new IndexId("UNKNOWN")).setName("b").setShareId(new ShareId("b"))));
		verify(shareDao).update(eq(new Share().setIndexId(new IndexId("UNKNOWN")).setName("d").setShareId(new ShareId("d"))));
	}

	@Test
	public void shouldResolveSharesPrices() throws IOException, SQLException {
		SharePriceClient sharePriceClient = mock(SharePriceClient.class);
		SharePriceDao sharePriceDao = mock(SharePriceDao.class);
		ShareDao shareDao = mock(ShareDao.class);
		IndexId index1 = new IndexId("index1");

		DateTime now = new DateTime(2015, 1, 1, 0, 0);
		Share shareA = new Share().setIndexId(index1).setName("a").setShareId(new ShareId("a")).setLastFetch(new DateTime(2015, 2, 27, 0, 0));
		SharePrice shareAPrice1 = new SharePrice().setSharePriceId(new SharePriceId("2015-02-26")).setShareId(new ShareId("a")).setDate(new DateTime(2015, 2, 26, 0, 0)).setHigh(1.0).setLow(2.0).setOpen(3.0).setClose(4.0).setVolume(5).setAdjClose(6.0);
		SharePrice shareAPrice2 = new SharePrice().setSharePriceId(new SharePriceId("2015-02-27")).setShareId(new ShareId("a")).setDate(new DateTime(2015, 2, 27, 0, 0)).setHigh(11.0).setLow(12.0).setOpen(13.0).setClose(14.0).setVolume(15).setAdjClose(16.0);

		Share shareB = new Share().setIndexId(index1).setName("b").setShareId(new ShareId("b")).setLastFetch(new DateTime(2015, 6, 30, 0, 0));
		SharePrice shareBPrice1 = new SharePrice().setSharePriceId(new SharePriceId("2015-06-09")).setShareId(new ShareId("b")).setDate(new DateTime(2015, 6, 9, 0, 0)).setHigh(21.0).setLow(22.0).setOpen(23.0).setClose(24.0).setVolume(25).setAdjClose(26.0);
		SharePrice shareBPrice2 = new SharePrice().setSharePriceId(new SharePriceId("2015-06-08")).setShareId(new ShareId("b")).setDate(new DateTime(2015, 6, 8, 0, 0)).setHigh(31.0).setLow(32.0).setOpen(33.0).setClose(34.0).setVolume(35).setAdjClose(36.0);

		when(shareDao.findByIndexId(eq(index1))).thenReturn(new ImmutableList.Builder<Share>().add(shareA).add(shareB).build());
		when(shareDao.read(eq(shareA.getShareId()))).thenReturn(shareA);
		when(shareDao.read(eq(shareB.getShareId()))).thenReturn(shareB);
		when(sharePriceClient.getSharePrices(eq(shareA.getShareId()), eq(shareA.getLastFetch()), eq(now))).thenReturn(new ImmutableList.Builder<SharePrice>().add(shareAPrice1).add(shareAPrice2).build());
		when(sharePriceClient.getSharePrices(eq(shareB.getShareId()), eq(shareB.getLastFetch()), eq(now))).thenReturn(new ImmutableList.Builder<SharePrice>().add(shareBPrice1).add(shareBPrice2).build());

		LoaderService loaderService = new LoaderService(mock(IndexDao.class), shareDao, sharePriceDao, mock(IndexClient.class), mock(ShareClient.class), sharePriceClient);
		loaderService.resolveSharePrices(index1, now);

		// FIXME ADD VERIFY HERE
	}
}
