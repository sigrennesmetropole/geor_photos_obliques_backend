package org.georchestra.photosobliques.storage.entity.acl;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.georchestra.photosobliques.core.common.SchemaConstants;
import org.georchestra.photosobliques.storage.entity.AbstractStampedOrderedEntity;

import java.util.Set;

@Entity
@Table(name = "role", schema = SchemaConstants.DATA_SCHEMA)
@Getter
@Setter
@ToString
public class RoleEntity extends AbstractStampedOrderedEntity {

	private static final long serialVersionUID = -5673157586856745693L;

	@Column(name="code_ldap", length = 100)
	private String codeLdap;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="role_fk")
	private Set<RoleFeatureEntity> roleFeatures;

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}
}
