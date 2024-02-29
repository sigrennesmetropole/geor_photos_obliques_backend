/**
 *
 */
package org.georchestra.photosobliques.storage.aop;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.util.NameTransformer;
import org.openapitools.jackson.nullable.UnwrappingJsonNullableBeanPropertyWriter;

/**
 * Classe {@link BeanPropertyWriter} pour écrire ... rien
 *
 * @author FNI18300
 *
 */
public class HistoricNullBeanWriter extends BeanPropertyWriter {

	private static final long serialVersionUID = -2893499383803818837L;

	protected HistoricNullBeanWriter(BeanPropertyWriter base) {
		super(base);
	}

	protected HistoricNullBeanWriter(BeanPropertyWriter base, PropertyName newName) {
		super(base, newName);
	}

	@Override
	protected BeanPropertyWriter _new(PropertyName newName) {
		return new HistoricNullBeanWriter(this, newName);
	}

	@Override
	public BeanPropertyWriter unwrappingWriter(NameTransformer unwrapper) {
		return new UnwrappingJsonNullableBeanPropertyWriter(this, unwrapper);
	}

	@Override
	public void serializeAsField(Object bean, JsonGenerator jgen, SerializerProvider prov) throws Exception {
		// nothing to do
	}
}
