package org.jalfrezi.datamodel;

import java.util.Date;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public class SharePrice {
	private Date date;
	private double open;
	private double close;
	private double high;
	private double low;
	private int volume;
	private double adjClose;

	public SharePrice() {
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public double getOpen() {
		return open;
	}

	public void setOpen(double open) {
		this.open = open;
	}

	public double getClose() {
		return close;
	}

	public void setClose(double close) {
		this.close = close;
	}

	public double getHigh() {
		return high;
	}

	public void setHigh(double high) {
		this.high = high;
	}

	public double getLow() {
		return low;
	}

	public void setLow(double low) {
		this.low = low;
	}

	public int getVolume() {
		return volume;
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}

	public double getAdjClose() {
		return adjClose;
	}

	public void setAdjClose(double adjClose) {
		this.adjClose = adjClose;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(date,
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
		return Objects.equal(this.date, that.date)
				&& Objects.equal(this.open, that.open)
				&& Objects.equal(this.close, that.close)
				&& Objects.equal(this.high, that.high)
				&& Objects.equal(this.low, that.low)
				&& Objects.equal(this.volume, that.volume)
				&& Objects.equal(this.adjClose, that.adjClose);
	}

	public String toString() {
		return MoreObjects.toStringHelper(this.getClass())
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
