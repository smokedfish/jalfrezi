package org.jalfrezi.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.Set;

import org.jalfrezi.datamodel.Share;
import org.jalfrezi.datamodel.id.IndexId;
import org.jalfrezi.datamodel.id.ShareId;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ShareDaoTest {
	private static ShareDao shareDao = new ShareDao(DerbyTestHelper.getConnection());

	@BeforeClass
	public static void beforeClass() throws SQLException {
		shareDao.init();
	}

	@Before
	public void before() throws SQLException {
		shareDao.truncate();
	}

	@Test
	public void shouldCreate() throws SQLException {
		Share expected = new Share().setIndexId(IndexId.FTSE100).setName("rob").setShareId(new ShareId("1RSW.L")).setLastFetch(new DateTime());
		shareDao.create(expected);
		Share actual = shareDao.read(expected.getShareId());
		assertEquals(expected, actual);
	}

	@Test
	public void shouldUpdate() throws SQLException {
		Share initial = new Share().setIndexId(IndexId.FTSE100).setName("rob").setShareId(new ShareId("2RSW.L")).setLastFetch(new DateTime());
		Share expected = new Share().setIndexId(IndexId.FTSE250).setName("bob").setShareId(new ShareId("2RSW.L")).setLastFetch(new DateTime());
		shareDao.create(initial);
		shareDao.update(expected);
		Share actual = shareDao.read(expected.getShareId());
		assertEquals(expected, actual);
	}

	@Test
	public void shouldDelete() throws SQLException {
		Share initial = new Share().setIndexId(IndexId.FTSE100).setName("rob").setShareId(new ShareId("3RSW.L")).setLastFetch(new DateTime());
		shareDao.create(initial);
		shareDao.delete(initial.getShareId());
		Share actual = shareDao.read(initial.getShareId());
		assertNull(actual);
	}

	@Test
	public void shouldFindAll() throws SQLException {
		IndexId indexId = new IndexId("index1");
		Share[] shares = {
				new Share().setIndexId(indexId).setName("rob1").setShareId(new ShareId("1RSW.L")).setLastFetch(new DateTime()),
				new Share().setIndexId(indexId).setName("rob2").setShareId(new ShareId("2RSW.L")).setLastFetch(new DateTime()),
				new Share().setIndexId(indexId).setName("rob3").setShareId(new ShareId("3RSW.L")).setLastFetch(new DateTime())
		};
		for (Share share : shares) {
			shareDao.create(share);
		}
		shareDao.create(new Share().setIndexId(new IndexId("index2")).setName("rob4").setShareId(new ShareId("4RSW.L")).setLastFetch(new DateTime()));

		Set<ShareId> shareIdsFromDb = shareDao.findByIndexId(indexId);
		assertEquals(shares.length, shareIdsFromDb.size());
		for (Share share : shares) {
			assertTrue(shareIdsFromDb.contains(share.getShareId()));
		}
	}
}
