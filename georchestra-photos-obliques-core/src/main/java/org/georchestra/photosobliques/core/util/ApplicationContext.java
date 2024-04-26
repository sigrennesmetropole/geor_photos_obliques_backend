/**
 * SHOM - Search
 */
package org.georchestra.photosobliques.core.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author FNI18300
 *
 */
@Component
public class ApplicationContext implements ApplicationContextAware {

	private static org.springframework.context.ApplicationContext context;

	private static void initializeApplicationContext(
			org.springframework.context.ApplicationContext context) {
		ApplicationContext.context = context;
	}

	@Override
	public void setApplicationContext(org.springframework.context.ApplicationContext context)
			throws BeansException {
		initializeApplicationContext(context);
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		return (T) context.getBean(name);
	}

	public static <T> T getBean(Class<T> clazz) {
		return context.getBean(clazz);
	}

	public static String[] getBeanNames() {
		return context.getBeanDefinitionNames();
	}

}
