/**
 * SHOM - Search
 */
package org.georchestra.photosobliques.core.common;

/**
 * Interface des entités à suppression logique
 *
 * @author FNI18300
 *
 */
public interface Deletable {

	boolean isDeleted();

	void setDeleted(boolean deleted);
}
