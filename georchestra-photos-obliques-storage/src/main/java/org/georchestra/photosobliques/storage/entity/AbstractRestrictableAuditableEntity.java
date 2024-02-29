/**
 *
 */
package org.georchestra.photosobliques.storage.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import org.georchestra.photosobliques.core.common.Restrictable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * @author FNI18300
 *
 */
@MappedSuperclass
@Setter
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractRestrictableAuditableEntity extends AbstractAuditableEntity implements Restrictable {

	private static final long serialVersionUID = -507465889634656994L;

	@Column(name = "restricted", nullable = false)
	private boolean restricted;

	/**
	 * Constructeur par copie
	 *
	 * @param source
	 */
	protected AbstractRestrictableAuditableEntity(AbstractRestrictableAuditableEntity source) {
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
