/**
 *
 */
package org.georchestra.photosobliques.storage.aop;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.boot.Metadata;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;

/**
 * @author FNI18300
 *
 */
@Slf4j
public class HistoricIntegrator implements Integrator {

	private HistoricEventListener eventListener = new HistoricEventListener();

	public HistoricIntegrator() {
		super();
	}

	@Override
	public void integrate(Metadata metadata, SessionFactoryImplementor sessionFactory,
			SessionFactoryServiceRegistry serviceRegistry) {
		final EventListenerRegistry eventRegistry = serviceRegistry.getService(EventListenerRegistry.class);
		eventRegistry.prependListeners(EventType.PERSIST, eventListener);
		eventRegistry.prependListeners(EventType.DELETE, eventListener);
		eventRegistry.prependListeners(EventType.SAVE_UPDATE, eventListener);
		eventRegistry.prependListeners(EventType.SAVE, eventListener);
		eventRegistry.prependListeners(EventType.UPDATE, eventListener);
		eventRegistry.prependListeners(EventType.POST_DELETE, eventListener);
		eventRegistry.prependListeners(EventType.POST_UPDATE, eventListener);
		eventRegistry.prependListeners(EventType.POST_INSERT, eventListener);
		eventRegistry.prependListeners(EventType.PRE_COLLECTION_REMOVE, eventListener);
		eventRegistry.prependListeners(EventType.POST_COLLECTION_RECREATE, eventListener);
		eventRegistry.prependListeners(EventType.POST_COLLECTION_REMOVE, eventListener);
		eventRegistry.prependListeners(EventType.POST_COLLECTION_UPDATE, eventListener);

		log.info("HistoricIntegrator integrated");
	}

	@Override
	public void disintegrate(SessionFactoryImplementor sessionFactory, SessionFactoryServiceRegistry serviceRegistry) {
		log.info("HistoricIntegrator disintegrated");
	}

}
