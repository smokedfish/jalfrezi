package org.jalfrezi.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.jalfrezi.datamodel.Index;
import org.jalfrezi.datamodel.id.IndexId;

public class IndexDao {
	private static final String TABLE_STATEMENT = "CREATE TABLE APP.INDEX (id VARCHAR(30) NOT NULL, name VARCHAR(30), PRIMARY KEY (id))";
	private static final String CREATE_STATEMENT = "INSERT INTO APP.INDEX (id, name) VALUES (?, ?)";
	private static final String READ_STATEMENT = "SELECT name FROM APP.INDEX WHERE id = ?";
	private static final String UPDATE_STATEMENT = "UPDATE APP.INDEX SET name = ? WHERE id = ?";
	private static final String DELETE_STATEMENT = "DELETE FROM APP.INDEX WHERE id = ?";

	private PreparedStatement createStatement;
	private PreparedStatement updateStatement;
	private PreparedStatement readStatement;
	private PreparedStatement deleteStatement;

	public IndexDao() {
	}

	public void init(Connection dbConnection) throws SQLException {
		try {
			Statement statement = dbConnection.createStatement();
			statement.execute(TABLE_STATEMENT);
		}
		catch (SQLException e) {
			if (!DerbyUtils.tableAlreadyExists(e)) {
				throw e;
			}
		}
		createStatement = dbConnection.prepareStatement(CREATE_STATEMENT);
		readStatement = dbConnection.prepareStatement(READ_STATEMENT);
		updateStatement = dbConnection.prepareStatement(UPDATE_STATEMENT);
		deleteStatement = dbConnection.prepareStatement(DELETE_STATEMENT);
	}

	public void create(Index index) throws SQLException {
		createStatement.clearParameters();
		createStatement.setString(1, index.getIndexId().getId());
		createStatement.setString(2, index.getName());
		createStatement.executeUpdate();
	}

	public Index read(IndexId indexId) throws SQLException {
		readStatement.clearParameters();
		readStatement.setString(1, indexId.getId());
		ResultSet resultSet = readStatement.executeQuery();
		if (resultSet.next()) {
			return new Index()
			.setIndexId(indexId)
			.setName(resultSet.getString(1));
		}
		return null;
	}

	public void update(Index index) throws SQLException {
		updateStatement.clearParameters();
		updateStatement.setString(1, index.getName());
		updateStatement.setString(2, index.getIndexId().getId());
		updateStatement.executeUpdate();
	}

	public void delete(IndexId indexId) throws SQLException {
		deleteStatement.clearParameters();
		deleteStatement.setString(1, indexId.getId());
		deleteStatement.executeUpdate();
	}
}
