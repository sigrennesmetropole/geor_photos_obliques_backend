package org.georchestra.photosobliques.storage.entity.acl;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.georchestra.photosobliques.core.common.SchemaConstants;
import org.georchestra.photosobliques.storage.entity.AbstractStampedOrderedEntity;

@Entity
@Table(name = "feature", schema = SchemaConstants.DATA_SCHEMA)
@Getter
@Setter
@ToString
public class FeatureEntity extends AbstractStampedOrderedEntity {

	private static final long serialVersionUID = -5673157586856745693L;

	@Column(name="available_scope", length = 50, nullable = false)
	@Enumerated(EnumType.STRING)
	private FeatureScope availablescope;

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}
}
