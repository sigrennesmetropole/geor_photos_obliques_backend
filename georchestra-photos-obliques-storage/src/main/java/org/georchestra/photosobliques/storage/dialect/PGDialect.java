/**
 *
 */
package org.georchestra.photosobliques.storage.dialect;

import org.hibernate.dialect.DatabaseVersion;
import org.hibernate.dialect.PostgreSQLDialect;

/**
 * @author FNI18300
 *
 */
public class PGDialect extends PostgreSQLDialect {

	public PGDialect() {
		super(DatabaseVersion.make(11, 0));
		// registerFunction("string_agg", new
		// SQLFunctionTemplate(StandardBasicTypes.STRING, "string_agg(?1, ?2)"));
	}

}
