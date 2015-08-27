package org.jalfrezi.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.inject.Inject;

public class AbstractBaseDao {

	private final Connection dbConnection;

	@Inject
	public AbstractBaseDao(Connection dbConnection) {
		this.dbConnection = dbConnection;
	}
	
	public void createTable(String tableStatement) throws SQLException {
		try {
			Statement statement = dbConnection.createStatement();
			statement.execute(tableStatement);
		}
		catch (SQLException e) {
			if (!tableAlreadyExists(e)) {
				throw e;
			}
		}
	}
	
	public PreparedStatement prepareStatement(String statement) throws SQLException {
		return dbConnection.prepareStatement(statement);
	}
	
    public static boolean tableAlreadyExists(SQLException e) {
        return e.getSQLState().equals("X0Y32");
    }
}
