package org.jalfrezi.dao;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.junit.BeforeClass;

import com.google.common.io.Files;

public class DerbyTestHelper {
	protected static Connection connection;

	@BeforeClass
	public static void beforeClass() throws ClassNotFoundException, SQLException, IOException {
		if (connection == null) {
			File path = new File(Files.createTempDir(), "db");
			connection = DriverManager.getConnection("jdbc:derby:" + path + ";create=true");
		}
	}
}
