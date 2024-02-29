package org.georchestra.photosobliques.core.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.openapitools.jackson.nullable.JsonNullableModule;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 *
 * @author FNI18300
 *
 */
public class DefaultJackson2ObjectMapperBuilder extends Jackson2ObjectMapperBuilder {

	public DefaultJackson2ObjectMapperBuilder() {
		this(new JavaTimeModule());
	}

	public DefaultJackson2ObjectMapperBuilder(JavaTimeModule javaTimeModule) {
		this.modules(new Jdk8Module(), javaTimeModule, new ParameterNamesModule(), new JsonNullableModule())
				// On évite de se retrouver avec des Dates sous forme de tableaux
				// https://stackoverflow.com/a/60570542
				.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
				// On ignore les propriétés nulles
				.serializationInclusion(JsonInclude.Include.NON_NULL);
	}
}
