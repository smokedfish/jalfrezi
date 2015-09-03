package org.jalfrezi.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.jalfrezi.datamodel.Share;
import org.jalfrezi.datamodel.id.IndexId;
import org.jalfrezi.datamodel.id.ShareId;
import org.joda.time.DateTime;

@Named
public class ShareDao extends AbstractBaseDao {
	private static final String TABLE_STATEMENT = "CREATE TABLE APP.SHARE (share_id VARCHAR(30) NOT NULL, name VARCHAR(100), index_id VARCHAR(30) NOT NULL, last_fetch BIGINT, PRIMARY KEY (share_id))";
	private static final String TRUNCATE_STATEMENT = "TRUNCATE TABLE APP.SHARE";
	private static final String CREATE_STATEMENT = "INSERT INTO APP.SHARE (share_id, name, index_id, last_fetch) VALUES (?, ?, ?, ?)";
	private static final String READ_STATEMENT = "SELECT name, index_id, last_fetch FROM APP.SHARE WHERE share_id = ?";
	private static final String UPDATE_STATEMENT = "UPDATE APP.SHARE SET name = ?, index_id = ?, last_fetch = ? WHERE share_id = ?";
	private static final String DELETE_STATEMENT = "DELETE FROM APP.SHARE WHERE share_id = ?";
	private static final String FIND_ALL_STATEMENT = "SELECT share_id, name FROM APP.SHARE WHERE index_id = ?";

	private PreparedStatement createStatement;
	private PreparedStatement updateStatement;
	private PreparedStatement readStatement;
	private PreparedStatement deleteStatement;
	private PreparedStatement findByIndexIdStatement;

	@Inject
	public ShareDao(Connection dbConnection) {
		super(dbConnection);
	}

	@PostConstruct
	public void init() throws SQLException {
		createTable(TABLE_STATEMENT);
		createStatement = prepareStatement(CREATE_STATEMENT);
		readStatement = prepareStatement(READ_STATEMENT);
		updateStatement = prepareStatement(UPDATE_STATEMENT);
		deleteStatement = prepareStatement(DELETE_STATEMENT);
		findByIndexIdStatement = prepareStatement(FIND_ALL_STATEMENT);
	}

	public void truncate() throws SQLException {
		truncateTable(TRUNCATE_STATEMENT);
	}

	public void create(Share share) throws SQLException {
		createStatement.clearParameters();
		createStatement.setString(1, share.getShareId().getId());
		createStatement.setString(2, share.getName());
		createStatement.setString(3, share.getIndexId().getId());
		createStatement.setLong(4, share.getLastFetch().getMillis());
		createStatement.executeUpdate();
	}

	public Share read(ShareId shareId) throws SQLException {
		readStatement.clearParameters();
		readStatement.setString(1, shareId.getId());
		ResultSet resultSet = readStatement.executeQuery();
		try {
			if (resultSet.next()) {
				return new Share()
						.setShareId(shareId)
						.setName(resultSet.getString(1))
						.setIndexId(new IndexId(resultSet.getString(2)))
						.setLastFetch((new DateTime(resultSet.getLong(3))));
			}
			return null;
		}
		finally {
			resultSet.close();
		}
	}

	public void update(Share share) throws SQLException {
		updateStatement.clearParameters();
		updateStatement.setString(1, share.getName());
		updateStatement.setString(2, share.getIndexId().getId());
		updateStatement.setLong(3, share.getLastFetch().getMillis());
		updateStatement.setString(4, share.getShareId().getId());
		updateStatement.executeUpdate();
	}

	public void delete(ShareId shareId) throws SQLException {
		deleteStatement.clearParameters();
		deleteStatement.setString(1, shareId.getId());
		deleteStatement.executeUpdate();
	}

	public Set<ShareId> findByIndexId(IndexId indexId) throws SQLException {
		findByIndexIdStatement.clearParameters();
		findByIndexIdStatement.setString(1, indexId.getId());
		ResultSet resultSet = findByIndexIdStatement.executeQuery();
		try {
			Set<ShareId> shareIds = new HashSet<>();
			while (resultSet.next()) {
				shareIds.add(new ShareId(resultSet.getString(1)));
			}
			return shareIds;
		}
		finally {
			resultSet.close();
		}
	}
}
