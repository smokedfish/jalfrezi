package org.jalfrezi.dao;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.google.common.io.Files;

public class DerbyTestHelper {
	private static Connection connection;

	public static Connection getConnection() {
		if (connection == null) {
			try {
				File path = new File(Files.createTempDir(), "db");
				connection = DriverManager.getConnection("jdbc:derby:" + path + ";create=true");
			}
			catch (SQLException e) {
				connection = null;
			}
		}
		return connection;
	}
}
