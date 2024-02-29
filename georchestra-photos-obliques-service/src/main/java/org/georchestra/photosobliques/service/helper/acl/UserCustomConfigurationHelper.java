/**
 * SHOM - Search
 */
package org.georchestra.photosobliques.service.helper.acl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.georchestra.photosobliques.core.bean.UserCustomConfiguration;
import org.georchestra.photosobliques.storage.entity.acl.UserCustomConfigurationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

/**
 * Gestion de la configuration utilisateur
 * @author FNI18300
 *
 */
@Component
@Slf4j
public class UserCustomConfigurationHelper {

	private static final String DEFAULT_CUSTOM_CONFIGURATION = "default_custom_configuration.json";

	@Autowired
	private ObjectMapper objectMapper;

	private ObjectNode defaultConfiguration = null;

	/**
	 *
	 * @return default configuration (load it if needed)
	 */
	public ObjectNode getDefaultCustomConfiguration() {
		if (defaultConfiguration == null) {
			defaultConfiguration = loadDefaultCustomConfiguration();
		}
		return defaultConfiguration;
	}

	/**
	 * MErge the user custom configuration into the default one
	 *
	 * @param userCustomConfiguration
	 * @return user custom configuration
	 */
	public UserCustomConfiguration mergCustomConfiguration(UserCustomConfigurationEntity userCustomConfiguration) {
		try {
			return mergCustomConfiguration(
					userCustomConfiguration != null ? userCustomConfiguration.getProperties() : "{}");
		} catch (Exception e) {
			return new UserCustomConfiguration();
		}
	}

	public UserCustomConfiguration mergCustomConfiguration(String properties) throws IOException {
		ObjectNode target = getDefaultCustomConfiguration().deepCopy();
		JsonNode sink = loadProperties(properties);

		Iterator<String> it = sink.fieldNames();
		while (it.hasNext()) {
			String name = it.next();
			JsonNode node = sink.get(name);
			target.replace(name, node);
		}

		return objectMapper.readerFor(UserCustomConfiguration.class).readValue(target.toString());
	}

	public String serializeCustomConfiguration(UserCustomConfiguration userCustomConfiguration) {
		String result = "{}";
		if (userCustomConfiguration != null) {
			try {
				result = objectMapper.writeValueAsString(userCustomConfiguration);
			} catch (Exception e) {
				log.warn("Impossible de serialiser le configuration", e);
			}
		}
		return result;
	}

	protected ObjectNode loadDefaultCustomConfiguration() {
		ObjectNode result = null;
		try (InputStream defaultCustomConfigurationStream = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(DEFAULT_CUSTOM_CONFIGURATION);) {
			result = objectMapper.reader().readValue(defaultCustomConfigurationStream, ObjectNode.class);
		} catch (Exception e) {
			log.warn("Impossible de charger le fichier de configuration par défaut", e);
		}
		return result;
	}

	protected JsonNode loadProperties(String properties) throws IOException {
		JsonNode sink = null;
		if (StringUtils.isNotEmpty(properties)) {
			sink = objectMapper.reader().readValue(properties, ObjectNode.class);
		} else {
			sink = objectMapper.createObjectNode();
		}
		return sink;
	}

}
