package org.georchestra.photosobliques.storage.aop;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.georchestra.photosobliques.core.security.AuthenticatedUser;
import org.georchestra.photosobliques.storage.entity.AbstractLongIdEntity;
import org.georchestra.photosobliques.storage.entity.historic.HistoricDataEntity;
import org.georchestra.photosobliques.storage.entity.historic.HistoricDataType;
import org.springframework.boot.jackson.JsonComponentModule;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
public class HistoricDataHelper {

	private static final String UNKNOWN = "unknown";
	private ObjectMapper objectMapper;

	/**
	 * Création d'un objet historique pour une entité
	 *
	 * @param entity
	 * @param type
	 * @return
	 */
	public HistoricDataEntity createHistoricData(AbstractLongIdEntity entity, HistoricDataType type) {
		return createHistoricData(entity.getUuid(), entity.getClass(), serialize(entity), type);
	}

	/**
	 * Création d'un objet historique à partir de la descrition
	 *
	 * @param owner
	 * @param ownerClass
	 * @param data
	 * @param type
	 * @return
	 */
	public HistoricDataEntity createHistoricData(UUID owner, Class<?> ownerClass, String data, HistoricDataType type) {
		HistoricDataEntity historicDataEntity = new HistoricDataEntity();
		historicDataEntity.setUuid(UUID.randomUUID());
		historicDataEntity.setWhen(LocalDateTime.now());
		historicDataEntity.setWhat(type);
		historicDataEntity.setWho(getWho());
		historicDataEntity.setFullWho(getFullWho());
		historicDataEntity.setWhich(owner);
		historicDataEntity.setWhichType(ownerClass.getName());
		historicDataEntity.setData(data);
		return historicDataEntity;
	}

	public ObjectMapper getObjectMapper() {
		if (objectMapper == null) {

			JsonMapper.Builder builder = JsonMapper.builder();

			builder.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
			builder.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
			builder.configure(SerializationFeature.WRITE_DATES_WITH_ZONE_ID, true);
			builder.configure(SerializationFeature.INDENT_OUTPUT, false);
			builder.configure(SerializationFeature.WRITE_SELF_REFERENCES_AS_NULL, true);
			builder.configure(MapperFeature.USE_STD_BEAN_NAMING, true);
			builder.serializationInclusion(Include.NON_NULL);
			SerializerFactory factory = BeanSerializerFactory.instance
					.withSerializerModifier(new HistoricSerializerModifier());
			builder.serializerFactory(factory);
			builder.addModule(new JavaTimeModule());
			builder.addModule(new JsonComponentModule());
			objectMapper = builder.build();
		}
		return objectMapper;
	}

	public String serialize(AbstractLongIdEntity entity) {
		try {
			return getObjectMapper().writerFor(entity.getClass()).writeValueAsString(entity);
		} catch (Exception e) {
			log.warn("Failed to serialize data...", e);
			return "{}";
		}
	}

	protected String getWho() {
		String result = UNKNOWN;
		final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			result = (auth.getPrincipal() != null) ? auth.getPrincipal().toString() : UNKNOWN;
		}
		return result;
	}

	protected String getFullWho() {
		String result = UNKNOWN;
		final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			result = computeFullName(auth);
		}
		return result;
	}

	protected String computeFullName(Authentication auth) {
		StringBuilder buffer = new StringBuilder();
		if (auth.getDetails() instanceof AuthenticatedUser user) {
			if (StringUtils.isNotEmpty(user.getFirstname())) {
				buffer.append(user.getFirstname());
			}
			if (StringUtils.isNotEmpty(user.getLastname())) {
				if (!buffer.isEmpty()) {
					buffer.append(' ');
				}
				buffer.append(user.getLastname());
			}
		}
		if (buffer.isEmpty()) {
			buffer.append(getWho());
		}
		return buffer.toString();
	}
}
