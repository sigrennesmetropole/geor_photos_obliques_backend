package org.georchestra.photosobliques.storage.entity.acl;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.georchestra.photosobliques.core.common.SchemaConstants;
import org.georchestra.photosobliques.storage.entity.AbstractLongIdEntity;

@Entity
@Table(name = "user_custom_configuration", schema = SchemaConstants.DATA_SCHEMA)
@Getter
@Setter
@ToString
@EqualsAndHashCode(exclude = "properties", callSuper = true)
public class UserCustomConfigurationEntity extends AbstractLongIdEntity {

	private static final long serialVersionUID = 8003153212905689476L;

	@Column(name = "login", nullable = false, length = SchemaConstants.LOGIN_LENTH)
	private String login;

	@Column(name = "properties", columnDefinition = "TEXT")
	private String properties;
}
