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
import org.jalfrezi.datamodel.id.IndexId;
import org.jalfrezi.datamodel.id.ShareId;
import org.jalfrezi.loader.service.LoaderService;
import org.jalfrezi.yahoo_client.IndexClient;
import org.jalfrezi.yahoo_client.ShareClient;
import org.jalfrezi.yahoo_client.SharePriceClient;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

public class LoadServiceTest {

	@Test
	public void shouldResolveShares() throws IOException, SQLException {
		SharePriceClient sharePriceClient = mock(SharePriceClient.class);
		ShareClient shareClient = mock(ShareClient.class);
		IndexClient indexClient = mock(IndexClient.class);
		SharePriceDao sharePriceDao = mock(SharePriceDao.class);
		ShareDao shareDao = mock(ShareDao.class);
		IndexDao indexDao = mock(IndexDao.class);

		IndexId index1 = new IndexId("index1");
		IndexId index2 = new IndexId("index2");

		Share aInIdx = new Share().setIndexId(index1).setName("a").setShareId(new ShareId("a"));
		Share cInIdx = new Share().setIndexId(index1).setName("c").setShareId(new ShareId("c"));

		Share bInIDb = new Share().setIndexId(index1).setName("b").setShareId(new ShareId("b"));
		Share cInIDb = new Share().setIndexId(index2).setName("c").setShareId(new ShareId("c"));
		Share dInIDb = new Share().setIndexId(index1).setName("d").setShareId(new ShareId("d"));

		when(indexClient.findByIndexId(eq(index1))).thenReturn(new ImmutableSet.Builder<ShareId>().add(aInIdx.getShareId()).add(cInIdx.getShareId()).build());
		when(shareDao.findByIndexId(eq(index1))).thenReturn(new ImmutableSet.Builder<ShareId>().add(bInIDb.getShareId()).add(dInIDb.getShareId()).build());
		when(shareDao.read(eq(bInIDb.getShareId()))).thenReturn(bInIDb);
		when(shareDao.read(eq(cInIDb.getShareId()))).thenReturn(cInIDb);
		when(shareDao.read(eq(dInIDb.getShareId()))).thenReturn(dInIDb);
		when(shareClient.getShares(eq(index1), eq(new ImmutableList.Builder<ShareId>().add(aInIdx.getShareId()).build()))).thenReturn(new ImmutableSet.Builder<Share>().add(aInIdx).build());

		LoaderService loaderService = new LoaderService(indexDao, shareDao, sharePriceDao, indexClient, shareClient, sharePriceClient);
		loaderService.resolve(index1);

		verify(shareDao).update(eq(cInIdx));
		verify(shareDao).create(eq(aInIdx));
		verify(shareDao).update(eq(new Share().setIndexId(new IndexId("UNKNOWN")).setName("b").setShareId(new ShareId("b"))));
		verify(shareDao).update(eq(new Share().setIndexId(new IndexId("UNKNOWN")).setName("d").setShareId(new ShareId("d"))));
	}
}
