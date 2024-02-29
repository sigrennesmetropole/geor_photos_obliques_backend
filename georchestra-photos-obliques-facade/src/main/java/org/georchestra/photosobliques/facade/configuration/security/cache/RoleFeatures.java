/**
 * SHOM - Search
 */
package org.georchestra.photosobliques.facade.configuration.security.cache;

import lombok.Data;
import org.georchestra.photosobliques.core.bean.Feature;
import org.georchestra.photosobliques.core.bean.FeatureScope;
import org.georchestra.photosobliques.core.bean.Role;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author FNI18300
 *
 */
@Data
public class RoleFeatures implements Serializable {

	private static final long serialVersionUID = -6324270989600466881L;

	private Role role;

	private Map<String, Feature> features = new HashMap<>();

	private Map<String, FeatureScope> featureScopes = new HashMap<>();
	
	private Map<String, String> featureScopeValues = new HashMap<>();

	public RoleFeatures(Role role) {
		this.role = role;
	}

	public void putFeature(Feature feature, FeatureScope scope) {
		features.put(feature.getCode(), feature);
		FeatureScope actualScope = featureScopes.get(feature.getCode());
		if (actualScope == null
				|| (actualScope == FeatureScope.READ
						&& (scope == FeatureScope.READ_WRITE || scope == FeatureScope.READ_WRITE_DELETE))
				|| (actualScope == FeatureScope.READ_WRITE && scope == FeatureScope.READ_WRITE_DELETE)) {
			featureScopes.put(feature.getCode(), scope);
			featureScopeValues.put(feature.getCode(), scope.name());
		}
	}
}
