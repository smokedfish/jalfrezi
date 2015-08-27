package org.jalfrezi.loader.service;

import java.io.IOException;
import java.sql.SQLException;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.jalfrezi.dao.IndexDao;
import org.jalfrezi.dao.ShareDao;
import org.jalfrezi.dao.SharePriceDao;
import org.jalfrezi.datamodel.Index;
import org.jalfrezi.datamodel.Share;
import org.jalfrezi.datamodel.id.IndexId;
import org.jalfrezi.yahoo_client.IndexClient;
import org.jalfrezi.yahoo_client.ShareClient;
import org.jalfrezi.yahoo_client.SharePriceClient;

@Named
public class LoaderService {
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
		for (Share share : shareClient.getShares(indexId, indexClient.getShareIds(indexId))) {
			shareDao.create(share);
			System.out.println(share);
		}
	}
}
