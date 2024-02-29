/**
 *
 */
package org.georchestra.photosobliques.storage.entity.acl;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.georchestra.photosobliques.core.common.SchemaConstants;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author FNI18300
 *
 */
@Entity
@Table(name = "refresh_token", schema = SchemaConstants.DATA_SCHEMA)
@Getter
@Setter
@ToString
public class RefreshTokenEntity implements Serializable {

	private static final long serialVersionUID = -1061569294666044902L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "token", length = 2048, nullable = false)
	private String token;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(getToken());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		RefreshTokenEntity other = (RefreshTokenEntity) obj;
		return Objects.equals(getToken(), other.getToken());
	}

}
