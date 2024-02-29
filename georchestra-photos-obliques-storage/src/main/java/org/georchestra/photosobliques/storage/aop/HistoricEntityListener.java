/**
 * SHOM - Search
 */
package org.georchestra.photosobliques.storage.aop;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.georchestra.photosobliques.core.util.ApplicationContext;
import org.georchestra.photosobliques.storage.entity.AbstractLongIdEntity;
import org.georchestra.photosobliques.storage.entity.historic.HistoricDataEntity;
import org.georchestra.photosobliques.storage.entity.historic.HistoricDataType;
import org.georchestra.photosobliques.storage.repository.historic.HistoricDataRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author FNI18300
 *
 */
@Slf4j
public class HistoricEntityListener {

	private HistoricDataRepository historicDataRepository;

	private HistoricDataHelper historicDataHelper = new HistoricDataHelper();

	@Getter
	@Setter
	private boolean enabled = false;

	public HistoricEntityListener() {
		super();
	}

	@PostLoad
	public void postLoad(final AbstractLongIdEntity entity) {
		log.debug("postLoad {}", entity.getUuid());
	}

	@PostPersist
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void postPersist(final AbstractLongIdEntity entity) {
		historize(entity, HistoricDataType.CREATE);
	}

	@PostUpdate
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void postUpdate(final AbstractLongIdEntity entity) {
		historize(entity, HistoricDataType.UPDATE);
	}

	@PostRemove
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void postRemove(final AbstractLongIdEntity entity) {
		historize(entity, HistoricDataType.DELETE);
	}

	public void historize(final AbstractLongIdEntity entity, HistoricDataType type) {
		if (!enabled || entity instanceof HistoricDataEntity) {
			return;
		}
		HistoricDataEntity historicDataEntity = historicDataHelper.createHistoricData(entity, type);
		getHistoricDataRepository().save(historicDataEntity);
	}

	protected HistoricDataRepository getHistoricDataRepository() {
		if (historicDataRepository == null) {
			historicDataRepository = ApplicationContext.getBean(HistoricDataRepository.class);
		}
		return historicDataRepository;
	}

	protected EntityManager getEntityManager() {
		return ApplicationContext.getBean(EntityManager.class);
	}

}
