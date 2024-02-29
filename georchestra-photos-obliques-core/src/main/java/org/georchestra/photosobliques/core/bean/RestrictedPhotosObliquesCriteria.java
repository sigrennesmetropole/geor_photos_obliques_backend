package org.georchestra.photosobliques.core.bean;

/**
 * @author FNI18300
 *
 */
public interface RestrictedPhotosObliquesCriteria {

	/**
	 * si restricted = false cela signifie que seul est les éléments non restreints
	 * (NP) sont accessible <br/>
	 * si restricted = true cela signie que tous les éléments NP + DR sont
	 * accessibles
	 */
	boolean isRestricted();

	void setRestricted(boolean restricted);

}
