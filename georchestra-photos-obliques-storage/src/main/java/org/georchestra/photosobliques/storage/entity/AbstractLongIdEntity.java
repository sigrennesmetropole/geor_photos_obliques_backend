package org.georchestra.photosobliques.storage.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.georchestra.photosobliques.core.common.LongId;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author FNI18300
 */
@MappedSuperclass
@Setter
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public abstract class AbstractLongIdEntity implements LongId, Serializable {

	private static final long serialVersionUID = 7110297013421909363L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "uuid", nullable = false, unique = true)
	private UUID uuid;

	/**
	 * Constructeur par copie
	 *
	 * @param source source du clonage pour compatibilité et distinction avec le
	 *               constructeur sans paramètre
	 */
	protected AbstractLongIdEntity(AbstractLongIdEntity source) {
		super();
		setUuid(UUID.randomUUID());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof AbstractLongIdEntity)) {
			return false;
		}
		AbstractLongIdEntity other = (AbstractLongIdEntity) obj;
		return (getId() != null && getId().equals(other.getId()));
	}
}
