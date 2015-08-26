package org.jalfrezi.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.jalfrezi.datamodel.Share;
import org.jalfrezi.datamodel.id.IndexId;
import org.jalfrezi.datamodel.id.ShareId;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class ShareDaoTest {
	private ShareDao shareDao = new ShareDao();

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	@Before
	public void before() throws ClassNotFoundException, SQLException, IOException {
		Connection connection = new DerbyManager(new File(folder.newFolder(), "db")).getConnection();
		shareDao.init(connection);
	}

	@Test
	public void shouldCreate() throws SQLException {
		Share expected = new Share().setIndexId(IndexId.FTSE100).setName("rob").setShareId(new ShareId("RSW.L"));
		shareDao.create(expected);
		Share actual = shareDao.read(expected.getShareId());
		assertEquals(expected, actual);
	}

	@Test
	public void shouldUpdate() throws SQLException {
		Share initial = new Share().setIndexId(IndexId.FTSE100).setName("rob").setShareId(new ShareId("RSW.L"));
		Share expected = new Share().setIndexId(IndexId.FTSE250).setName("bob").setShareId(new ShareId("RSW.L"));
		shareDao.create(initial);
		shareDao.update(expected);
		Share actual = shareDao.read(expected.getShareId());
		assertEquals(expected, actual);
	}

	@Test
	public void shouldDelete() throws SQLException {
		Share initial = new Share().setIndexId(IndexId.FTSE100).setName("rob").setShareId(new ShareId("RSW.L"));
		shareDao.create(initial);
		shareDao.delete(initial.getShareId());
		Share actual = shareDao.read(initial.getShareId());
		assertNull(actual);
	}
}
