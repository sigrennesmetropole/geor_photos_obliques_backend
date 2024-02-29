package org.georchestra.photosobliques.facade.configuration.security;

import jakarta.servlet.Filter;
import org.apache.commons.lang3.ArrayUtils;
import org.georchestra.photosobliques.facade.configuration.BasicSecurityConstants;
import org.georchestra.photosobliques.facade.configuration.filter.PreAuthenticationFilter;
import org.georchestra.photosobliques.facade.configuration.filter.PreAuthenticationProvider;
import org.georchestra.photosobliques.facade.configuration.security.permission.FeaturePermissionEvaluator;
import org.georchestra.photosobliques.service.sm.acl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {


	@Autowired
	UserService userService;

	private static final String[] AUTHENTICATION_PERMIT_URL = { "/login/oauth2/code/**",
			"/oauth2/authorization/lemonng" };

	private static final String[] SB_PERMIT_ALL_URL = {
			//
			SecurityConstants.AUTHENTICATE_URL,
			// URL public
			SecurityConstants.CONFIGURATION_URL, SecurityConstants.HEALTH_CHECK_URL, SecurityConstants.GEOSERVER_URL,
			// swagger ui / openapi
			BasicSecurityConstants.V3_API_DOCS_URL, BasicSecurityConstants.SWAGGER_RESOURCES_URL,
			BasicSecurityConstants.SWAGGER_UI_HTML_URL, BasicSecurityConstants.SWAGGER_UI_URL,
			BasicSecurityConstants.CONFIGURATION_UI_URL, BasicSecurityConstants.CONFIGURATION_SECURITY_URL,
			BasicSecurityConstants.WEBJARS_URL, BasicSecurityConstants.ERROR_URL };

	@Value("${security.authentication.disabled:false}")
	private boolean disableAuthentification = false;

	@Value("${security.authentication.pre.disabled:true}")
	private boolean disablePreAuthentication = true;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http)
			throws Exception {
		if (!disableAuthentification) {
			http.cors().and().authorizeHttpRequests()
					.requestMatchers(SB_PERMIT_ALL_URL).permitAll()
					// On autorise certains WS utilisés pour l'authent
					.requestMatchers("/photos-obliques/**").permitAll()
					.requestMatchers("/administration/**").fullyAuthenticated().and().httpBasic().and()
					.addFilterAfter(createPreAuthenticationFilter(), BasicAuthenticationFilter.class).sessionManagement()
					.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().csrf().disable();
			if (!disablePreAuthentication) {
				http.addFilterAfter(createPreAuthenticationFilter(), BasicAuthenticationFilter.class);
			}
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
		configuration.setAllowedOriginPatterns(Arrays.asList("*"));

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
		return new PreAuthenticationFilter(ArrayUtils.addAll(SB_PERMIT_ALL_URL, AUTHENTICATION_PERMIT_URL), userService);
	}

	@Bean
	protected MethodSecurityExpressionHandler createExpressionHandler() {
		DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
		expressionHandler.setPermissionEvaluator(new FeaturePermissionEvaluator());
		return expressionHandler;
	}

	@Bean(BeanIds.USER_DETAILS_SERVICE)
	public UserDetailsService userDetailsServiceBean() {
		return new UserDetailServiceImpl();
	}

}
