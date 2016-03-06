package org.jalfrezi.server;

import java.io.IOException;

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

import org.jalfrezi.datamodel.id.Id;
import org.jalfrezi.datamodel.id.IndexId;
import org.jalfrezi.datamodel.id.ShareId;
import org.jalfrezi.datamodel.id.SharePriceId;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

public class JacksonFeature implements Feature {

	public class NumericIdSerializer<T extends Id<? extends Number>> extends JsonSerializer<T> {
		@Override
		public void serialize(T value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
			jgen.writeNumber(value.getId().longValue());
		}
	}

	public class StringIdSerializer<T extends Id<? extends String>> extends JsonSerializer<T> {
		@Override
		public void serialize(T value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
			jgen.writeString(value.getId().toString());
		}
	}

	@Override
	public boolean configure(FeatureContext context) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JodaModule());
		// We want ISO dates, not Unix timestamps!:
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

		SimpleModule module = new SimpleModule();
		module.addSerializer(ShareId.class, new StringIdSerializer<ShareId>());
		module.addSerializer(SharePriceId.class, new StringIdSerializer<SharePriceId>());
		module.addSerializer(IndexId.class, new StringIdSerializer<IndexId>());
		mapper.registerModule(module);

		JacksonJaxbJsonProvider provider = new JacksonJaxbJsonProvider();
		provider.setMapper(mapper);

		context.register(provider);
		return true;
	}
}