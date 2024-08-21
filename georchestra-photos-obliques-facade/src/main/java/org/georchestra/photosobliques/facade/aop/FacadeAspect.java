package org.georchestra.photosobliques.facade.aop;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.georchestra.photosobliques.core.bean.User;
import org.georchestra.photosobliques.core.bean.statistiques.Statistiques;
import org.georchestra.photosobliques.facade.controller.phototheque.OwnersApiController;
import org.georchestra.photosobliques.facade.controller.phototheque.PhotosApiController;
import org.georchestra.photosobliques.facade.controller.phototheque.ProvidersApiController;
import org.georchestra.photosobliques.facade.controller.phototheque.YearsApiController;
import org.georchestra.photosobliques.service.exception.AppServiceException;
import org.georchestra.photosobliques.service.helper.acl.ACLHelper;
import org.georchestra.photosobliques.service.sm.statistiques.StatistiquesServices;
import org.georchestra.photosobliques.service.sm.statistiques.visitor.StatistiquesVisitor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.*;

@Aspect
@Component
@Slf4j
@AllArgsConstructor
public class FacadeAspect {

	private static final List<Class<?>> ALLOWED_CLASSES = List.of(OwnersApiController.class, PhotosApiController.class,
			ProvidersApiController.class, YearsApiController.class);

	private final ACLHelper utilContextHelper;

	private StatistiquesServices statistiquesServices;

	private List<StatistiquesVisitor> visitors;

	private final Map<String, List<String>> methodParametersMap = new HashMap<>();
	private final Map<String, String> methodPathMap = new HashMap<>();


	// Pour chaque entrée dans un controller
	@Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
	public void businessMethods() {
		// Nothing to do
	}

	@Around("businessMethods()")
	public Object profile(final ProceedingJoinPoint pjp) throws Throwable {

		Object output;
		final User authenticatedUser = utilContextHelper.getAuthenticatedUser();
		long startTimeMs = System.currentTimeMillis();
		try {
			output = pjp.proceed();

			final String accountName = authenticatedUser != null ? authenticatedUser.getLogin() : "ANONYMOUS";

			if (log.isInfoEnabled()) {
				log.info(String.format("by(%s) - %s.%s(%s)", accountName, pjp.getSignature().getDeclaringType().getName(),
						pjp.getSignature().getName(), Arrays.toString(pjp.getArgs())));
			}
		} catch (AppServiceException appServiceException) {
			output = appServiceException;
		}

		storeAPIStatistics(pjp, output, authenticatedUser, System.currentTimeMillis() - startTimeMs);
		if (output instanceof AppServiceException serviceException) {
			throw serviceException;
		}
		return output;
	}


	private void storeAPIStatistics(ProceedingJoinPoint pjp, Object object, User authenticatedUser, Long serviceDuration) {

		Statistiques statistiques = new Statistiques();
		statistiques.setWho(authenticatedUser != null ? authenticatedUser.getLogin() : "ANONYMOUS");
		statistiques.setWhen(LocalDateTime.now());
		statistiques.setDuration(serviceDuration);

		//récupération de la target et de la méthode
		Object target = pjp.getTarget();
		MethodSignature signature = (MethodSignature) pjp.getSignature();

		if (ALLOWED_CLASSES.contains(target.getClass())) {

			//extraction des paramètres et du path
			Method method = findMethod(target, signature.getName());
			if (method != null) {
				List<String> parameters = getMethodParametersName(method);
				String query = mapParameters(parameters, pjp.getArgs());
				statistiques.setQuery(query);
				statistiques.setUrl(getMethodPath(method));
			}

			//génération des données supplémentaires en fonction de la méthode appelée
			visitors.stream().findFirst().filter(visitor -> visitor.accept(signature.getName()))
					.ifPresent(visitor -> statistiques.setData(visitor. process(object)));

			if (object instanceof ResponseEntity<?> response) {
				statistiques.setResult(response.getStatusCode().toString());
			} else if (object instanceof AppServiceException exception) {
				statistiques.setResult(exception.getAppExceptionStatusCode().getStringValue());
			}
			statistiquesServices.saveStatistiques(statistiques);
		}
	}

	private Method findMethod(Object target, String methodName) {
		Class<?>[] interfaces = target.getClass().getInterfaces();
		return Arrays.stream(interfaces[0].getDeclaredMethods()).filter(method -> method.getName().equals(methodName)).findFirst().orElse(null);
	}


	/**
	 * Extrait le nom des paramètre d'une méthode dans l'ordre dans lequel ils ont été déclarés
	 *
	 * @param method la methode dont on souhaite extraire les paramètres
	 * @return la liste des paramètres
	 */
	private List<String> getMethodParametersName(Method method) {
		if (methodParametersMap.get(method.getName()) != null && !methodParametersMap.get(method.getName()).isEmpty()) {
			return methodParametersMap.get(method.getName());
		}
		List<String> parameters = new ArrayList<>();
		try {
			parameters = Arrays.stream(method.getParameters()).map(p -> p.getAnnotation(io.swagger.v3.oas.annotations.Parameter.class).name()).toList();
		} catch (Exception e) {
			log.warn("Les paramètres de la méthode {} n'ont pas pu être récupérés", method.getName());
		}
		methodParametersMap.put(method.getName(), parameters);
		return parameters;
	}

	/**
	 * Extrait le path d'une méthode
	 *
	 * @param method la methode dont on souhaite extraire les paramètres
	 * @return la liste des paramètres
	 */
	private String getMethodPath(Method method) {
		if (methodPathMap.get(method.getName()) != null && StringUtils.isNotEmpty(methodPathMap.get(method.getName()))) {
			return methodPathMap.get(method.getName());
		}
		try {
			RequestMapping mapping = (RequestMapping) Arrays.stream(method.getDeclaredAnnotations()).filter(RequestMapping.class::isInstance).findFirst().orElse(null);
			if (mapping != null) {
				methodPathMap.put(method.getName(), mapping.value()[0]);
				return mapping.value()[0];
			}
		} catch (Exception e) {
			log.warn("Les paramètres de la méthode {} n'ont pas pu être récupérés", method.getName());
		}
		return "";
	}

	private String mapParameters(List<String> parameters, Object[] args) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < parameters.size(); i++) {
			stringBuilder.append(parameters.get(i))
					.append("=")
					.append((args[i] != null) ? args[i] : "null");
			if (i < parameters.size() - 1) {
				stringBuilder.append(", ");
			}
		}
		return stringBuilder.toString();
	}
}
