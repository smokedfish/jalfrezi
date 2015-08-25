package org.jalfrezi.dao;

import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.jalfrezi.datamodel.Index;
import org.jalfrezi.datamodel.id.IndexId;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class IndexDaoTest {
	private IndexDao indexDao = new IndexDao();

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	@Before
	public void before() throws ClassNotFoundException, SQLException, IOException {
		Connection connection = new DerbyManager(new File(folder.newFolder(), "db")).getConnection();
		indexDao.init(connection);
	}

	@Test
	public void shouldCreate() throws SQLException {
		Index expected = new Index().setIndexId(IndexId.FTSE100).setName("rob");
		indexDao.create(expected);
		Index actual = indexDao.read(expected.getIndexId());
		assertEquals(expected, actual);
	}

	@Test
	public void shouldUpdate() throws SQLException {
		Index initial = new Index().setIndexId(IndexId.FTSE100).setName("bob");
		Index expected = new Index().setIndexId(IndexId.FTSE100).setName("rob");
		indexDao.create(initial);
		indexDao.update(expected);
		Index actual = indexDao.read(expected.getIndexId());
		assertEquals(expected, actual);
	}

	@Test
	public void shouldDelete() throws SQLException {
		Index initial = new Index().setIndexId(IndexId.FTSE100).setName("bob");
		indexDao.create(initial);
		indexDao.delete(initial.getIndexId());
		Index actual = indexDao.read(initial.getIndexId());
		assertNull(actual);
	}
}
