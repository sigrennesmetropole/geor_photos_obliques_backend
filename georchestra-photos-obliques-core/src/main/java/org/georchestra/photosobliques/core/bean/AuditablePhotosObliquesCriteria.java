/**
 *
 */
package org.georchestra.photosobliques.core.bean;

/**
 * @author FNI18300
 *
 */
public interface AuditablePhotosObliquesCriteria {

	String getCreatedBy();

	void setCreatedBy(String createdBy);

	String getLastModifiedBy();

	void setLastModifiedBy(String lastModifiedBy);
}
