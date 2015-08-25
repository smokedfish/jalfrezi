package org.jalfrezi.yahoo_client;

import java.io.IOException;
import java.util.List;

import org.jalfrezi.datamodel.SharePrice;
import org.jalfrezi.datamodel.id.ShareId;
import org.joda.time.DateTime;

import com.fasterxml.jackson.core.JsonProcessingException;

public class TestClient {

	public static void main(String[] args) throws JsonProcessingException, IOException {
		SharePriceClient sharePriceClient = new SharePriceClient();
		List<SharePrice> sharePrices = sharePriceClient.getSharePrices(new ShareId("RSW.L"), new DateTime(2015, 8, 14, 0, 0 ,0), new DateTime());
		for (SharePrice sharePrice : sharePrices) {
			System.out.println(sharePrice);
		}
	}
}
