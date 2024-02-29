package org.georchestra.photosobliques.storage.entity.acl;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.georchestra.photosobliques.core.common.SchemaConstants;
import org.georchestra.photosobliques.storage.entity.AbstractLongIdEntity;

@Entity
@Table(name = "role_feature", schema = SchemaConstants.DATA_SCHEMA)
@Getter
@Setter
@ToString
public class RoleFeatureEntity extends AbstractLongIdEntity {

	private static final long serialVersionUID = -5673157586856745693L;

	@ManyToOne
	@JoinColumn(name = "feature_fk")
	private FeatureEntity feature;

	@Column(name = "scope", length = 50)
	@Enumerated(EnumType.STRING)
	private FeatureScope scope;

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}
}
