/**
 *
 */
package org.georchestra.photosobliques.facade.configuration.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.georchestra.photosobliques.core.bean.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;

/**
 *
 * @author FNI18300
 *
 */
public class PreAuthenticationFilter extends OncePerRequestFilter implements Filter {

	private static final Logger LOGGER = LoggerFactory.getLogger(PreAuthenticationFilter.class);

	public static final String SEC_USERNAME = "sec-username";
	public static final String SEC_ROLES = "sec-roles";
	public static final String SEC_ORG = "sec-org";
	public static final String SEC_EMAIL = "sec-email";
	public static final String SEC_ORGNAME = "sec-orgname";
	public static final String SEC_TEL = "sec-tel";
	public static final String SEC_PROXY = "sec-proxy";
	public static final String SEC_LASTNAME = "sec-lastname";
	public static final String SEC_FIRSTNAME = "sec-firstname";

	// Controle des patterns des URL
	private AntPathMatcher pathMatcher;


	// Liste des URL à exclure
	private Collection<String> excludeUrlPatterns;



	/**
	 *
	 * @param excludeUrlPatterns
	 */
	public PreAuthenticationFilter(final String[] excludeUrlPatterns) {
		super();
		this.excludeUrlPatterns = Arrays.asList(excludeUrlPatterns);
		pathMatcher = new AntPathMatcher();
	}

	@Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
								 jakarta.servlet.FilterChain filterChain)
			throws ServletException, IOException {

		if (LOGGER.isInfoEnabled()) {
			Enumeration<String> names = request.getHeaderNames();
			while (names.hasMoreElements()) {
				LOGGER.info("header:{}", names.nextElement());
			}
		}
		final String username = request.getHeader(SEC_USERNAME);
		if (username != null) {
			SecurityContextHolder.getContext().setAuthentication(createAuthentication(request));

			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("Populated SecurityContextHolder with pre-auth token: '{}'",
						SecurityContextHolder.getContext().getAuthentication());
			}
		} else {
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("SecurityContextHolder not populated with pre-auth token");
			}
		}
		filterChain.doFilter(request, response);
	}

	/**
	 * Construction du token pre-authentification
	 *
	 * @param httpServletRequest
	 * @return
	 */
	private Authentication createAuthentication(HttpServletRequest httpServletRequest) {
		final String username = httpServletRequest.getHeader(SEC_USERNAME);
		final String rolesString = httpServletRequest.getHeader(SEC_ROLES);
		Set<String> rolesSet = new LinkedHashSet<>();
		List<String> roles = null;
		if (rolesString != null) {
			roles = Arrays.asList(rolesString.split(";"));
			rolesSet.addAll(roles);
		}
		try {

				User user = new User();
				user.setLogin(username);
				assignUserData(user, httpServletRequest, roles);

			return new PreAuthenticationToken(username, user, rolesSet);
		} catch (Exception e) {
			LOGGER.debug("Mise à jour ou création d'utilisateur en erreur :{}", e.getMessage());
			User user = new User();
			user.setLogin(username);
			assignUserData(user, httpServletRequest, roles);
			return new PreAuthenticationToken(username, user, rolesSet);
		}
	}

	private boolean assignUserData(User user, HttpServletRequest httpServletRequest, List<String> roles) {
		boolean update = false;
		String email = httpServletRequest.getHeader(SEC_EMAIL);
		if (StringUtils.isNotEmpty(email) && !email.equals(user.getEmail())) {
			user.setEmail(email);
			update = true;
		}
		String firstname = httpServletRequest.getHeader(SEC_FIRSTNAME);
		if (StringUtils.isNotEmpty(firstname) && !firstname.equals(user.getFirstname())) {
			user.setFirstname(firstname);
			update = true;
		}
		String lastname = httpServletRequest.getHeader(SEC_LASTNAME);
		if (StringUtils.isNotEmpty(lastname) && !lastname.equals(user.getLastname())) {
			user.setLastname(lastname);
			update = true;
		}
		String organization = httpServletRequest.getHeader(SEC_ORGNAME);
		if (StringUtils.isNotEmpty(organization) && !organization.equals(user.getOrganization())) {
			user.setOrganization(organization);
			update = true;
		}
		if ((roles != null && !roles.equals(user.getRoles())) || (roles == null && user.getRoles() != null)) {
			user.setRoles(roles);
			update = true;
		}
		return update;
	}

	@Override
	protected boolean shouldNotFilter(final HttpServletRequest request) throws ServletException {
		// Contrôle si l'URL n'est pas dans le liste d'exclusion. Si c'est le cas, elle
		// ne passera pas dans ce filtre
		return excludeUrlPatterns.stream().anyMatch(p -> pathMatcher.match(p, request.getServletPath()));
	}
}
