/**
 * 
 */
package org.georchestra.photosobliques.storage.aop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.hibernate.HibernateException;
import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.collection.spi.PersistentSet;
import org.hibernate.event.spi.*;
import org.hibernate.metamodel.mapping.AttributeMapping;
import org.hibernate.persister.entity.EntityPersister;
import org.georchestra.photosobliques.core.util.ApplicationContext;
import org.georchestra.photosobliques.storage.entity.AbstractLongIdEntity;
import org.georchestra.photosobliques.storage.entity.historic.HistoricDataEntity;
import org.georchestra.photosobliques.storage.entity.historic.HistoricDataType;
import org.georchestra.photosobliques.storage.repository.historic.HistoricDataRepository;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author FNI18300
 *
 */
@Slf4j
public class HistoricEventListener implements PersistEventListener, DeleteEventListener, SaveOrUpdateEventListener,
		PostDeleteEventListener, PostUpdateEventListener, PostInsertEventListener, PostCollectionRecreateEventListener,
		PostCollectionRemoveEventListener, PostCollectionUpdateEventListener, PreCollectionRemoveEventListener {

	private static final String FAILED_TO_SERIALIZE_DATA_ERROR = "Failed to serialize data...";

	private HistoricDataRepository historicDataRepository;

	private HistoricDataHelper historicDataHelper = new HistoricDataHelper();

	/**
	 * A la suppression de l'entité on historize son intégralité
	 */
	@Override
	public void onPostDelete(PostDeleteEvent event) {
		if (event.getEntity() instanceof AbstractLongIdEntity longIdEntity
				&& !(event.getEntity() instanceof HistoricDataEntity)) {
			historize(longIdEntity, HistoricDataType.DELETE);
		} else if (!(event.getEntity() instanceof HistoricDataEntity)
				&& !event.getEntity().getClass().getSimpleName().contains("RefreshTokenEntity")) {
			log.debug("onPostDelete skip:" + event);
		}
	}

	/**
	 * A la création de l'entité on historize son intégralité
	 */
	@Override
	public void onPostInsert(PostInsertEvent event) {
		if (event.getEntity() instanceof AbstractLongIdEntity longIdEntity
				&& !(event.getEntity() instanceof HistoricDataEntity)) {
			historize(longIdEntity, HistoricDataType.CREATE);
		} else if (!(event.getEntity() instanceof HistoricDataEntity)) {
			log.debug("onPostInsert skip:" + event);
		}
	}

	/**
	 * A la modification on historize que ce qui change
	 */
	@Override
	public void onPostUpdate(PostUpdateEvent event) {
		log.info("onPostUpdate:" + event);
		if (event.getEntity() instanceof AbstractLongIdEntity longIdEntity
				&& !(event.getEntity() instanceof HistoricDataEntity)) {
			int[] dirtyPropertyIndexes = event.getDirtyProperties();
			Object[] oldStates = event.getOldState();
			try {
				Map<String, Object> values = new HashMap<>();
				for (int dirtyPropertyIndex : dirtyPropertyIndexes) {
					AttributeMapping attributeDefinition = lookupAttributeDefinition(event.getPersister(),
							dirtyPropertyIndex);
					if (!attributeDefinition.isPluralAttributeMapping()
							&& !attributeDefinition.getAttributeName().equalsIgnoreCase("updateDate")) {
						values.put(attributeDefinition.getAttributeName(), oldStates[dirtyPropertyIndex]);
					}
				}
				ObjectWriter objectWriter = historicDataHelper.getObjectMapper().writer();
				String data = objectWriter.writeValueAsString(values);
				historize(longIdEntity.getUuid(), longIdEntity.getClass(), data, HistoricDataType.UPDATE);
			} catch (Exception e) {
				log.warn(FAILED_TO_SERIALIZE_DATA_ERROR, e);
			}

		} else if (!(event.getEntity() instanceof HistoricDataEntity)) {
			log.info("onPostUpdate:" + event);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onPostUpdateCollection(PostCollectionUpdateEvent event) {
		if (event.getAffectedOwnerOrNull() instanceof AbstractLongIdEntity longIdEntity) {
			String role = computeRole(event.getCollection());
			try {
				Map<String, Object> values = new HashMap<>();
				Serializable snapshot = event.getCollection().getStoredSnapshot();
				if (snapshot instanceof Map && event.getCollection() instanceof PersistentSet<?> collection) {
					List<AbstractLongIdEntity> items = collectAddedItems(collection, (Map<Object, Object>) snapshot);
					if (CollectionUtils.isNotEmpty(items)) {
						values.put(role, items);
					}
				}
				historizeCollection(longIdEntity, values, HistoricDataType.ADD);
			} catch (Exception e) {
				log.warn(FAILED_TO_SERIALIZE_DATA_ERROR, e);
			}
		} else {
			log.info("onPostUpdateCollection:" + event);
		}
	}

	@Override
	public void onPostRemoveCollection(PostCollectionRemoveEvent event) {
		onRemoveCollection(event);
	}

	@Override
	public void onPreRemoveCollection(PreCollectionRemoveEvent event) {
		onRemoveCollection(event);
	}

	@SuppressWarnings("unchecked")
	private void onRemoveCollection(AbstractCollectionEvent event) {
		if (event.getAffectedOwnerOrNull() instanceof AbstractLongIdEntity longIdEntity) {
			String role = computeRole(event.getCollection());
			try {
				Map<String, Object> values = new HashMap<>();
				Serializable snapshot = event.getCollection().getStoredSnapshot();
				if (snapshot instanceof Map && event.getCollection() instanceof PersistentSet<?> collection) {
					List<AbstractLongIdEntity> items = collectRemovedItems(collection, (Map<Object, Object>) snapshot);
					if (CollectionUtils.isNotEmpty(items)) {
						values.put(role, items);
					}
				}

				historizeCollection(longIdEntity, values, HistoricDataType.REMOVE);
			} catch (Exception e) {
				log.warn(FAILED_TO_SERIALIZE_DATA_ERROR, e);
			}
		} else {
			log.info("onPostRemoveCollection:" + event);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onPostRecreateCollection(PostCollectionRecreateEvent event) {
		if (event.getAffectedOwnerOrNull() instanceof AbstractLongIdEntity longIdEntity) {
			String role = computeRole(event.getCollection());
			try {
				Map<String, Object> values = new HashMap<>();
				Serializable snapshot = event.getCollection().getStoredSnapshot();
				if (snapshot instanceof Map && event.getCollection() instanceof PersistentSet<?> collection) {
					List<AbstractLongIdEntity> removedItems = collectRemovedItems(collection,
							(Map<Object, Object>) snapshot);
					if (CollectionUtils.isNotEmpty(removedItems)) {
						values.put(role, removedItems);
					}
					historizeCollection(longIdEntity, values, HistoricDataType.REMOVE);

					List<AbstractLongIdEntity> items = collectAddedItems(collection, new HashMap<>());
					items.removeAll(removedItems);
					if (CollectionUtils.isNotEmpty(items)) {
						values.put(role, items);
					}
					historizeCollection(longIdEntity, values, HistoricDataType.ADD);
				}
			} catch (Exception e) {
				log.warn(FAILED_TO_SERIALIZE_DATA_ERROR, e);
			}
		} else {
			log.info("onPostRecreateCollection:" + event);
		}
	}

	@Override
	public void onPersist(PersistEvent event) throws HibernateException {
		log.debug("onPersist:" + event.getObject());
	}

	@Override
	public boolean requiresPostCommitHandling(EntityPersister persister) {
		return false;
	}

	@Override
	public void onPersist(PersistEvent event, PersistContext createdAlready) throws HibernateException {
		log.debug("onPersist map:" + event);
	}

	@Override
	public void onSaveOrUpdate(SaveOrUpdateEvent event) throws HibernateException {
		log.debug("onSaveOrUpdate:" + event);

	}

	@Override
	public void onDelete(DeleteEvent event) throws HibernateException {
		log.debug("onDelete:" + event);
	}

	@Override
	public void onDelete(DeleteEvent event, DeleteContext transientEntities) throws HibernateException {
		log.debug("onDelete transientEntities :" + event);
	}

	protected boolean exists(Map<Object, Object> snapshot, Object item) {
		boolean result = false;
		for (Object key : snapshot.keySet()) {
			if (key instanceof AbstractLongIdEntity) {
				result = item.equals(key);
				if (result) {
					break;
				}
			}
		}
		return result;
	}

	protected boolean exists(PersistentSet<?> collection, Object item) {
		boolean result = false;
		for (Object key : collection) {
			if (key instanceof AbstractLongIdEntity) {
				result = item.equals(key);
				if (result) {
					break;
				}
			}
		}
		return result;
	}

	protected void historizeCollection(AbstractLongIdEntity entity, Map<String, Object> values, HistoricDataType type)
			throws JsonProcessingException {
		if (MapUtils.isNotEmpty(values)) {
			ObjectWriter objectWriter = historicDataHelper.getObjectMapper().writer();
			String data = objectWriter.writeValueAsString(values);
			historize(entity.getUuid(), entity.getClass(), data, type);
		}
	}

	protected List<AbstractLongIdEntity> collectAddedItems(PersistentSet<?> collection, Map<Object, Object> snapshot) {
		List<AbstractLongIdEntity> items = new ArrayList<>();
		Iterator<?> it = collection.iterator();
		while (it.hasNext()) {
			Object item = it.next();
			if (item instanceof AbstractLongIdEntity longIdEntity && !exists(snapshot, item)) {
				items.add(longIdEntity);
			}
		}
		return items;
	}

	protected List<AbstractLongIdEntity> collectRemovedItems(PersistentSet<?> collection,
			Map<Object, Object> snapshot) {
		List<AbstractLongIdEntity> items = new ArrayList<>();
		for (Map.Entry<Object, Object> entry : snapshot.entrySet()) {
			Object item = entry.getKey();
			if (item instanceof AbstractLongIdEntity longIdEntity && !exists(collection, item)) {
				items.add(longIdEntity);
			}
		}
		return items;
	}

	protected String computeRole(PersistentCollection<?> collection) {
		if (collection.getRole() != null) {
			return computeHasRole(collection);
		} else {
			return computeNoRole(collection);
		}
	}

	private String computeNoRole(PersistentCollection<?> collection) {
		String result = "data";
		Object owner = collection.getOwner();
		Method[] methods = owner.getClass().getMethods();
		for (Method method : methods) {
			if (method.getReturnType().equals(Set.class)) {
				try {
					Object value = method.invoke(owner);
					if (value == collection) {
						result = method.getName().substring(3);
						break;
					}
				} catch (Exception e) {
					// Nothing
				}
			}
		}
		return result;
	}

	private String computeHasRole(PersistentCollection<?> collection) {
		String role = collection.getRole();
		int index = role.lastIndexOf('.');
		if (index >= 0) {
			return role.substring(index + 1, role.length());
		} else {
			return role;
		}
	}

	protected AttributeMapping lookupAttributeDefinition(EntityPersister entityPersister, int index) {
		AttributeMapping result = null;
		if (index >= 0) {
			Iterator<AttributeMapping> attributes = entityPersister.getAttributeMappings().iterator();
			int localIndex = 0;
			while (attributes.hasNext()) {
				AttributeMapping attributeDefinition = attributes.next();
				if (localIndex == index) {
					result = attributeDefinition;
					break;
				}
				localIndex++;
			}
		}
		return result;
	}

	protected HistoricDataRepository getHistoricDataRepository() {
		if (historicDataRepository == null) {
			historicDataRepository = ApplicationContext.getBean(HistoricDataRepository.class);
		}
		return historicDataRepository;
	}

	protected void historize(UUID owner, Class<?> ownerClass, String data, HistoricDataType type) {
		HistoricDataEntity historicDataEntity = historicDataHelper.createHistoricData(owner, ownerClass, data, type);
		getHistoricDataRepository().save(historicDataEntity);
	}

	protected void historize(final AbstractLongIdEntity entity, HistoricDataType type) {
		if (entity instanceof HistoricDataEntity) {
			return;
		}
		historize(entity.getUuid(), entity.getClass(), historicDataHelper.serialize(entity), type);
	}

}
