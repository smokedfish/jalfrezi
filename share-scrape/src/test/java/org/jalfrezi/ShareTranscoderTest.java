package org.jalfrezi;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Date;

import org.jalfrezi.Share.SharePrice;
import org.junit.Assert;
import org.junit.Test;

public class ShareTranscoderTest {
	
	@Test
	public void testEncodeDecode() throws InterruptedException, IOException {
		String json = "{\"name\":\"E2v Tech\",\"symbol\":\"E2V\",\"prices\":{\"20131207\":{\"price\":153.75,\"volume\":32507,\"low\":0.0,\"high\":153.0,\"change\":155.0}}}";
		ShareTranscoder shareTranscoder = new ShareTranscoder();
		Share share = shareTranscoder.read(new StringReader(json));
		share.getPrices().put(new Date(1385455299945L), new SharePrice(1.2, 2, 1.1, 1.3, 0.1));
		StringWriter stringWriter = new StringWriter();
		shareTranscoder.write(stringWriter, share);
		Assert.assertEquals("{\"name\":\"E2v Tech\",\"symbol\":\"E2V\",\"prices\":{\"20131207\":{\"price\":153.75,\"volume\":32507,\"low\":0.0,\"high\":153.0,\"change\":155.0},\"20131126\":{\"price\":1.2,\"volume\":2,\"low\":1.1,\"high\":1.3,\"change\":0.1}}}", stringWriter.toString());
	}
}
