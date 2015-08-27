package org.jalfrezi.yahoo_client;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

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

	public IndexClient() {
	}
	
	public List<ShareId> getShareIds(IndexId indexId) throws IOException {
		List<ShareId> symbols = new ArrayList<>();
		String query = "/q/cp?s=" + URLEncoder.encode(indexId.getId(), "UTF-8");
		while(query != null && query.length() > 0) {
			URL url = new URL(URL_BASE + query);
			Document doc = Jsoup.parse(url, 3000);
			Elements nexts = doc.getElementsContainingOwnText("Next");
			query = nexts.get(0).attr("href");

			Elements yfncTabledata1 = doc.getElementsByClass("yfnc_tabledata1");
			for (Element row : yfncTabledata1) {
				Elements hrefs = row.getElementsByTag("a");
				if (hrefs != null && hrefs.size() > 0) {
					String symbol = hrefs.text().trim();
					symbols.add(new ShareId(symbol));
				}
			}
		}
		return symbols;
	}
}
