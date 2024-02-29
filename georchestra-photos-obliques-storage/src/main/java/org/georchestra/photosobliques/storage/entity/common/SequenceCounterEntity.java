/**
 * SHOM - Search
 */
package org.georchestra.photosobliques.storage.entity.common;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.georchestra.photosobliques.core.common.SchemaConstants;
import org.georchestra.photosobliques.storage.entity.AbstractLongIdEntity;

/**
 * Sequence pour l'assignation de référence
 *
 * @author FNI18300
 *
 */
@Entity
@Table(name = "sequence_counter", schema = SchemaConstants.DATA_SCHEMA)
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SequenceCounterEntity extends AbstractLongIdEntity {

	private static final long serialVersionUID = 6101134559807985372L;

	@Column(name = "code", nullable = false, length = 50)
	private String code;

	@Column(name = "annee", nullable = false)
	private int annee;

	@Version
	@Column(name = "value_", nullable = false)
	private long value;
}
