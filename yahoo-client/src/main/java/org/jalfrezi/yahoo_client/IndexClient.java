package org.jalfrezi.yahoo_client;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.jalfrezi.datamodel.id.IndexId;
import org.jalfrezi.datamodel.id.ShareId;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@Named
public class IndexClient {
	private final String URL_BASE = "https://uk.finance.yahoo.com";

	private final PageFetcher pageFetcher;

	@Inject
	public IndexClient(PageFetcher pageFetcher) {
		this.pageFetcher = pageFetcher;
	}

	public Set<ShareId> findByIndexId(IndexId indexId) throws IOException {
		Set<ShareId> shareIds = new HashSet<>();
		String query = "/q/cp?s=" + URLEncoder.encode(indexId.getId(), "UTF-8");
		while(query != null && query.length() > 0) {
			URL url = new URL(URL_BASE + query);
			Document doc = Jsoup.parse(pageFetcher.getStream(url), "UTF-8", URL_BASE);
			if (doc == null) {
				break;
			}
			Elements nexts = doc.getElementsContainingOwnText("Next");
			query = nexts.get(0).attr("href");

			Elements yfncTabledata1 = doc.getElementsByClass("yfnc_tabledata1");
			for (Element row : yfncTabledata1) {
				Elements hrefs = row.getElementsByTag("a");
				if (hrefs != null && hrefs.size() > 0) {
					String symbol = hrefs.text().trim();
					shareIds.add(new ShareId(symbol));
				}
			}
		}
		return shareIds;
	}
}
