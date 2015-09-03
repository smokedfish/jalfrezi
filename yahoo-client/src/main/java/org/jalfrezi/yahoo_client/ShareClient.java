package org.jalfrezi.yahoo_client;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.jalfrezi.datamodel.Share;
import org.jalfrezi.datamodel.id.IndexId;
import org.jalfrezi.datamodel.id.ShareId;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.google.common.base.Joiner;

@Named
public class ShareClient {
	private final int chunk = 50;
	private final CsvMapper mapper = new CsvMapper();
	private final CsvSchema shareSchema = CsvSchema.builder()
	        .addColumn("indexId") //x
	        .addColumn("shareId") //s
	        .addColumn("name") //n
	        .build();

	private final PageFetcher pageFetcher;

	@Inject
	public ShareClient(PageFetcher pageFetcher) {
		this.pageFetcher = pageFetcher;
	}

	public Set<Share> getShares(IndexId indexId, Collection<ShareId> shareIds) throws JsonProcessingException, IOException {
		List<ShareId> shareIdList = new ArrayList<>(shareIds);
		Set<Share> shares = new HashSet<>(shareIds.size());
		for (int s = 0, e = chunk; s < shareIds.size(); s += chunk, e += chunk) {
			List<ShareId> subShareIds = shareIdList.subList(s, Math.min(e, shareIdList.size()));
			String shareIdsQuery = Joiner.on(",").join(subShareIds);
			URL url = new URL("http://download.finance.yahoo.com/d/quotes.csv?s=" + shareIdsQuery + "&f=xsn&e=.csv");
			MappingIterator<Share> it = mapper.reader(Share.class).with(shareSchema).<Share>readValues(pageFetcher.getStream(url));
			while (it.hasNext()) {
				shares.add(it.next().setIndexId(indexId));
			}
		}
		return shares;
	}
}
