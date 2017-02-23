package com.epay.epayApp.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {

	public static ObjectMapper objectMapper;
	static {
		JsonFactory factory = new JsonFactory();
		objectMapper = new ObjectMapper(factory);
	}

	public static <T> T deserializeEntity(String responseData, Class<T> clazz) throws IOException {
		T value = objectMapper.readValue(responseData, clazz);
		return value;
	}

}
