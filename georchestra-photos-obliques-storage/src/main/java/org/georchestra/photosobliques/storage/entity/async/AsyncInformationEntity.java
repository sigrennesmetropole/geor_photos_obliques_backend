package org.georchestra.photosobliques.storage.entity.async;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.georchestra.photosobliques.core.common.SchemaConstants;
import org.georchestra.photosobliques.storage.entity.AbstractLongIdEntity;

import java.util.Date;

/**
 * Classes des Information asynchrone.<br/>
 *
 */
@Entity
@Table(name = "async_information", schema = SchemaConstants.DATA_SCHEMA)
@Getter
@Setter
@ToString
public class AsyncInformationEntity extends AbstractLongIdEntity {

	private static final long serialVersionUID = 8246461244235164376L;

	@Column(name = "login", nullable = false, length = 50)
	private String login;

	@Column(name = "scope", length = 255)
	private String scope;

	@Column(name = "progression", nullable = false)
	private int progression;

	@Column(name = "status", nullable = false, length = 20)
	@Enumerated(EnumType.STRING)
	private AsyncStatus status;

	@Column(name = "creation_date", nullable = false)
	private Date creationDate;

	@Column(name = "message", length = 1024)
	private String message;

	@Column(name = "content_type", length = 100)
	private String contentType;

	@Column(name = "file_name", length = 255)
	private String fileName;

	@Column(name = "file_path", length = 255)
	private String filePath;

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof AsyncInformationEntity)) {
			return false;
		}
		return super.equals(obj);
	}

}
