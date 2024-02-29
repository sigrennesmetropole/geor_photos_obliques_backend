/**
 * SHOM - Search
 */
package org.georchestra.photosobliques.storage.entity.acl;

import lombok.Getter;

/**
 * @author FNI18300
 */
@Getter
public enum FeatureScope {

	READ(0x1), READ_WRITE(0x3), READ_WRITE_DELETE(0x7);

	private final int value;

	private FeatureScope(int value) {
		this.value = value;
	}

	/**
	 * Est ce que le scope courant accepte le scope demandé
	 *
	 * @param scope le scope demandé
	 * @return oui si le scope courant est plus large ou égal au scope demandé
	 */
	public boolean accept(FeatureScope scope) {
		return value <= scope.value;
	}

}
