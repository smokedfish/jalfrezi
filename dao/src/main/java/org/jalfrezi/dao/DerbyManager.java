package org.jalfrezi.dao;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DerbyManager {
	private final Connection connection;

	public DerbyManager(File dbPath) throws SQLException, ClassNotFoundException {
		this.connection = DriverManager.getConnection("jdbc:derby:" + dbPath.getAbsolutePath() + ";create=true");
	}

	public Connection getConnection() {
		return connection;
	}

	public void close() throws SQLException {
		connection.close();
	}
}
