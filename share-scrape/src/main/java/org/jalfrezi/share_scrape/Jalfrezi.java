package org.jalfrezi.share_scrape;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

import org.jalfrezi.share_scrape.Share.SharePrice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class Jalfrezi {
	private static final Logger LOGGER = LoggerFactory.getLogger(Jalfrezi.class);
	private static final String SHARE_SUFFIX = ".json";

	private final ShareTranscoder shareTranscoder = new ShareTranscoder();
	private final ShareReader shareReader;
	private final File directory;

	private Jalfrezi(ShareReader shareReader, File directory) {
		this.shareReader = shareReader;
		this.directory = directory;
	}

	public void parse() throws IOException {
		final Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 4);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		shareReader.parse(new ShareReader.Listener() {
			public void share(String name, String symbol, double price, int volume, double change, double low, double high) throws IOException {
				File shareFile = new File(directory, symbol + SHARE_SUFFIX);
				Share share;
				if (shareFile.exists()) {
					share = read(shareFile);
				}
				else {
					share = new Share(name, symbol);
				}
				updateSharePrice(share, new SharePrice(price, volume, change, low, high));
				write(share, shareFile);
			}

			private void updateSharePrice(Share share, SharePrice sharePrice) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	private Share read(final File file) throws IOException, JsonGenerationException, JsonMappingException {
		FileReader reader = new FileReader(file);
		try {
			return shareTranscoder.read(reader);
		}
		finally {
			reader.close();
		}
	}

	private void write(Share share, final File file) throws IOException, JsonGenerationException, JsonMappingException {
		FileWriter writer = new FileWriter(file);
		try {
			shareTranscoder.write(writer, share);
		}
		finally {
			writer.close();
		}
	}

	static Jalfrezi parseArgs(String... args) throws MalformedURLException {
		if (args == null) {
			throw new IllegalArgumentException("missing arguments");			
		}
		File directory = null;
		URL url = null;
		File file = null;
		for (int i = 0 ; i < args.length; i++) {
			if ("-dir".equals(args[i])) {
				if (i < args.length - 1) {
					directory = new File(args[i+1]);
				}
				else {
					throw new IllegalArgumentException("-dir missing argument");					
				}
			}
			if (i < args.length - 1 && "-url".equals(args[i])) {
				if (i < args.length - 1) {
					url = new URL(args[i+1]);
				}
				else {
					throw new IllegalArgumentException("-url missing argument");					
				}
			}
			if (i < args.length - 1 && "-file".equals(args[i])) {
				if (i < args.length - 1) {
					file = new File(args[i+1]);
				}
				else {
					throw new IllegalArgumentException("-file missing argument");					
				}
			}
		}
		if (directory == null) {
			throw new IllegalArgumentException("-dir missing argument");					
		}
		if (!directory.isDirectory()) {
			throw new IllegalArgumentException("-dir " + directory + " is unreadable");
		}
		if (url == null && file == null) {
			throw new IllegalArgumentException("-url or -file missing argument");
		}
		if (url != null) {
			return new Jalfrezi(new ShareReader(url), directory);
		}
		return new Jalfrezi(new ShareReader(file), directory);
	}

	public static void main(String[] args) {
		try {
			Jalfrezi jalfrezi = Jalfrezi.parseArgs(args);
			jalfrezi.parse();
		} catch (IOException e) {
			LOGGER.error("Failed", e);
		}
	}
}
