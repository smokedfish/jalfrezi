package org.jalfrezi.loader.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.jalfrezi.dao.IndexDao;
import org.jalfrezi.dao.ShareDao;
import org.jalfrezi.dao.SharePriceDao;
import org.jalfrezi.datamodel.Index;
import org.jalfrezi.datamodel.Share;
import org.jalfrezi.datamodel.SharePrice;
import org.jalfrezi.datamodel.id.IndexId;
import org.jalfrezi.datamodel.id.ShareId;
import org.jalfrezi.yahoo_client.IndexClient;
import org.jalfrezi.yahoo_client.ShareClient;
import org.jalfrezi.yahoo_client.SharePriceClient;
import org.joda.time.DateTime;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Sets;

@Named
public class LoaderService {
	private static final DateTime START_DATE = new DateTime(2015, 1, 1, 0, 0);
	private final IndexDao indexDao;
	private final ShareDao shareDao;
	private final SharePriceDao sharePriceDao;

	private final IndexClient indexClient;
	private final ShareClient shareClient;
	private final SharePriceClient sharePriceClient;

	@Inject
	public LoaderService(IndexDao indexDao, ShareDao shareDao, SharePriceDao sharePriceDao, IndexClient indexClient, ShareClient shareClient, SharePriceClient sharePriceClient) {
		super();
		this.indexDao = indexDao;
		this.shareDao = shareDao;
		this.sharePriceDao = sharePriceDao;
		this.indexClient = indexClient;
		this.shareClient = shareClient;
		this.sharePriceClient = sharePriceClient;
	}

	@PostConstruct
	public void init() throws SQLException, IOException {
		IndexId indexId = IndexId.FTSE100;

		Index index = indexDao.read(indexId);
		if (index == null) {
			index = new Index().setIndexId(indexId).setName("FTSE100");
			indexDao.create(index);
		}
		System.out.println(index);

		resolveShares(indexId);
		resolveSharePrices(indexId, new DateTime());
	}

	public void resolveShares(IndexId indexId) throws IOException, SQLException, JsonProcessingException {
		Set<ShareId> shareIdsInIx = indexClient.findByIndexId(indexId);
		Set<ShareId> shareIdsInDb = new HashSet<>();
		for (Share share : shareDao.findByIndexId(indexId)) {
			shareIdsInDb.add(share.getShareId());
		}

		List<ShareId> shareIdsToBeAdded = new ArrayList<>();
		for (ShareId addToIndex : Sets.difference(shareIdsInIx, shareIdsInDb)) {
			Share share = shareDao.read(addToIndex);
			if (share == null) {
				shareIdsToBeAdded.add(addToIndex);
			}
			else {
				System.out.println("move " + share);
				share.setIndexId(indexId);
				shareDao.update(share);
			}
		}

		for (Share share : shareClient.getShares(indexId, shareIdsToBeAdded)) {
			share.setLastFetch(START_DATE);
			System.out.println("add " + share);
			shareDao.create(share);
		}

		for (ShareId removeFromIndex : Sets.difference(shareIdsInDb, shareIdsInIx)) {
			Share share = shareDao.read(removeFromIndex);
			if (share != null) {
				share.setIndexId(IndexId.UNKNOWN);
				System.out.println("remove " + share);
				shareDao.update(share);
			}
		}
	}

	public void resolveSharePrices(IndexId indexId, DateTime now) throws SQLException, JsonProcessingException, IOException {
		for (Share share : shareDao.findByIndexId(indexId)) {
			System.out.println(share);
			for(SharePrice sharePrice : sharePriceClient.getSharePrices(share.getShareId(), share.getLastFetch(), now)) {
				System.out.println(sharePrice);
				sharePriceDao.create(sharePrice);
			}
			share.setLastFetch(now);
			shareDao.update(share);
		}
	}
}
