package org.jalfrezi.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.jalfrezi.datamodel.Share;
import org.jalfrezi.datamodel.id.IndexId;
import org.jalfrezi.datamodel.id.ShareId;

public class ShareDao {
	private static final String TABLE_STATEMENT = "CREATE TABLE APP.SHARE (id VARCHAR(30) NOT NULL, name VARCHAR(100), index_id VARCHAR(30) NOT NULL, PRIMARY KEY (id))";
	private static final String CREATE_STATEMENT = "INSERT INTO APP.SHARE (id, name, index_id) VALUES (?, ?, ?)";
	private static final String READ_STATEMENT = "SELECT name, index_id FROM APP.SHARE WHERE id = ?";
	private static final String UPDATE_STATEMENT = "UPDATE APP.SHARE SET name = ?, SET index_id = ?  WHERE id = ?";
	private static final String DELETE_STATEMENT = "DELETE FROM APP.SHARE WHERE id = ?";

	private PreparedStatement createStatement;
	private PreparedStatement updateStatement;
	private PreparedStatement readStatement;
	private PreparedStatement deleteStatement;

	public ShareDao() {
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

	public void create(Share share) throws SQLException {
		createStatement.clearParameters();
		createStatement.setString(1, share.getShareId().getId());
		createStatement.setString(2, share.getName());
		createStatement.setString(3, share.getIndexId().getId());
		createStatement.executeUpdate();
	}

	public Share read(ShareId shareId) throws SQLException {
		readStatement.clearParameters();
		readStatement.setString(1, shareId.getId());
		ResultSet resultSet = readStatement.executeQuery();
		if (resultSet.next()) {
			return new Share()
			.setShareId(shareId)
			.setName(resultSet.getString(1))
			.setIndexId(new IndexId(resultSet.getString(2)));

		}
		return null;
	}

	public void update(Share share) throws SQLException {
		updateStatement.clearParameters();
		updateStatement.setString(1, share.getName());
		updateStatement.setString(2, share.getIndexId().getId());
		updateStatement.executeUpdate();
	}

	public void delete(ShareId shareId) throws SQLException {
		deleteStatement.clearParameters();
		deleteStatement.setString(1, shareId.getId());
		deleteStatement.executeUpdate();
	}
}
