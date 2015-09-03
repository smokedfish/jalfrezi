package org.jalfrezi.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.inject.Inject;

public class AbstractBaseDao {

	private final Connection dbConnection;

	@Inject
	public AbstractBaseDao(Connection dbConnection) {
		this.dbConnection = dbConnection;
	}

	public void createTable(String createTableStatement) throws SQLException {
		try {
			dbConnection.createStatement().execute(createTableStatement);
		}
		catch (SQLException e) {
			if (!tableAlreadyExists(e)) {
				throw e;
			}
		}
	}

	public void truncateTable(String truncateTableStatement) throws SQLException {
		try {
			dbConnection.createStatement().execute(truncateTableStatement);
		}
		catch (SQLException e) {
			if (!tableDoesNotExist(e)) {
				throw e;
			}
		}
	}

	public PreparedStatement prepareStatement(String statement) throws SQLException {
		return dbConnection.prepareStatement(statement);
	}

    public static boolean tableDoesNotExist(SQLException e) {
        return e.getSQLState().equals("42Y55");
    }

    public static boolean tableAlreadyExists(SQLException e) {
        return e.getSQLState().equals("X0Y32");
    }
}
