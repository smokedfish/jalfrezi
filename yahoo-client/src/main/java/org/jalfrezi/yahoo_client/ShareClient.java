package org.jalfrezi.yahoo_client;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

import org.jalfrezi.datamodel.Share;
import org.jalfrezi.datamodel.id.IndexId;
import org.jalfrezi.datamodel.id.ShareId;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterators;

@Named
public class ShareClient {
	private final int chunk = 50;
	private final CsvMapper mapper = new CsvMapper();
	private final CsvSchema shareSchema = CsvSchema.builder()
	        .addColumn("indexId") //x
	        .addColumn("shareId") //s
	        .addColumn("name") //n
	        .build();

	public ShareClient() {
	}
	
	public List<Share> getShares(IndexId indexId, List<ShareId> shareIds) throws JsonProcessingException, IOException {
		List<Share> shares = new ArrayList<>(shareIds.size());
		for(int s = 0, e = chunk; s < shareIds.size(); s += chunk, e += chunk) {
			List<ShareId> subShareIds = shareIds.subList(s, Math.min(e, shareIds.size()));
			String shareIdsQuery = Joiner.on(",").join(subShareIds);
			URL url = new URL("http://download.finance.yahoo.com/d/quotes.csv?s=" + shareIdsQuery + "&f=xsn&e=.csv");
			MappingIterator<Share> it = mapper.reader(Share.class).with(shareSchema).<Share>readValues(url);
			while(it.hasNext()) {
				shares.add(it.next().setIndexId(indexId));
			}
		}
		return shares;
	}
}
