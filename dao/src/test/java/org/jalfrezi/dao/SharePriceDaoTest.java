package org.jalfrezi.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

import org.jalfrezi.datamodel.SharePrice;
import org.jalfrezi.datamodel.id.ShareId;
import org.jalfrezi.datamodel.id.SharePriceId;
import org.junit.Before;
import org.junit.Test;

public class SharePriceDaoTest extends DerbyTestHelper {
	private SharePriceDao sharePriceDao = new SharePriceDao();
    
	@Before
	public void before() throws ClassNotFoundException, SQLException, IOException {
		sharePriceDao.init(connection);
	}

	@Test
	public void shouldCreate() throws SQLException {
		SharePrice expected = new SharePrice().setSharePriceId(new SharePriceId("2015-09-08")).setShareId(new ShareId("1RSW.L")).setDate(new Date()).setHigh(1.0).setLow(2.0).setOpen(3.0).setClose(4.0).setVolume(5).setAdjClose(6.0);
		sharePriceDao.create(expected);
		SharePrice actual = sharePriceDao.read(expected.getShareId(), expected.getSharePriceId());
		assertEquals(expected, actual);
	}

	@Test
	public void shouldUpdate() throws SQLException {
		SharePrice initial = new SharePrice().setSharePriceId(new SharePriceId("2015-09-08")).setShareId(new ShareId("2RSW.L")).setDate(new Date()).setHigh(11.0).setLow(12.0).setOpen(13.0).setClose(14.0).setVolume(15).setAdjClose(16.0);
		SharePrice expected = new SharePrice().setSharePriceId(new SharePriceId("2015-09-08")).setShareId(new ShareId("2RSW.L")).setDate(new Date()).setHigh(1.0).setLow(2.0).setOpen(3.0).setClose(4.0).setVolume(5).setAdjClose(6.0);

		sharePriceDao.create(initial);
		sharePriceDao.update(expected);
		SharePrice actual = sharePriceDao.read(expected.getShareId(), expected.getSharePriceId());
		assertEquals(expected, actual);
	}

	@Test
	public void shouldDelete() throws SQLException {
		SharePrice initial = new SharePrice().setSharePriceId(new SharePriceId("2015-09-08")).setShareId(new ShareId("3RSW.L")).setDate(new Date()).setHigh(11.0).setLow(12.0).setOpen(13.0).setClose(14.0).setVolume(15).setAdjClose(16.0);
		sharePriceDao.create(initial);
		sharePriceDao.delete(initial.getShareId(), initial.getSharePriceId());
		SharePrice actual = sharePriceDao.read(initial.getShareId(), initial.getSharePriceId());
		assertNull(actual);
	}
}
