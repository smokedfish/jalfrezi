package org.jalfrezi.dao;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.google.common.io.Files;

public class DerbyTestHelper {
	protected static Connection connection;
    
	@BeforeClass
	public static void beforeClass() throws ClassNotFoundException, SQLException, IOException {
		connection = new DerbyManager(new File(Files.createTempDir(), "db")).getConnection();
	}

	@AfterClass
	public static void afterClass() throws ClassNotFoundException, SQLException, IOException {
		connection.close();
	}
}
