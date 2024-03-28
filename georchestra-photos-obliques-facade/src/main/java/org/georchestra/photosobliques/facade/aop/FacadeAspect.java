package org.georchestra.photosobliques.facade.aop;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.hc.core5.http.HttpStatus;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.georchestra.photosobliques.core.security.AuthenticatedUser;
import org.georchestra.photosobliques.service.helper.acl.ACLHelper;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
@AllArgsConstructor
public class FacadeAspect {

	private static final Map<Class<?>, Method> PAGE_RESULT_MAP = new HashMap<>();

	private static final Map<Class<?>, Method> GET_RESTRICTED_MAP = new HashMap<>();

	private static final Map<Class<?>, Method> UUID_MAP = new HashMap<>();

	private final ACLHelper utilContextHelper;

	// Pour chaque entrée dans un controller
	@Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
	public void businessMethods() {
		// Nothing to do
	}

	@Around("businessMethods()")
	public Object profile(final ProceedingJoinPoint pjp) throws Throwable {
		Object output = pjp.proceed();

		final AuthenticatedUser authenticatedUser = utilContextHelper.getAuthenticatedUser();
		final String accountname = authenticatedUser != null ? authenticatedUser.getLogin() : "ANONYMOUS";
		if (log.isInfoEnabled()) {
			log.info(String.format("by(%s) - %s.%s(%s)", accountname, pjp.getSignature().getDeclaringType().getName(),
					pjp.getSignature().getName(), Arrays.toString(pjp.getArgs())));
		}
		if (authenticatedUser != null && !authenticatedUser.isRestricted()
				&& output instanceof @SuppressWarnings("rawtypes") ResponseEntity response) {
			Object body = response.getBody();
			if (body != null) {
				// données non nulle et utilisateur authentifié sans accès aux données
				// restreintes
				try {
					if (body instanceof Collection) {
						handleCollection((Collection<?>) body);
					} else if (body.getClass().getName().endsWith("PageResult")) {
						handleCollection(lookupItems(body));
					} else if (hasRestricted(body)) {
						handleRestrictedItem(body);
					}
				} catch (InvalidDataAccessApiUsageException e) {
					log.error("ALERT user " + accountname + " try to access restricted data", e);
					output = ResponseEntity.status(HttpStatus.SC_FORBIDDEN).headers(response.getHeaders()).build();
				}
			}
		}
		return output;
	}

	private void handleRestrictedItem(Object restrictedItem) {
		Method m = GET_RESTRICTED_MAP.get(restrictedItem.getClass());
		if (m != null) {
			try {
				Boolean value = Boolean.class.cast(m.invoke(restrictedItem));
				if (value == null || Boolean.TRUE.equals(value)) {
					// il doit toujours y avoir cette information
					final AuthenticatedUser authenticatedUser = utilContextHelper.getAuthenticatedUser();
					builError(authenticatedUser, restrictedItem, null);
				}
			} catch (InvalidDataAccessApiUsageException e1) {
				throw e1;
			} catch (Exception e) {
				final AuthenticatedUser authenticatedUser = utilContextHelper.getAuthenticatedUser();
				builError(authenticatedUser, restrictedItem, e);
			}
		}
	}

	private void builError(final AuthenticatedUser authenticatedUser, Object restrictedItem, Exception e) {
		throw new InvalidDataAccessApiUsageException(String.format("User %s try to access restricted data %s",
				authenticatedUser.getLogin(), lookupIdentifier(restrictedItem)), e);
	}

	private void handleCollection(Collection<?> items) {
		if (CollectionUtils.isNotEmpty(items)) {
			for (Object item : items) {
				if (hasRestricted(item)) {
					handleRestrictedItem(item);
				}
			}
		}
	}

	private boolean hasRestricted(Object object) {
		try {
			Method m = null;
			if (GET_RESTRICTED_MAP.containsKey(object.getClass())) {
				m = GET_RESTRICTED_MAP.get(object.getClass());
			} else {
				m = object.getClass().getMethod("getRestricted");
				GET_RESTRICTED_MAP.put(object.getClass(), m);
			}
			return m != null;
		} catch (Exception e) {
			log.warn("Can't get getRestricted method", e);
		}
		return false;
	}

	private List<?> lookupItems(Object pageResultObject) {
		try {
			Method m = null;
			if (PAGE_RESULT_MAP.containsKey(pageResultObject.getClass())) {
				m = PAGE_RESULT_MAP.get(pageResultObject.getClass());
			} else {
				m = pageResultObject.getClass().getMethod("getElements");
				PAGE_RESULT_MAP.put(pageResultObject.getClass(), m);
			}
			if (m != null) {
				return List.class.cast(m.invoke(pageResultObject));
			}
		} catch (Exception e) {
			log.warn("Can't get elements method", e);
		}
		return List.of();
	}

	private String lookupIdentifier(Object item) {
		try {
			Method m = null;
			if (UUID_MAP.containsKey(item.getClass())) {
				m = UUID_MAP.get(item.getClass());
			} else {
				m = item.getClass().getMethod("getUuid");
				UUID_MAP.put(item.getClass(), m);
			}
			Object value = m.invoke(item);
			if (value != null) {
				return value.toString();
			}
		} catch (Exception e) {
			log.warn("Can't get getUuid method", e);
		}

		return "unkown identifier";

	}

}
