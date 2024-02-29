package org.georchestra.photosobliques.service.config.webclient;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;

/**
 * @author FNI18300
 *
 */
public abstract class AbstractWebClientConfig {

	protected ObjectMapper createObjectMapper() {
		// On crée un nouvel opbjectMapper
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
		objectMapper.configure(DeserializationFeature.ACCEPT_FLOAT_AS_INT, true);
		return objectMapper;
	}

	protected ExchangeStrategies createStrategies(ObjectMapper objectMapper) {
		return ExchangeStrategies.builder()
				.codecs(clientDefaultCodecsConfigurer -> clientDefaultCodecsConfigurer.defaultCodecs()
						.jackson2JsonDecoder(new Jackson2JsonDecoder(objectMapper, MediaType.APPLICATION_JSON)))
				.build();
	}
}
