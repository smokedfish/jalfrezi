package org.jalfrezi.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.jalfrezi.datamodel.SharePrice;
import org.jalfrezi.datamodel.id.ShareId;
import org.jalfrezi.datamodel.id.SharePriceId;

@Named
public class SharePriceDao extends AbstractBaseDao {
	private static final String TABLE_STATEMENT = "CREATE TABLE APP.SHARE_PRICE (share_id VARCHAR(30) NOT NULL, share_price_id VARCHAR(30) NOT NULL, date BIGINT, high DOUBLE, low DOUBLE, openx DOUBLE, closex DOUBLE, volume int, adj_close DOUBLE, PRIMARY KEY (share_id, share_price_id))";
	private static final String CREATE_STATEMENT = "INSERT INTO APP.SHARE_PRICE (share_id, share_price_id, date, high, low, openx, closex, volume, adj_close) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String READ_STATEMENT = "SELECT date, high, low, openx, closex, volume, adj_close FROM APP.SHARE_PRICE WHERE share_id = ? AND share_price_id = ?";
	private static final String UPDATE_STATEMENT = "UPDATE APP.SHARE_PRICE SET date = ?, high = ?, low = ?, openx = ?, closex = ?, volume = ?, adj_close = ? WHERE share_id = ? AND share_price_id = ?";
	private static final String DELETE_STATEMENT = "DELETE FROM APP.SHARE_PRICE WHERE share_id = ? AND share_price_id = ?";

	private PreparedStatement createStatement;
	private PreparedStatement updateStatement;
	private PreparedStatement readStatement;
	private PreparedStatement deleteStatement;

	@Inject
	public SharePriceDao(Connection dbConnection) {
		super(dbConnection);
	}

	@PostConstruct
	public void init() throws SQLException {
		createTable(TABLE_STATEMENT);
		createStatement = prepareStatement(CREATE_STATEMENT);
		readStatement = prepareStatement(READ_STATEMENT);
		updateStatement = prepareStatement(UPDATE_STATEMENT);
		deleteStatement = prepareStatement(DELETE_STATEMENT);
	}

	public void create(SharePrice sharePrice) throws SQLException {
		createStatement.clearParameters();
		createStatement.setString(1, sharePrice.getShareId().getId());
		createStatement.setString(2, sharePrice.getSharePriceId().getId());
		createStatement.setLong(3, sharePrice.getDate().getTime());
		createStatement.setDouble(4, sharePrice.getHigh());
		createStatement.setDouble(5, sharePrice.getLow());
		createStatement.setDouble(6, sharePrice.getOpen());
		createStatement.setDouble(7, sharePrice.getClose());
		createStatement.setInt(8, sharePrice.getVolume());
		createStatement.setDouble(9, sharePrice.getAdjClose());
		createStatement.executeUpdate();
	}

	public SharePrice read(ShareId shareId, SharePriceId sharePriceId) throws SQLException {
		readStatement.clearParameters();
		readStatement.setString(1, shareId.getId());
		readStatement.setString(2, sharePriceId.getId());
		ResultSet resultSet = readStatement.executeQuery();
		if (resultSet.next()) {
			return new SharePrice()
			.setShareId(shareId)
			.setSharePriceId(sharePriceId)
			.setDate(new Date(resultSet.getLong(1)))
			.setHigh(resultSet.getDouble(2))
			.setLow(resultSet.getDouble(3))
			.setOpen(resultSet.getDouble(4))
			.setClose(resultSet.getDouble(5))
			.setVolume(resultSet.getInt(6))
			.setAdjClose(resultSet.getDouble(7));
		}
		return null;
	}

	public void update(SharePrice sharePrice) throws SQLException {
		updateStatement.clearParameters();
		updateStatement.setLong(1, sharePrice.getDate().getTime());
		updateStatement.setDouble(2, sharePrice.getHigh());
		updateStatement.setDouble(3, sharePrice.getLow());
		updateStatement.setDouble(4, sharePrice.getOpen());
		updateStatement.setDouble(5, sharePrice.getClose());
		updateStatement.setInt(6, sharePrice.getVolume());
		updateStatement.setDouble(7, sharePrice.getAdjClose());
		updateStatement.setString(8, sharePrice.getShareId().getId());
		updateStatement.setString(9, sharePrice.getSharePriceId().getId());
		updateStatement.executeUpdate();
	}

	public void delete(ShareId shareId, SharePriceId sharePriceId) throws SQLException {
		deleteStatement.clearParameters();
		deleteStatement.setString(1, shareId.getId());
		deleteStatement.setString(2, sharePriceId.getId());
		deleteStatement.executeUpdate();
	}
}
