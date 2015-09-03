package org.jalfrezi.yahoo_client;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.jalfrezi.datamodel.SharePrice;
import org.jalfrezi.datamodel.id.ShareId;
import org.jalfrezi.datamodel.id.SharePriceId;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Days;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.datatype.joda.JodaModule;

@Named
public class SharePriceClient {
	private final CsvMapper mapper = new CsvMapper();

	private CsvSchema sharePriceSchema = CsvSchema.builder()
	        .addColumn("date")
	        .addColumn("open")
	        .addColumn("high")
	        .addColumn("low")
	        .addColumn("close")
	        .addColumn("volume")
	        .addColumn("adjClose")
	        .build()
	        .withHeader();

	private final PageFetcher pageFetcher;

	@Inject
	public SharePriceClient(PageFetcher pageFetcher) {
		this.pageFetcher = pageFetcher;
		this.mapper.registerModule(new JodaModule());
	}

	public List<SharePrice> getSharePrices(ShareId shareId, DateTime startDate, DateTime endDate) throws JsonProcessingException, IOException {
		List<SharePrice> sharePrices = new ArrayList<>();
		if (inRange(startDate, endDate)) {
			StringBuilder sb = new StringBuilder();
			sb.append("?").append("s=").append(shareId);
			sb.append("&").append("a=").append(startDate.getMonthOfYear() - 1);
			sb.append("&").append("b=").append(startDate.getDayOfMonth());
			sb.append("&").append("c=").append(startDate.getYear());
			sb.append("&").append("d=").append(endDate.getMonthOfYear() - 1);
			sb.append("&").append("e=").append(endDate.getDayOfMonth());
			sb.append("&").append("f=").append(endDate.getYear());
			URL url = new URL("http://ichart.yahoo.com/table.csv" + sb.toString());

			MappingIterator<SharePrice> iterator = mapper.reader(SharePrice.class).with(sharePriceSchema).<SharePrice>readValues(pageFetcher.getStream(url));
			while(iterator.hasNext()) {
				SharePrice sharePrice = iterator.next();
				sharePrice.setShareId(shareId);
				sharePrice.setSharePriceId(makeShareId(sharePrice));
				sharePrices.add(sharePrice);
			}
		}
		return sharePrices;
	}

	public static boolean inRange(DateTime start, DateTime end) {
		int days = Days.daysBetween(start, end).getDays();
		int endDayOfWeek = end.getDayOfWeek();
		if (days <= 0) {
			return false;
		}
		if (days == 1) {
			return DateTimeConstants.SUNDAY != endDayOfWeek && DateTimeConstants.SATURDAY != endDayOfWeek;
		}
		if (days == 2) {
			return DateTimeConstants.SUNDAY != endDayOfWeek;
		}
		return true;
	}

	private SharePriceId makeShareId(SharePrice sharePrice) {
		DateTime date = sharePrice.getDate();
		StringBuilder sb = new StringBuilder();
		sb.append(date.getYear()).append("-");
		if (date.getMonthOfYear() < 10) {
			sb.append("0");
		}
		sb.append(date.getMonthOfYear()).append("-");
		if (date.getDayOfMonth() < 10) {
			sb.append("0");
		}
		sb.append(date.getDayOfMonth());
		SharePriceId sharePriceId = new SharePriceId(sb.toString());
		return sharePriceId;
	}
}
