package org.jalfrezi.datamodel;

import org.jalfrezi.datamodel.id.ShareId;
import org.jalfrezi.datamodel.id.SharePriceId;
import org.joda.time.DateTime;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public class SharePrice {
	private ShareId shareId;
	private SharePriceId sharePriceId;
	private DateTime date;
	private double open;
	private double close;
	private double high;
	private double low;
	private int volume;
	private double adjClose;

	public SharePrice() {
	}

	public ShareId getShareId() {
		return shareId;
	}

	public SharePrice setShareId(ShareId shareId) {
		this.shareId = shareId;
		return this;
	}

	public SharePriceId getSharePriceId() {
		return sharePriceId;
	}

	public SharePrice setSharePriceId(SharePriceId sharePriceId) {
		this.sharePriceId = sharePriceId;
		return this;
	}

	public DateTime getDate() {
		return date;
	}

	public SharePrice setDate(DateTime date) {
		this.date = date;
		return this;

	}

	public double getOpen() {
		return open;
	}

	public SharePrice setOpen(double open) {
		this.open = open;
		return this;
	}

	public double getClose() {
		return close;
	}

	public SharePrice setClose(double close) {
		this.close = close;
		return this;
	}

	public double getHigh() {
		return high;
	}

	public SharePrice setHigh(double high) {
		this.high = high;
		return this;
	}

	public double getLow() {
		return low;
	}

	public SharePrice setLow(double low) {
		this.low = low;
		return this;
	}

	public int getVolume() {
		return volume;
	}

	public SharePrice setVolume(int volume) {
		this.volume = volume;
		return this;
	}

	public double getAdjClose() {
		return adjClose;
	}

	public SharePrice setAdjClose(double adjClose) {
		this.adjClose = adjClose;
		return this;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(shareId,
				sharePriceId,
				date,
				open,
				close,
				high,
				low,
				volume,
				adjClose);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		SharePrice that = (SharePrice) obj;
		return Objects.equal(this.shareId, that.shareId)
				&& Objects.equal(this.sharePriceId, that.sharePriceId)
				&& Objects.equal(this.date, that.date)
				&& Objects.equal(this.open, that.open)
				&& Objects.equal(this.close, that.close)
				&& Objects.equal(this.high, that.high)
				&& Objects.equal(this.low, that.low)
				&& Objects.equal(this.volume, that.volume)
				&& Objects.equal(this.adjClose, that.adjClose);
	}

	public String toString() {
		return MoreObjects.toStringHelper(this.getClass())
				.add("shareId", shareId)
				.add("sharePriceId", sharePriceId)
				.add("date", date)
				.add("open", open)
				.add("close", close)
				.add("high", high)
				.add("low", low)
				.add("volume", volume)
				.add("adjClose", adjClose)
				.toString();
	}
}
