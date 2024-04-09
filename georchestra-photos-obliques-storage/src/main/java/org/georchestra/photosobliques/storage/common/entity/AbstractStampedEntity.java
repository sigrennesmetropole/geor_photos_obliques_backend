package org.georchestra.photosobliques.storage.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.georchestra.photosobliques.core.common.Stamped;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Entité technique administrable via un BackOffice
 * @author FNI18300
 */
@MappedSuperclass
@Setter
@Getter
@ToString
public abstract class AbstractStampedEntity extends AbstractLabelizedEntity implements Stamped, Serializable {

	private static final long serialVersionUID = 2106712668832334687L;

	@Column(name = "opening_date", nullable = false)
	private LocalDateTime openingDate;

	@Column(name = "closing_date")
	private LocalDateTime closingDate;

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}
}
