/**
 *
 */
package org.georchestra.photosobliques.storage.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import org.georchestra.photosobliques.core.common.Auditable;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;

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
public abstract class AbstractAuditableEntity extends AbstractLongIdEntity implements Auditable, Serializable {

	private static final long serialVersionUID = 3642617461998137595L;

	@Column(name = "creation_date", nullable = false)
	@CreatedDate
	private LocalDateTime creationDate;

	@Column(name = "updated_date", nullable = false)
	@LastModifiedDate
	private LocalDateTime updatedDate;

	@Column(name = "created_by", nullable = false)
	@CreatedBy
	private String createdBy;

	@Column(name = "fullname_created_by", nullable = false)
	private String fullnameCreatedBy;

	@Column(name = "updated_by", nullable = false)
	@LastModifiedBy
	private String updatedBy;

	@Column(name = "fullname_updated_by", nullable = false)
	private String fullnameUpdatedBy;

	/**
	 * Constructeur par copie
	 *
	 * @param source
	 */
	protected AbstractAuditableEntity(AbstractAuditableEntity source) {
		super(source);
		setUpdatedDate(source.getUpdatedDate());
		setCreationDate(source.getCreationDate());
		setCreatedBy(source.getCreatedBy());
		setUpdatedBy(source.getUpdatedBy());
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
