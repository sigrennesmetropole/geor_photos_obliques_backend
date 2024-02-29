/**
 * 
 */
package org.georchestra.photosobliques.storage.aop;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
import com.fasterxml.jackson.databind.util.NameTransformer;
import org.openapitools.jackson.nullable.UnwrappingJsonNullableBeanPropertyWriter;
import org.georchestra.photosobliques.core.common.LongId;
import org.georchestra.photosobliques.storage.entity.AbstractLongIdEntity;

import java.util.UUID;

/**
 * Classe {@link BeanPropertyWriter} pour écrire juste l'uid des many-to-one
 * 
 * @author FNI18300
 *
 */
public class HistoricLongIdBeanWriter extends BeanPropertyWriter {

	private static final long serialVersionUID = -2893499383803818837L;

	protected HistoricLongIdBeanWriter(BeanPropertyWriter base) {
		super(base);
	}

	protected HistoricLongIdBeanWriter(BeanPropertyWriter base, PropertyName newName) {
		super(base, newName);
	}

	@Override
	protected BeanPropertyWriter _new(PropertyName newName) {
		return new HistoricLongIdBeanWriter(this, newName);
	}

	@Override
	public BeanPropertyWriter unwrappingWriter(NameTransformer unwrapper) {
		return new UnwrappingJsonNullableBeanPropertyWriter(this, unwrapper);
	}

	@Override
	public void serializeAsField(Object bean, JsonGenerator jgen, SerializerProvider prov) throws Exception {
		Object value = get(bean);
		if (!(value instanceof AbstractLongIdEntity)) {
			return;
		}
		jgen.writeFieldName(_name);
		UUID exactValue = ((LongId) value).getUuid();
		// then find serializer to use
		JsonSerializer<Object> ser = _serializer;
		if (ser == null) {
			Class<?> cls = exactValue.getClass();
			PropertySerializerMap m = _dynamicSerializers;
			ser = m.serializerFor(cls);
			if (ser == null) {
				ser = _findAndAddDynamic(m, cls, prov);
			}
		}
		ser.serialize(exactValue, jgen, prov);
	}
}
