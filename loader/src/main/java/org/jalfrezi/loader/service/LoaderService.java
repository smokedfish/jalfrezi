package org.jalfrezi.loader.service;

import java.sql.SQLException;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.jalfrezi.dao.IndexDao;
import org.jalfrezi.dao.ShareDao;
import org.jalfrezi.dao.SharePriceDao;
import org.jalfrezi.datamodel.Index;
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
	public void init() throws SQLException {
		Index index = indexDao.read(IndexId.FTSE100);
		if (index == null) {
			System.out.println("None create a new one");
			index = new Index().setIndexId(IndexId.FTSE100).setName("rob");
			indexDao.create(index);
		}
		System.out.println(index.getIndexId() + " " + index.getName());
	}
}
