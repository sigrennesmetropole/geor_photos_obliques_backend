/**
 *
 */
package org.georchestra.photosobliques.storage.entity.async;

/**
 * @author FNI18300
 *
 */
public enum AsyncStatus {

	IN_PROGRESS, DONE, ERROR;

	public static AsyncStatus getInitialStatus() {
		return IN_PROGRESS;
	}
}
