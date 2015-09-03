package org.jalfrezi.yahoo_client;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.inject.Named;

@Named
public class PageFetcher {

	public PageFetcher() {
	}

	public InputStream getStream(URL url) throws IOException {
		return url.openStream();
	}
}
