package org.jalfrezi;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class ShareTranscoder {
	private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
	
	private static class DateAsTimestampSerializer extends JsonSerializer<Date> {
		@Override
		public void serialize(Date value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
			jgen.writeFieldName(DATE_FORMAT.format(value));
		}
	}
	
	private static class DateAsTimestampDeserializer extends KeyDeserializer {
		@Override
		public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException, JsonProcessingException {
			try {
				return DATE_FORMAT.parseObject(key);
			} catch (ParseException e) {
				throw new JsonParseException(e.getMessage(), ctxt.getParser().getCurrentLocation());
			}
		}
	}

	private final ObjectMapper mapper;
	
	public ShareTranscoder() {
		SimpleModule module = new SimpleModule();
		module.addKeySerializer(Date.class, new DateAsTimestampSerializer());
		module.addKeyDeserializer(Date.class, new DateAsTimestampDeserializer());
		this.mapper = new ObjectMapper().registerModule(module);
		mapper.getFactory().disable(Feature.AUTO_CLOSE_TARGET);
	}
	
	public Share read(Reader reader) throws JsonProcessingException, IOException {
		return mapper.readValue(reader, Share.class);
	}
	
	public void write(Writer writer, Share share) throws JsonGenerationException, JsonMappingException, IOException {
		mapper.writeValue(writer, share);
	}
}
