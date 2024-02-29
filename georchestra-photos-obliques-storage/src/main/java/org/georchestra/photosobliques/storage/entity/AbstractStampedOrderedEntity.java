package org.georchestra.photosobliques.storage.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.georchestra.photosobliques.core.common.Ordered;
import org.georchestra.photosobliques.core.common.Stamped;

import java.io.Serializable;

/**
 * Entité technique administrable via un BackOffice
 * @author FNI18300
 */
@MappedSuperclass
@Setter
@Getter
@ToString
public abstract class AbstractStampedOrderedEntity extends AbstractStampedEntity implements Stamped, Ordered, Serializable {

	private static final long serialVersionUID = 2106712668832334687L;

	@Column(name = "order_", nullable = false)
	private int order;

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}
}
