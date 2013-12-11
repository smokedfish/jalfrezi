package org.jalfrezi;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShareReader {
	private static final Logger LOGGER = LoggerFactory.getLogger(Jalfrezi.class);	
	private static final Pattern SHARE_PATTERN = Pattern.compile("(.*) \\((.*)\\)");

	private static final int NAME_SYMBOL = 0;
	private static final int PRICE = 1;
	private static final int VOLUME = 2;
	private static final int CHANGE = 3;
	private static final int LOW = 4;
	private static final int HIGH = 5;

	public interface Listener {
		void share(String name, String symbol, double price, int volume, double change, double low, double high) throws IOException;
	}

	private final URL url;
	private final File file;

	public ShareReader(File file) {
		this.url = null;
		this.file = file;
	}

	public ShareReader(URL url) {
		this.url = url;
		this.file = null;
	}

	public void parse(Listener listener) throws IOException {
		if (url != null) {
			Document doc = Jsoup.parse(url, 30000);
			parse(listener, doc);
		}
		else if (file != null) {
			Document doc = Jsoup.parse(file, "UTF-8", "http://dummy.org");
			parse(listener, doc);
		}
		else {
			LOGGER.error("Nothing to do");
		}
	}

	private void parse(Listener listener, Document doc) throws IOException {
		Elements tbodies = doc.getElementsByTag("tbody");
		if (tbodies.size() != 1) {
			throw new IllegalArgumentException("Expected only one table");
		}
		for (Element row : tbodies.get(0).children()) { // Expect only one
			Elements cells = row.children();
			Matcher matcher = SHARE_PATTERN.matcher(cells.get(NAME_SYMBOL).text());
			if (matcher.find()) {
				String name = matcher.group(1);
				String symbol = matcher.group(2);
				double price = Double.parseDouble(cells.get(PRICE).text().replaceAll(",", ""));
				int volume = Integer.parseInt(cells.get(VOLUME).text().replaceAll(",", ""));
				double change = Double.parseDouble((cells.get(CHANGE).text().substring(0, cells.get(3).text().length()-1)));
				double low = Double.parseDouble(cells.get(LOW).text().replaceAll(",", ""));
				double high = Double.parseDouble(cells.get(HIGH).text().replaceAll(",", ""));
				listener.share(name, symbol, price, volume, change, low, high);
			}
			else {
				LOGGER.error("unable to parse share symbol from " + cells.get(0).text());
			}
		}
	}
}
