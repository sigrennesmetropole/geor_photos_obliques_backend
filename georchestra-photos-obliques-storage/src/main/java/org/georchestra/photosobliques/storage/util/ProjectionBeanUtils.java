package org.georchestra.photosobliques.storage.util;

import org.apache.commons.lang3.StringUtils;
import org.georchestra.photosobliques.core.bean.LibelleCodeUuidBean;
import org.georchestra.photosobliques.core.bean.LibelleUuidBean;
import org.georchestra.photosobliques.storage.common.RepositoryConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ProjectionBeanUtils {

	public static List<String> unWrapData(String datas) {
		return Arrays.asList(datas.split("\\" + RepositoryConstants.QUERY_ITEM_SEPARATOR));
	}

	public static List<String> unWrapDataList(String datas) {
		return Arrays.asList(datas.split(RepositoryConstants.LIST_SEPARATOR));
	}

	/**
	 * Transforme une chaine de caractère concaténée en objet exploitable par le
	 * front
	 *
	 * @param datas Les données à transformer
	 * @return Une liste d'objet Libelle-Uuid
	 */
	public static List<LibelleUuidBean> unWrapLibelleUuidQuerySelect(List<LibelleUuidBean> result, String datas) {
		if (StringUtils.isNotEmpty(datas)) {
			List<String> dataItems = unWrapDataList(datas);
			dataItems.forEach(c -> {
				LibelleUuidBean bean = new LibelleUuidBean();
				bean.setUuid(UUID.fromString(c.split("\\" + RepositoryConstants.QUERY_ITEM_SEPARATOR)[0]));
				bean.setLibelle(c.split("\\" + RepositoryConstants.QUERY_ITEM_SEPARATOR)[1]);
				result.add(bean);
			});
		}
		return result;
	}

	public static List<LibelleUuidBean> unWrapLibelleUuidQuerySelect(String datas) {
		List<LibelleUuidBean> toRet = new ArrayList<>();
		return unWrapLibelleUuidQuerySelect(toRet, datas);
	}

	/**
	 * Transforme une chaine de caractère concaténée en objet exploitable par le
	 * front
	 *
	 * @param datas Les données à transformer
	 * @return Une liste d'objet Libelle-Code-Uuid
	 */
	public static List<LibelleCodeUuidBean> unWrapLibelleCodeUuidQuerySelect(String datas) {
		if (StringUtils.isNotEmpty(datas)) {
			List<LibelleCodeUuidBean> toRet = new ArrayList<>();
			List<String> dataItems = unWrapDataList(datas);
			dataItems.forEach(m -> {
				LibelleCodeUuidBean bean = new LibelleCodeUuidBean();
				bean.setUuid(UUID.fromString(m.split("\\|")[0]));
				bean.code(m.split("\\|")[1]);
				bean.libelle(m.split("\\|")[2]);
				toRet.add(bean);
			});
			return toRet;
		}
		return new ArrayList<>();
	}

	public static Integer convertToInteger(String value) {
		if (StringUtils.isNotEmpty(value) && !"null".equalsIgnoreCase(value)) {
			try {
				return Integer.valueOf(value);
			} catch (Exception e) {
				//
			}
		}
		return null;
	}

	public static Double convertToDouble(String value) {
		if (StringUtils.isNotEmpty(value) && !"null".equalsIgnoreCase(value)) {
			try {
				return Double.valueOf(value);
			} catch (Exception e) {
				//
			}
		}
		return null;
	}

	private ProjectionBeanUtils() {
		throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
	}
}
