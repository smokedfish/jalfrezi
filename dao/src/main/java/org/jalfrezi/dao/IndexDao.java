package org.jalfrezi.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.jalfrezi.datamodel.Index;
import org.jalfrezi.datamodel.id.IndexId;

@Named
public class IndexDao extends AbstractBaseDao {
	private static final String TABLE_STATEMENT = "CREATE TABLE APP.INDEX (index_id VARCHAR(30) NOT NULL, name VARCHAR(30), PRIMARY KEY (index_id))";
	private static final String CREATE_STATEMENT = "INSERT INTO APP.INDEX (index_id, name) VALUES (?, ?)";
	private static final String READ_STATEMENT = "SELECT name FROM APP.INDEX WHERE index_id = ?";
	private static final String UPDATE_STATEMENT = "UPDATE APP.INDEX SET name = ? WHERE index_id = ?";
	private static final String DELETE_STATEMENT = "DELETE FROM APP.INDEX WHERE index_id = ?";

	private PreparedStatement createStatement;
	private PreparedStatement updateStatement;
	private PreparedStatement readStatement;
	private PreparedStatement deleteStatement;

	@Inject
	public IndexDao(Connection dbConnection) {
		super(dbConnection);
	}

	@PostConstruct
	public void init() throws SQLException {
		createTable(TABLE_STATEMENT);
		this.createStatement = prepareStatement(CREATE_STATEMENT);
		this.readStatement = prepareStatement(READ_STATEMENT);
		this.updateStatement = prepareStatement(UPDATE_STATEMENT);
		this.deleteStatement = prepareStatement(DELETE_STATEMENT);
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
