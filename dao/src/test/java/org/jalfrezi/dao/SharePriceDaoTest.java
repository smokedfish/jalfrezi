package org.jalfrezi.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.sql.SQLException;

import org.jalfrezi.datamodel.SharePrice;
import org.jalfrezi.datamodel.id.ShareId;
import org.jalfrezi.datamodel.id.SharePriceId;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class SharePriceDaoTest extends DerbyTestHelper {
	private static SharePriceDao sharePriceDao = new SharePriceDao(DerbyTestHelper.getConnection());

	@BeforeClass
	public static void beforeClass() throws SQLException {
		sharePriceDao.init();
	}

	@Before
	public void before() throws SQLException {
		sharePriceDao.truncate();
	}

	@Test
	public void shouldCreate() throws SQLException {
		SharePrice expected = new SharePrice().setSharePriceId(new SharePriceId("2015-09-08")).setShareId(new ShareId("1RSW.L")).setDate(new DateTime()).setHigh(1.0).setLow(2.0).setOpen(3.0).setClose(4.0).setVolume(5).setAdjClose(6.0);
		sharePriceDao.create(expected);
		SharePrice actual = sharePriceDao.read(expected.getShareId(), expected.getSharePriceId());
		assertEquals(expected, actual);
	}

	@Test
	public void shouldUpdate() throws SQLException {
		SharePrice initial = new SharePrice().setSharePriceId(new SharePriceId("2015-09-08")).setShareId(new ShareId("2RSW.L")).setDate(new DateTime()).setHigh(11.0).setLow(12.0).setOpen(13.0).setClose(14.0).setVolume(15).setAdjClose(16.0);
		SharePrice expected = new SharePrice().setSharePriceId(new SharePriceId("2015-09-08")).setShareId(new ShareId("2RSW.L")).setDate(new DateTime()).setHigh(1.0).setLow(2.0).setOpen(3.0).setClose(4.0).setVolume(5).setAdjClose(6.0);

		sharePriceDao.create(initial);
		sharePriceDao.update(expected);
		SharePrice actual = sharePriceDao.read(expected.getShareId(), expected.getSharePriceId());
		assertEquals(expected, actual);
	}

	@Test
	public void shouldDelete() throws SQLException {
		SharePrice initial = new SharePrice().setSharePriceId(new SharePriceId("2015-09-08")).setShareId(new ShareId("3RSW.L")).setDate(new DateTime()).setHigh(11.0).setLow(12.0).setOpen(13.0).setClose(14.0).setVolume(15).setAdjClose(16.0);
		sharePriceDao.create(initial);
		sharePriceDao.delete(initial.getShareId(), initial.getSharePriceId());
		SharePrice actual = sharePriceDao.read(initial.getShareId(), initial.getSharePriceId());
		assertNull(actual);
	}
}
