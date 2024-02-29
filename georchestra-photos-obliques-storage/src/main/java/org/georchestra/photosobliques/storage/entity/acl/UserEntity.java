package org.georchestra.photosobliques.storage.entity.acl;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.georchestra.photosobliques.core.common.SchemaConstants;
import org.georchestra.photosobliques.storage.entity.AbstractLongIdEntity;

/**
 * User entity
 */
@Entity
@Table(name = "user_", schema = SchemaConstants.DATA_SCHEMA)
@Getter
@Setter
@ToString
public class UserEntity extends AbstractLongIdEntity {

	private static final long serialVersionUID = -6508639499690690560L;

	@Column(name = "login", length = SchemaConstants.LOGIN_LENTH, nullable = false)
	private String login;

	@Column(name = "type", nullable = false)
	@Enumerated(EnumType.STRING)
	private UserType type;

	@Column(name = "lastname", length = 30)
	private String lastname;

	@Column(name = "firstname", length = 30)
	private String firstname;

	@Column(name = "email", length = 255)
	private String email;

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof UserEntity)) {
			return false;
		}
		UserEntity other = (UserEntity) obj;
		if (getId() != null && getId().equals(other.getId())) {
			return true;
		}
		if (getLogin() == null) {
			if (other.getLogin() != null) {
				return false;
			}
		} else if (!getLogin().equals(other.getLogin())) {
			return false;
		}
		return true;
	}

}
