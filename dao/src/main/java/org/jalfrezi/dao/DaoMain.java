package org.jalfrezi.dao;

import java.io.File;
import java.sql.SQLException;

import org.jalfrezi.datamodel.Index;
import org.jalfrezi.datamodel.id.IndexId;

public class DaoMain {

	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		DerbyManager derbyManager = new DerbyManager(new File("./db"));
		IndexDao indexDao = new IndexDao();
		indexDao.init(derbyManager.getConnection());

		Index index = indexDao.read(IndexId.FTSE100);
		if (index == null) {
			System.out.println("None create a new one");
			index = new Index().setIndexId(IndexId.FTSE100).setName("rob");
			indexDao.create(index);
		}
		System.out.println(index.getIndexId() + " " + index.getName());
	}

}
