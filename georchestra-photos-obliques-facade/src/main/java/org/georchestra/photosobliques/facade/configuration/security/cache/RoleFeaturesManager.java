/**
 * SHOM - Search
 */
package org.georchestra.photosobliques.facade.configuration.security.cache;

import lombok.extern.slf4j.Slf4j;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.Status;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.xml.XmlConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.net.URL;
import java.time.Duration;

/**
 * @author FNI18300
 *
 */
@Component
@Slf4j
public class RoleFeaturesManager {

	public static final String ROLE_FEATURES_MANAGER_NAME = "roleFeatureCacheManager";

	public static final String ROLE_FEATURES_CACHE_NAME = "roleFeatureCache";

	public static final String ROLE_FEATURES_CACHE = "rolefeatures";

	private static final String CONFIG_FILE_PATH = "photos-obliques-ehcache.xml";

	private CacheManager cacheManager;

	@Bean(name = ROLE_FEATURES_MANAGER_NAME)
	public CacheManager cacheManager() {
		final URL myUrl = Thread.currentThread().getContextClassLoader().getResource(CONFIG_FILE_PATH);
		if (myUrl == null) {
			throw new IllegalArgumentException(
					String.format("Impossible de trouver le fichier de configuration EhCache : %s", CONFIG_FILE_PATH));
		}
		XmlConfiguration xmlConfig = new XmlConfiguration(myUrl, Thread.currentThread().getContextClassLoader());
		cacheManager = CacheManagerBuilder.newCacheManager(xmlConfig);
		cacheManager.init();

		return cacheManager;

	}

	protected Cache<String, RoleFeatures> cache() {
		try {
			Cache<String, RoleFeatures> cache = cacheManager.getCache(ROLE_FEATURES_CACHE, String.class,
					RoleFeatures.class);
			if (cache == null) {
				throw new IllegalArgumentException("Erreur lors de la configuration du cache client_registration");
			}
			return cache;
		} catch (Exception e) {
			// petite tambouille pour supporter les redémarrage springboot en mode
			// developpement
			cacheManager.removeCache(ROLE_FEATURES_CACHE);
			ResourcePoolsBuilder resourcePoolsBuilder = ResourcePoolsBuilder.newResourcePoolsBuilder().heap(50,
					EntryUnit.ENTRIES);
			CacheConfigurationBuilder<String, RoleFeatures> builder = CacheConfigurationBuilder
					.newCacheConfigurationBuilder(String.class, RoleFeatures.class, resourcePoolsBuilder)
					.withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofMinutes(15)));
			return cacheManager.createCache(ROLE_FEATURES_CACHE, builder);
		}

	}

	@PreDestroy
	public void preDestroyCacheManager() {
		if (cacheManager != null && cacheManager.getStatus() != Status.UNINITIALIZED) {
			try {
				cacheManager.close();
			} catch (Exception e) {
				log.warn("Failed to destroy cache", e);
			}
			cacheManager = null;
		}
	}

	public RoleFeatures lookupRoleFeatures(String roleCode) {
		return cache().get(roleCode);
	}

	public void storeRoleFeatures(RoleFeatures roleFeatures) {
		if (roleFeatures == null || roleFeatures.getRole() == null) {
			return;
		}
		cache().put(roleFeatures.getRole().getCode(), roleFeatures);
	}
}
