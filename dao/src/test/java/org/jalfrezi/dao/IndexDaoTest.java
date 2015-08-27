package org.jalfrezi.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.sql.SQLException;

import org.jalfrezi.datamodel.Index;
import org.jalfrezi.datamodel.id.IndexId;
import org.junit.Before;
import org.junit.Test;

public class IndexDaoTest extends DerbyTestHelper {
    private IndexDao indexDao = new IndexDao(connection);
    
	@Before
	public void before() throws SQLException {
		indexDao.init();
	}

	@Test
	public void shouldCreate() throws SQLException {
		Index expected = new Index().setIndexId(new IndexId("IDX1")).setName("rob");
		indexDao.create(expected);
		Index actual = indexDao.read(expected.getIndexId());
		assertEquals(expected, actual);
	}

	@Test
	public void shouldUpdate() throws SQLException {
		Index initial = new Index().setIndexId(new IndexId("IDX2")).setName("bob");
		Index expected = new Index().setIndexId(new IndexId("IDX2")).setName("rob");
		indexDao.create(initial);
		indexDao.update(expected);
		Index actual = indexDao.read(expected.getIndexId());
		assertEquals(expected, actual);
	}

	@Test
	public void shouldDelete() throws SQLException {
		Index initial = new Index().setIndexId(new IndexId("IDX3")).setName("bob");
		indexDao.create(initial);
		indexDao.delete(initial.getIndexId());
		Index actual = indexDao.read(initial.getIndexId());
		assertNull(actual);
	}
}
