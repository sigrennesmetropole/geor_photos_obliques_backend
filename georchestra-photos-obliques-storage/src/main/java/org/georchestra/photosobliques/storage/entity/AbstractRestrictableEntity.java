package org.georchestra.photosobliques.storage.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.georchestra.photosobliques.core.common.Restrictable;

/**
 * @author FNI18300
 */
@MappedSuperclass
@Setter
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public abstract class AbstractRestrictableEntity extends AbstractLongIdEntity implements Restrictable {

	private static final long serialVersionUID = 7110297013421909363L;

	@Column(name = "restricted", nullable = false)
	private boolean restricted;

	/**
	 * Constructeur par copie
	 *
	 * @param source source du clonage pour compatibilité et distinction avec le
	 *               constructeur sans paramètre
	 */
	protected AbstractRestrictableEntity(AbstractRestrictableEntity source) {
		super(source);
		setRestricted(source.isRestricted());
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		return getClass() == obj.getClass();
	}

}
