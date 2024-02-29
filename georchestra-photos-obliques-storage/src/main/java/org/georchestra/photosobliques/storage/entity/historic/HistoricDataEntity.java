/**
 * SHOM - Search
 */
package org.georchestra.photosobliques.storage.entity.historic;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.georchestra.photosobliques.core.common.SchemaConstants;
import org.georchestra.photosobliques.storage.entity.AbstractLongIdEntity;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author FNI18300
 *
 */
@Entity
@Table(name = "historic_data", schema = SchemaConstants.DATA_SCHEMA)
@Getter
@Setter
public class HistoricDataEntity extends AbstractLongIdEntity {

	private static final long serialVersionUID = 9103655656663253615L;
	
	@Column(name="who", length = 100, nullable = false)
	private String who;
	
	@Column(name="fullWho", length = 100, nullable = false)
	private String fullWho;
	
	@Column(name="when_", nullable = false)
	private LocalDateTime when;
	
	@Column(name="what", length = 50, nullable = false)
	@Enumerated(EnumType.STRING)
	private HistoricDataType what;
	
	@Column(name="which", nullable = false)
	private UUID which;
	
	@Column(name="which_type", nullable = false, length = 256)
	private String whichType;
	
	@Column(name="data", length = 4096)
	private String data;

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
		return (obj instanceof HistoricDataEntity);
	}

}
