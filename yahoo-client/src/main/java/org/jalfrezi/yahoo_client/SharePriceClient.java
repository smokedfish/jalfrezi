package org.jalfrezi.yahoo_client;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

import org.jalfrezi.datamodel.SharePrice;
import org.jalfrezi.datamodel.id.ShareId;
import org.joda.time.DateTime;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.google.common.collect.Iterators;

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

	public SharePriceClient() {
	}
	
	public List<SharePrice> getSharePrices(ShareId shareId, DateTime startDate, DateTime endDate) throws JsonProcessingException, IOException {
		List<SharePrice> sharePrices = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		sb.append("?").append("s=").append(shareId);
		sb.append("&").append("a=").append(startDate.getMonthOfYear() - 1);
		sb.append("&").append("b=").append(startDate.getDayOfMonth());
		sb.append("&").append("c=").append(startDate.getYear());
		sb.append("&").append("d=").append(endDate.getMonthOfYear() - 1);
		sb.append("&").append("e=").append(endDate.getDayOfMonth());
		sb.append("&").append("f=").append(endDate.getYear());
		URL url = new URL("http://ichart.yahoo.com/table.csv" + sb.toString());
		Iterators.addAll(sharePrices, mapper.reader(SharePrice.class).with(sharePriceSchema).<SharePrice>readValues(url));
		return sharePrices;
	}
}
