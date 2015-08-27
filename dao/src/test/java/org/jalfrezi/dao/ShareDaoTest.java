package org.jalfrezi.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.sql.SQLException;

import org.jalfrezi.datamodel.Share;
import org.jalfrezi.datamodel.id.IndexId;
import org.jalfrezi.datamodel.id.ShareId;
import org.junit.Before;
import org.junit.Test;

public class ShareDaoTest extends DerbyTestHelper {
	private ShareDao shareDao = new ShareDao();
    
	@Before
	public void before() throws ClassNotFoundException, SQLException, IOException {
		shareDao.init(connection);
	}

	@Test
	public void shouldCreate() throws SQLException {
		Share expected = new Share().setIndexId(IndexId.FTSE100).setName("rob").setShareId(new ShareId("1RSW.L"));
		shareDao.create(expected);
		Share actual = shareDao.read(expected.getShareId());
		assertEquals(expected, actual);
	}

	@Test
	public void shouldUpdate() throws SQLException {
		Share initial = new Share().setIndexId(IndexId.FTSE100).setName("rob").setShareId(new ShareId("2RSW.L"));
		Share expected = new Share().setIndexId(IndexId.FTSE250).setName("bob").setShareId(new ShareId("2RSW.L"));
		shareDao.create(initial);
		shareDao.update(expected);
		Share actual = shareDao.read(expected.getShareId());
		assertEquals(expected, actual);
	}

	@Test
	public void shouldDelete() throws SQLException {
		Share initial = new Share().setIndexId(IndexId.FTSE100).setName("rob").setShareId(new ShareId("3RSW.L"));
		shareDao.create(initial);
		shareDao.delete(initial.getShareId());
		Share actual = shareDao.read(initial.getShareId());
		assertNull(actual);
	}
}
