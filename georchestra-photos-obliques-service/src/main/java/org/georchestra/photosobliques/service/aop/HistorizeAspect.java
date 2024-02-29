package org.georchestra.photosobliques.service.aop;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.georchestra.photosobliques.core.security.AuthenticatedUser;
import org.georchestra.photosobliques.core.util.ApplicationContext;
import org.georchestra.photosobliques.service.helper.acl.ACLHelper;
import org.georchestra.photosobliques.storage.entity.AbstractAuditableEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Slf4j
@Component
public class HistorizeAspect {

	private ACLHelper utilContextHelper;

	@Pointcut("within(@org.springframework.stereotype.Repository *) or bean(org.georchestra.photosobliques.*Repository)")
	public void repositoryMethods() {
		// Nothing to do
	}

	@Around("repositoryMethods()")
	public Object repositoryMethods(ProceedingJoinPoint pjp) throws Throwable {
		return pjp.proceed();
	}

	@Before("within(@org.springframework.stereotype.Repository *) or bean(org.georchestra.photosobliques.*Repository)")
	public void beforeRepositoryMethods(JoinPoint pjp) {
		Object[] args = pjp.getArgs();
		if (pjp.getSignature().getName().startsWith("save") && ArrayUtils.isNotEmpty(args) && args.length == 1) {
			beforeMethods(pjp, args);
		}
	}

	/**
	 * Prise en compte du point de jointure des mathodes save
	 *
	 * @param pjp  le point de jointure
	 * @param args les arguments
	 */
	protected void beforeMethods(JoinPoint pjp, Object[] args) {
		if (args[0] instanceof AbstractAuditableEntity auditable) {
			if (auditable.getCreationDate() == null) {
				auditable.setCreationDate(LocalDateTime.now());
				auditable.setUpdatedDate(auditable.getCreationDate());
			} else if (auditable.getUpdatedDate() == null) {
				auditable.setUpdatedDate(LocalDateTime.now());
			}
			String fullname = "anonymous";
			if (getUtilContextHelper() != null) {
				AuthenticatedUser user = utilContextHelper.getAuthenticatedUser();
				if (user != null) {
					fullname = user.getFirstname() + " " + user.getLastname();
				}
			}
			if (auditable.getFullnameCreatedBy() == null) {
				auditable.setFullnameCreatedBy(fullname);
			}
			if (auditable.getFullnameUpdatedBy() == null) {
				auditable.setFullnameUpdatedBy(fullname);
			}
		}
	}

	private ACLHelper getUtilContextHelper() {
		if (utilContextHelper == null) {
			try {
				utilContextHelper = ApplicationContext.getBean(ACLHelper.class);
			} catch (Exception e) {
				log.warn("Failed to get utilContextHelper", e);
			}
		}
		return utilContextHelper;
	}
}
