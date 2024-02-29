/**
 *
 */
package org.georchestra.photosobliques.service.helper.common;

import org.apache.commons.collections4.CollectionUtils;
import org.georchestra.photosobliques.storage.entity.AbstractLongIdEntity;
import org.georchestra.photosobliques.storage.repository.StampedRepository;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;

/**
 * @author FNI18300
 *
 */
public class CollectionMerger {

	public <E extends AbstractLongIdEntity, D, R extends StampedRepository<E>> void mergeCollection(
			Collection<E> entityItems, Collection<D> dtoItems, Class<D> dtoClass, R repository)
			throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		Method m = dtoClass.getMethod("getUuid");
		if (CollectionUtils.isNotEmpty(dtoItems)) {
			mergeAdded(entityItems, dtoItems, repository, m);
		}
		if (CollectionUtils.isNotEmpty(entityItems)) {
			mergeRemoved(entityItems, dtoItems, m);
		}
	}

	private <E extends AbstractLongIdEntity, D> void mergeRemoved(Collection<E> entityItems, Collection<D> dtoItems,
			Method m) throws IllegalAccessException, InvocationTargetException {
		Iterator<E> it = entityItems.iterator();
		while (it.hasNext()) {
			E entityItem = it.next();
			D dtoItem = lookupItemByUuid(dtoItems, m, entityItem.getUuid());
			if (dtoItem == null) {
				it.remove();
			}
		}
	}

	private <E extends AbstractLongIdEntity, R extends StampedRepository<E>, D> void mergeAdded(
			Collection<E> entityItems, Collection<D> dtoItems, R repository, Method m)
			throws IllegalAccessException, InvocationTargetException {
		for (D dtoItem : dtoItems) {
			Object uuid = m.invoke(dtoItem);
			E entityItem = repository.findByUuid((UUID) uuid);
			if (!entityItems.contains(entityItem)) {
				entityItems.add(entityItem);
			}
		}
	}

	private <D> D lookupItemByUuid(Collection<D> dtoItems, Method m, UUID uuid)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		D result = null;
		if (CollectionUtils.isNotEmpty(dtoItems)) {
			for (D dtoItem : dtoItems) {
				Object dtoUuid = m.invoke(dtoItem);
				if (uuid.equals(dtoUuid)) {
					result = dtoItem;
					break;
				}
			}
		}
		return result;
	}
}
