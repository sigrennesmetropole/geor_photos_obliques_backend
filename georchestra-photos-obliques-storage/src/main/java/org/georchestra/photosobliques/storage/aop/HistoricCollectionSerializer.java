/**
 * 
 */
package org.georchestra.photosobliques.storage.aop;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.ContainerSerializer;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
import com.fasterxml.jackson.databind.ser.std.AsArraySerializerBase;
import org.hibernate.collection.spi.PersistentSet;
import org.georchestra.photosobliques.storage.entity.AbstractLongIdEntity;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;

/**
 * Classe {@link BeanPropertyWriter} pour écrire juste l'uid des many-to-one
 * 
 * @author FNI18300
 *
 */
public class HistoricCollectionSerializer extends AsArraySerializerBase<Collection<?>> {

	private static final long serialVersionUID = 3795434540526859181L;

	public HistoricCollectionSerializer(Class<?> cls, JavaType et, boolean staticTyping, TypeSerializer vts,
			JsonSerializer<Object> elementSerializer) {
		super(cls, et, staticTyping, vts, null, elementSerializer, null);
	}

	/**
	 * @since 2.6
	 */
	public HistoricCollectionSerializer(JavaType elemType, boolean staticTyping, TypeSerializer vts,
			JsonSerializer<Object> valueSerializer) {
		super(Collection.class, elemType, staticTyping, vts, valueSerializer);
	}

	public HistoricCollectionSerializer(HistoricCollectionSerializer src, BeanProperty property, TypeSerializer vts,
			JsonSerializer<?> valueSerializer, Boolean unwrapSingle) {
		super(src, property, vts, valueSerializer, unwrapSingle);
	}

	@Override
	public ContainerSerializer<?> _withValueTypeSerializer(TypeSerializer vts) {
		return new HistoricCollectionSerializer(this, _property, vts, _elementSerializer, _unwrapSingle);
	}

	@Override
	public HistoricCollectionSerializer withResolved(BeanProperty property, TypeSerializer vts,
			JsonSerializer<?> elementSerializer, Boolean unwrapSingle) {
		return new HistoricCollectionSerializer(this, property, vts, elementSerializer, unwrapSingle);
	}

	protected boolean skipCollection(Collection<?> value) {
		boolean skip = false;
		if (value instanceof PersistentSet<?> persistentSet) {
			boolean dirty = persistentSet.isDirty();
			boolean initialized = persistentSet.wasInitialized();
			if (!dirty && !initialized) {
				skip = true;
			}
		}
		return skip;
	}

	@Override
	public boolean isEmpty(SerializerProvider prov, Collection<?> value) {
		if (skipCollection(value)) {
			return true;
		} else {
			return value.isEmpty();
		}
	}

	@Override
	public boolean hasSingleElement(Collection<?> value) {
		return value.size() == 1;
	}

	@Override
	public final void serialize(Collection<?> value, JsonGenerator g, SerializerProvider provider) throws IOException {
		if (skipCollection(value)) {
			g.writeStartArray(value, 0);
			g.writeEndArray();
			return;
		}

		final int len = value.size();
		if (len == 1 && (((_unwrapSingle == null)
				&& provider.isEnabled(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED))
				|| (_unwrapSingle == Boolean.TRUE))) {
			serializeContents(value, g, provider);
			return;
		}
		g.writeStartArray(value, len);
		serializeContents(value, g, provider);
		g.writeEndArray();
	}

	@Override
	public void serializeContents(Collection<?> value, JsonGenerator g, SerializerProvider provider)
			throws IOException {
		if (skipCollection(value)) {
			return;
		}

		g.setCurrentValue(value);

		Iterator<?> it = value.iterator();
		if (!it.hasNext()) {
			return;
		}
		PropertySerializerMap serializers = _dynamicSerializers;
		final TypeSerializer typeSer = _valueTypeSerializer;

		serializeContents(value, g, provider, it, serializers, typeSer);
	}

	private void serializeContents(Collection<?> value, JsonGenerator g, SerializerProvider provider, Iterator<?> it,
			PropertySerializerMap serializers, final TypeSerializer typeSer) throws IOException {
		int i = 0;
		try {
			do {
				Object elem = it.next();
				if (elem == null) {
					provider.defaultSerializeNull(g);
				} else {
					serializeContent(g, provider, serializers, typeSer, elem);
				}
				++i;
			} while (it.hasNext());
		} catch (Exception e) {
			wrapAndThrow(provider, e, value, i);
		}
	}

	private PropertySerializerMap serializeContent(JsonGenerator g, SerializerProvider provider,
			PropertySerializerMap serializers, final TypeSerializer typeSer, Object elem) throws IOException {
		Class<?> cc;
		Object value;
		if (elem instanceof AbstractLongIdEntity longId) {
			cc = UUID.class;
			value = longId.getUuid();
		} else if (elem instanceof Enum<?> enumValue) {
			cc = String.class;
			value = enumValue.name();
		} else {
			cc = elem.getClass();
			value = elem;
		}

		JsonSerializer<Object> serializer = serializers.serializerFor(cc);
		if (serializer == null) {
			if (_elementType.hasGenericTypes()) {
				try {
					serializer = _findAndAddDynamic(serializers, provider.constructSpecializedType(_elementType, cc),
							provider);
				} catch (Exception e) {
					// nothing to do
				}
			}
			if (serializer == null) {
				serializer = _findAndAddDynamic(serializers, cc, provider);
			}
			serializers = _dynamicSerializers;
		}
		if (typeSer == null) {
			serializer.serialize(value, g, provider);
		} else {
			serializer.serializeWithType(value, g, provider, typeSer);
		}
		return serializers;
	}

}
