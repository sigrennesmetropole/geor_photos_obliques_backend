/**
 *
 */
package org.georchestra.photosobliques.core.security;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.MapUtils;

import java.util.*;

/**
 * @author FNI18300
 *
 */
@Data
@NoArgsConstructor
public class AuthenticatedUser {

	private String login;

	private UserType type;

	private String firstname;

	private String lastname;

	private String email;

	private boolean restricted;

	private String mainOrganization;

	private Set<String> organizations = new HashSet<>();

	private List<String> roles = new ArrayList<>();

	private Map<String, String> featureScopes = new HashMap<>();

	public AuthenticatedUser(AuthenticatedUser source) {
		this.login = source.login;
		this.type = source.type;
		checkType();
		this.firstname = source.firstname;
		this.lastname = source.lastname;
		this.email = source.email;
		this.restricted = source.restricted;
		this.mainOrganization = source.mainOrganization;
		this.roles.addAll(source.getRoles());
		this.organizations.addAll(source.organizations);
		this.featureScopes.putAll(source.featureScopes);
	}

	public AuthenticatedUser(String login, UserType type) {
		super();
		this.login = login;
		this.type = type;
		checkType();
	}

	public AuthenticatedUser(String login) {
		this(login, UserType.PERSON);
	}

	public boolean hasFeature(String feature) {
		return featureScopes.containsKey(feature);
	}

	public String getFeatureScope(String feature) {
		return featureScopes.get(feature);
	}

	public void addRole(String role) {
		if (!roles.contains(role)) {
			roles.add(role);
		}
	}

	/**
	 * Ajout d'une feature avec sa portée
	 *
	 * @param feature
	 * @param scope
	 */
	public void putFeature(String feature, String scope) {
		String value = featureScopes.get(feature);
		// alors on est bien d'accord qu'ici il a y un petit truc tricky
		// en effet, on compare les longueurs ce qui qui signifie qu'on part du principe
		// que le scope READ_WRITE_DELETE est plus long que READ_WRITE
		// Dans les faits, ce scope ne bougera jamais et ne sera pas enrichie
		// On ajoutera plutot un nouvelle feature
		if (value == null || value.length() < scope.length()) {
			featureScopes.put(feature, scope);
		}
	}

	/**
	 * Ajout d'une ensemble de feature avec leurs portées
	 *
	 * @param featureScopeValues
	 */
	public void putFeatures(Map<String, String> featureScopeValues) {
		if (featureScopes == null) {
			featureScopes = new HashMap<>();
		}
		if (MapUtils.isNotEmpty(featureScopeValues)) {
			for (Map.Entry<String, String> featureScopeValue : featureScopeValues.entrySet()) {
				putFeature(featureScopeValue.getKey(), featureScopeValue.getValue());
			}
		}
	}

	public void addOrganization(String organization) {
		organizations.add(organization);
	}

	private void checkType() {
		if (this.type == null) {
			this.type = UserType.PERSON;
		}
	}

}
