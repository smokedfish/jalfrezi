package org.jalfrezi.share_scrape;

import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Share {

	public static class SharePrice {
		private final double price;
		private final int volume;
		private final double low;
		private final double high;
		private final double change;

		@JsonCreator
		public SharePrice(@JsonProperty("price") double price, @JsonProperty("volume") int volume, @JsonProperty("low") double low, @JsonProperty("high") double high, @JsonProperty("change") double change) {
			super();
			this.price = price;
			this.volume = volume;
			this.low = low;
			this.high = high;
			this.change = change;
		}

		public double getPrice() {
			return price;
		}

		public int getVolume() {
			return volume;
		}

		public double getLow() {
			return low;
		}

		public double getHigh() {
			return high;
		}

		public double getChange() {
			return change;
		}
	}

	private final String name;
	private final String symbol;
	private final SortedMap<Date, SharePrice> prices = new TreeMap<Date, SharePrice>();

	@JsonCreator
	public Share(@JsonProperty("name") String name, @JsonProperty("symbol") String symbol) {
		this.name = name;
		this.symbol = symbol;
	}

	public String getName() {
		return name;
	}

	public String getSymbol() {
		return symbol;
	}

	@JsonProperty("prices")
	public SortedMap<Date, SharePrice> getPrices() {
		return prices;
	}
}