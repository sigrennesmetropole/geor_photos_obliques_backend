package org.georchestra.photosobliques.facade.configuration.json;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.georchestra.photosobliques.core.json.DefaultJackson2ObjectMapperBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.TimeZone;

/**
 *
 * @author FNI18300
 *
 */
@Component
public class DefaultJacksonConfig {

	@Value("${photos_obliques.timezone.default:Europe/Paris}")
	private String defaultTimeZone;

	@Bean
	public Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
		Jackson2ObjectMapperBuilder builder = new DefaultJackson2ObjectMapperBuilder(javaTimeModule());
		builder.timeZone(TimeZone.getTimeZone(defaultTimeZone));
		return builder;
	}

	protected JavaTimeModule javaTimeModule() {
		JavaTimeModule module = new JavaTimeModule();
		module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(buildDateTimeFormatter()));
		return module;
	}

	protected DateTimeFormatter buildDateTimeFormatter() {
		return new DateTimeFormatterBuilder().parseCaseInsensitive().append(DateTimeFormatter.ISO_LOCAL_DATE)
				.appendLiteral('T').append(DateTimeFormatter.ISO_LOCAL_TIME).optionalStart().appendLiteral('Z')
				.optionalEnd().toFormatter();
	}
}
