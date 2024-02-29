/**
 *
 */
package org.georchestra.photosobliques.storage.dialect;

import org.hibernate.dialect.H2Dialect;

/**
 * @author FNI18300
 *
 */
public class H2GeoDialect extends H2Dialect {

	public H2GeoDialect() {
		super();
		// registerFunction("string_agg", new
		// SQLFunctionTemplate(StandardBasicTypes.STRING, "string_agg(?1, ?2)"));
	}

}
