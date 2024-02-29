/**
 * 
 */
package org.georchestra.photosobliques.storage.aop;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.openapitools.jackson.nullable.JsonNullableBeanSerializerModifier;
import org.georchestra.photosobliques.storage.entity.AbstractLongIdEntity;

import java.util.List;

/**
 * Class {@link BeanSerializerModifier } pour gérer l'historique et ne pas
 * écrire les many-to-many et les one-to-many de manière récursive et ne pas
 * écrire les many-to-one juste avec l'uid et courcircuiter certaines propriétés
 * (extent notamment)
 * 
 * @author FNI18300
 *
 */
public class HistoricSerializerModifier extends JsonNullableBeanSerializerModifier {

	@Override
	public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc,
			List<BeanPropertyWriter> beanProperties) {
		super.changeProperties(config, beanDesc, beanProperties);
		for (int i = 0; i < beanProperties.size(); ++i) {
			final BeanPropertyWriter writer = beanProperties.get(i);
			JavaType type = writer.getType();
			PropertyName fullName = writer.getFullName();
			if (type.isTypeOrSubTypeOf(AbstractLongIdEntity.class)) {
				beanProperties.set(i, new HistoricLongIdBeanWriter(writer));
			} else if (type.isTypeOrSubTypeOf(org.locationtech.jts.geom.Geometry.class)
					|| fullName.getSimpleName().equals("extent") || fullName.getSimpleName().equals("geometryEntity")) {
				beanProperties.set(i, new HistoricNullBeanWriter(writer));
			}
		}
		return beanProperties;
	}

	@Override
	public JsonSerializer<?> modifyCollectionSerializer(SerializationConfig config, CollectionType valueType,
			BeanDescription beanDesc, JsonSerializer<?> serializer) {
		return new HistoricCollectionSerializer(beanDesc.getType(), false, null, null);
	}

	@Override
	public JsonSerializer<?> modifyCollectionLikeSerializer(SerializationConfig config, CollectionLikeType valueType,
			BeanDescription beanDesc, JsonSerializer<?> serializer) {
		return new HistoricCollectionSerializer(beanDesc.getType(), false, null, null);
	}

}
