package org.georchestra.photosobliques.facade.configuration.security;

import jakarta.servlet.Filter;
import org.apache.commons.lang3.ArrayUtils;
import org.georchestra.photosobliques.facade.configuration.BasicSecurityConstants;
import org.georchestra.photosobliques.facade.configuration.filter.PreAuthenticationFilter;
import org.georchestra.photosobliques.facade.configuration.filter.PreAuthenticationProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {



	private static final String[] SB_PERMIT_ALL_URL = {
			// URL public
			SecurityConstants.CONFIGURATION_URL, SecurityConstants.HEALTH_CHECK_URL,
			// swagger ui / openapi
			BasicSecurityConstants.V3_API_DOCS_URL, BasicSecurityConstants.SWAGGER_RESOURCES_URL,
			BasicSecurityConstants.SWAGGER_UI_HTML_URL, BasicSecurityConstants.SWAGGER_UI_URL,
			BasicSecurityConstants.CONFIGURATION_UI_URL, BasicSecurityConstants.CONFIGURATION_SECURITY_URL,
			BasicSecurityConstants.WEBJARS_URL, BasicSecurityConstants.ERROR_URL };

	@Value("${security.authentication.disabled:false}")
	private boolean disableAuthentification = false;


	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http)
			throws Exception {
		if (!disableAuthentification) {
			http.cors().and().authorizeHttpRequests()
					.requestMatchers(SB_PERMIT_ALL_URL).permitAll()
					.anyRequest().fullyAuthenticated().and()
					.addFilterAfter(createPreAuthenticationFilter(), BasicAuthenticationFilter.class).sessionManagement()
					.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().csrf().disable();
		} else {
			http.cors().and().csrf().disable().authorizeHttpRequests().anyRequest().permitAll();
		}
		return http.build();
	}

	@Bean
	protected CorsConfigurationSource corsConfigurationSource() {
		final CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS", "PUT", "DELETE"));
		configuration.addAllowedHeader("*");
		configuration.setAllowCredentials(true);

		// Url autorisées
		// 4200 pour les développement | 8080 pour le déploiement
		configuration.setAllowedOriginPatterns(List.of("*"));

		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("admin").password("{noop}4dM1nApp!").roles("ADMIN");
		auth.authenticationProvider(createPreAuthenticationProvider());
	}

	private AuthenticationProvider createPreAuthenticationProvider() {
		return new PreAuthenticationProvider();
	}


	@Bean
	protected GrantedAuthorityDefaults grantedAuthorityDefaults() {
		// Remove the ROLE_ prefix
		return new GrantedAuthorityDefaults("");
	}

	@Bean
	protected Filter createPreAuthenticationFilter() {
		return new PreAuthenticationFilter(ArrayUtils.addAll(SB_PERMIT_ALL_URL));
	}

}
