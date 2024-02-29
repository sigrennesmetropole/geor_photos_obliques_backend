/**
 *
 */
package org.georchestra.photosobliques.storage.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author FNI18300
 *
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RepositoryConstants {
	public static final String FIELD_UUID = "uuid";

	public static final String PG_CONCAT = "concat";

	public static final String PG_STRING_AGG = "string_agg";

	public static final String LIST_SEPARATOR = ",";

	public static final String QUERY_ITEM_SEPARATOR = "|";
	public static final String QUERY_BLANK_SEPARATOR = " ";

	public static final String FIELD_NAME = "name";

	public static final String FIELD_USER_TASK_ID = "userTaskId";
	public static final String FIELD_REVISION = "revision";
	public static final String FIELD_PROCESS_DEFINITION_ID = "processDefinitionId";

	public static final String FIELD_LOGIN = "login";

	public static final String FIELD_FIRSTNAME = "firstname";
	public static final String FIELD_LASTNAME = "lastname";

	public static final String FIELD_ID = "id";
	public static final String FIELD_TYPE = "type";
	public static final String FIELD_RESTRICTED = "restricted";
	public static final String FIELD_CODE = "code";
	public static final String FIELD_LABEL = "label";
	public static final String FIELD_ARCHIVE = "archive";
	public static final String FIELD_OPENING_DATE = "openingDate";
	public static final String FIELD_CLOSING_DATE = "closingDate";

	public static final String FIELD_ORGANISME_PROPRIETAIRES = "organismeProprietaires";

}
