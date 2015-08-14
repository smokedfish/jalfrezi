package org.jalfrezi.yahoo_client;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jalfrezi.datamodel.Share;
import org.jalfrezi.datamodel.SharePrice;
import org.jalfrezi.datamodel.id.ShareId;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterators;

public class ShareClient {
	private final int chunk = 50;
	private final CsvMapper mapper = new CsvMapper();
	private final CsvSchema shareSchema = CsvSchema.builder()
	        .addColumn("indexId") //x
	        .addColumn("shareId") //s
	        .addColumn("name") //n
	        .build();

	private CsvSchema sharePriceSchema = CsvSchema.builder()
	        .addColumn("date")
	        .addColumn("open")
	        .addColumn("high")
	        .addColumn("low")
	        .addColumn("close")
	        .addColumn("volume")
	        .addColumn("adjClose")
	        .build()
	        .withHeader();

	public List<Share> getShares(List<ShareId> shareIds) throws JsonProcessingException, IOException {
		List<Share> shares = new ArrayList<>(shareIds.size());
		for(int s = 0, e = chunk; s < shareIds.size(); s += chunk, e += chunk) {
			List<ShareId> subShareIds = shareIds.subList(s, Math.min(e, shareIds.size()));
			String shareIdsQuery = Joiner.on(",").join(subShareIds);
			URL url = new URL("http://download.finance.yahoo.com/d/quotes.csv?s=" + shareIdsQuery + "&f=xsn&e=.csv");
			Iterators.addAll(shares, mapper.reader(Share.class).with(shareSchema).<Share>readValues(url));
		}
		return shares;
	}

	public List<SharePrice> getSharePrices(ShareId shareId, Date startDate, Date endDate) throws JsonProcessingException, IOException {
		List<SharePrice> sharePrices = new ArrayList<>();
		URL url = new URL("http://ichart.yahoo.com/table.csv?s=" + shareId);
		Iterators.addAll(sharePrices, mapper.reader(SharePrice.class).with(sharePriceSchema).<SharePrice>readValues(url));
		return sharePrices;
	}
}
