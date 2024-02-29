package org.georchestra.photosobliques.service.helper.common;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;

/**
 * Classe permettant de manipuler des fichiers de propriétés (*.properties).<br>
 * Il est possible de charger un fichier provenant d'un répertoire ou du
 * classpath ou d'un jar, mais aussi de charger un ensemble de fichiers contenus
 * dans un répertoire.<br>
 *
 * Il existe des méthodes permettant de convertir une propriété dans un type
 * primitif ou simple ou avancé.<br>
 * Il existe des méthodes permettant d'extraire des listes de valeurs ou toutes
 * les propriétés commençant pas un préfixe.<br>
 * Exemple d'utilisation : PropertiesManager propertiesManager = new
 * PropertiesManager(); propertiesManager.initialize("test.properties"); pour
 * charger un fichier test.properties présent dans le classpath (y compris un
 * jar)
 *
 * @author fnisseron
 *
 */
public class PropertiesManager {
	private static final String UTF_8 = "UTF-8";

	/**
	 * Caractère de séparation par défaut des propriétés multivaluée
	 */
	public static final String DEFAULT_VALUE_SEPARATOR = ",";

	/**
	 * Caractére de séparation par défaut des libellés de clés
	 */
	public static final String DEFAULT_KEY_SEPARATOR = ".";

	private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesManager.class);

	/**
	 * Liste des propriétés contenues dans le gestionnaire
	 */
	private Properties properties = new Properties();

	/**
	 * Méthode d'initialisation du manager :<br>
	 * <li>Le chemin passé en paramêtre pointe soit sur un répertoire soit sur un
	 * fichier :
	 * <ul>
	 * Dans le premier cas, le répertoire les sous-répertoires sont parcourus de
	 * maniére récursive pour charger l'ensemble des fichiers de propriétés
	 * </ul>
	 * <ul>
	 * Dans le second cas, le fichier seul est chargé
	 * </ul>
	 * </li>
	 *
	 * @param path        Le chemin vers le répertoire contenant le propriétés
	 * @param charsetName
	 * @throws UnsupportedEncodingException
	 */
	public void initialize(String path, String charsetName) throws UnsupportedEncodingException {
		// remplacement du getClass().getClassLoader() afin de rechercher hors jar
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		File propertiesRoot = new File(path);
		if (!propertiesRoot.exists()) {

			URL url = classLoader.getResource(path);

			if (url == null) {
				throw new IllegalArgumentException(
						"Configuration file path cannot be null. File path was taken from:" + path);
			}
			propertiesRoot = new File(URLDecoder.decode(url.getPath(), UTF_8));
			LOGGER.info("Find properties files from {}", propertiesRoot);
		}

		if (!propertiesRoot.isDirectory()) {
			if (propertiesRoot.exists()) {
				loadPropertiesFile(propertiesRoot, charsetName);
			} else {
				loadPropertiesFile(path, charsetName);
			}
		} else {
			loadPropertiesFiles(propertiesRoot, charsetName, true);
		}
	}

	/**
	 * Méthode d'initialisation du manager
	 * <li>Le chemin passé en paramêtre pointe soit sur un répertoire soit sur un
	 * fichier :
	 * <ul>
	 * Dans le premier cas, le répertoire les sous-répertoires sont parcourus de
	 * maniére récursive pour charger l'ensemble des fichiers de propriétés
	 * </ul>
	 * <ul>
	 * Dans le second cas, le fichier seul est chargé
	 * </ul>
	 * </li>
	 *
	 * @param pPath Le chemin vers le répertoire contenant le propriétés
	 */
	public void initialize(String pPath) throws UnsupportedEncodingException {
		initialize(pPath, null);
	}

	/**
	 * Méthode d'initialisation du manager<br>
	 * Le chemin passé en paramêtre pointe sur un fichier de propriétés dont chaque
	 * entrée est un pointeur vers une liste de fichiers<br>
	 * L'ensemble des fichiers ainsi indiqués est chargé.
	 *
	 * @param path        le chemin du fichier de propriété racine
	 * @param charsetName
	 * @throws UnsupportedEncodingException
	 */
	public void initializeFromPropertyFile(String path, String charsetName) throws UnsupportedEncodingException {
		// remplacement du getClass().getClassLoader() afin de rechercher hors jar
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		URL url = classLoader.getResource(path);
		File propertiesRoot = new File(URLDecoder.decode(url.getPath(), UTF_8));
		LOGGER.info("Load properties files from :{}", propertiesRoot);

		if (!propertiesRoot.exists() && propertiesRoot.isFile()) {
			LOGGER.error("Invalid ressources directory {}", propertiesRoot);
		}

		loadPropertiesFile(propertiesRoot, charsetName);
		Properties lproperties = properties;
		properties = new Properties();
		Enumeration<Object> enumeration = lproperties.keys();
		while (enumeration.hasMoreElements()) {
			Object key = enumeration.nextElement();
			String value = (String) lproperties.get(key);
			URL url2 = classLoader.getResource(value);
			File propertiesFile = new File(URLDecoder.decode(url2.getPath(), UTF_8));
			LOGGER.info("Load properties files from :{}", propertiesFile);
			loadPropertiesFile(propertiesRoot, charsetName);
		}

	}

	/**
	 * Méthode d'initialisation du manager<br>
	 * Le chemin passé en paramêtre pointe sur un fichier de propriétés dont chaque
	 * entrée est un pointeur vers une liste de fichiers<br>
	 * L'ensemble des fichiers ainsi indiqués est chargé.
	 *
	 * @param path le chemin du fichier de propriété racine
	 */
	public void initializeFromPropertyFile(String path) throws UnsupportedEncodingException {
		initializeFromPropertyFile(path, null);
	}

	/**
	 * Retourne la valeur de la propriété passée en param&egrave;tre
	 *
	 * @param pPropertyName La nom de la propriété
	 * @return La valeur de la propriété
	 */
	public String getStringProperty(String pPropertyName) {
		return properties.getProperty(pPropertyName);
	}

	/**
	 * Retourne la valeur de la propriété passée en param&egrave;tre
	 *
	 * @param pPropertyName La nom de la propriété
	 * @param pDefaultValue valeur par défaut
	 * @return La valeur de la propriété
	 */
	public String getStringProperty(String pPropertyName, String pDefaultValue) {
		return properties.getProperty(pPropertyName, pDefaultValue);
	}

	/**
	 * Retourne la valeur de la propriété passée en param&egrave;tre sous forme d'un
	 * entier
	 *
	 * @param pPropertyName La nom de la propriété
	 * @return La valeur de la propriété
	 */
	public int getPrimitiveIntegerProperty(String pPropertyName) {
		return getPrimitiveIntegerProperty(pPropertyName, 0);
	}

	/**
	 * Retourne la valeur de la propriété passée en param&egrave;tre sous forme d'un
	 * entier
	 *
	 * @param pPropertyName La nom de la propriété
	 * @param pDefaultValue valeur par défaut
	 * @return La valeur de la propriété
	 */
	public int getPrimitiveIntegerProperty(String pPropertyName, int pDefaultValue) {
		int value = pDefaultValue;
		String s = properties.getProperty(pPropertyName);
		if (StringUtils.isNotBlank(s)) {
			try {
				value = Integer.parseInt(s);
			} catch (NumberFormatException e) {
				LOGGER.error("Impossible to convert int property: {}", pPropertyName, e);
			}
		}
		return value;
	}

	/**
	 * Retourne la valeur de la propriété passée en param&egrave;tre sous forme d'un
	 * entier
	 *
	 * @param pPropertyName La nom de la propriété
	 * @return La valeur de la propriété
	 */
	public Integer getIntegerProperty(String pPropertyName) {
		return getIntegerProperty(pPropertyName, null);
	}

	/**
	 * Retourne la valeur de la propriété passée en param&egrave;tre sous forme d'un
	 * entier
	 *
	 * @param pPropertyName La nom de la propriété
	 * @param pDefaultValue valeur par défaut
	 * @return La valeur de la propriété
	 */
	public Integer getIntegerProperty(String pPropertyName, Integer pDefaultValue) {
		Integer value = pDefaultValue;
		String s = properties.getProperty(pPropertyName);
		if (StringUtils.isNotBlank(s)) {
			try {
				value = Integer.valueOf(s);
			} catch (NumberFormatException e) {
				LOGGER.error("Impossible to convert Integer property: {}", pPropertyName, e);
			}
		}
		return value;
	}

	/**
	 * Retourne la valeur de la propriété passée en param&egrave;tre sous forme d'un
	 * entier
	 *
	 * @param pPropertyName La nom de la propriété
	 * @return La valeur de la propriété
	 */
	public long getPrimitiveLongProperty(String pPropertyName) {
		return getPrimitiveLongProperty(pPropertyName, 0);
	}

	/**
	 * Retourne la valeur de la propriété passée en param&egrave;tre sous forme d'un
	 * entier
	 *
	 * @param pPropertyName La nom de la propriété
	 * @param pDefaultValue valeur par défaut
	 * @return La valeur de la propriété
	 */
	public long getPrimitiveLongProperty(String pPropertyName, long pDefaultValue) {
		long value = pDefaultValue;
		String s = properties.getProperty(pPropertyName);
		if (StringUtils.isNotBlank(s)) {
			try {
				value = Long.parseLong(s);
			} catch (NumberFormatException e) {
				LOGGER.error("Impossible to convert long property: {}", pPropertyName, e);
			}
		}
		return value;
	}

	/**
	 * Retourne la valeur de la propriété passée en param&egrave;tre sous forme d'un
	 * entier
	 *
	 * @param pPropertyName La nom de la propriété
	 * @return La valeur de la propriété
	 */
	public Long getLongProperty(String pPropertyName) {
		return getLongProperty(pPropertyName, null);
	}

	/**
	 * Retourne la valeur de la propriété passée en param&egrave;tre sous forme d'un
	 * entier
	 *
	 * @param pPropertyName La nom de la propriété
	 * @param pDefaultValue valeur par défaut
	 * @return La valeur de la propriété
	 */
	public Long getLongProperty(String pPropertyName, Long pDefaultValue) {
		Long value = pDefaultValue;
		String s = properties.getProperty(pPropertyName);
		if (StringUtils.isNotBlank(s)) {
			try {
				value = Long.valueOf(s);
			} catch (NumberFormatException e) {
				LOGGER.error("Impossible to convert Long property: {}", pPropertyName, e);
			}
		}
		return value;
	}

	/**
	 * Retourne la valeur de la propriété passée en param&egrave;tre sous forme d'un
	 * réel
	 *
	 * @param pPropertyName La nom de la propriété
	 * @return La valeur de la propriété
	 */
	public float getPrimitiveFloatProperty(String pPropertyName) {
		return getPrimitiveFloatProperty(pPropertyName, 0.0f);
	}

	/**
	 * Retourne la valeur de la propriété passée en param&egrave;tre sous forme d'un
	 * réel
	 *
	 * @param pPropertyName La nom de la propriété
	 * @param pDefaultValue valeur par défaut
	 * @return La valeur de la propriété
	 */
	public float getPrimitiveFloatProperty(String pPropertyName, float pDefaultValue) {
		float value = pDefaultValue;
		String s = properties.getProperty(pPropertyName);
		if (StringUtils.isNotBlank(s)) {
			try {
				value = Float.parseFloat(s);
			} catch (NumberFormatException e) {
				LOGGER.error("Impossible to convert float property: {}", pPropertyName, e);
			}
		}
		return value;
	}

	/**
	 * Retourne la valeur de la propriété passée en param&egrave;tre sous forme d'un
	 * réel
	 *
	 * @param pPropertyName La nom de la propriété
	 * @return La valeur de la propriété
	 */
	public Float getFloatProperty(String pPropertyName) {
		return getFloatProperty(pPropertyName, null);
	}

	/**
	 * Retourne la valeur de la propriété passée en param&egrave;tre sous forme d'un
	 * réel
	 *
	 * @param pPropertyName La nom de la propriété
	 * @param pDefaultValue valeur par défaut
	 * @return La valeur de la propriété
	 */
	public Float getFloatProperty(String pPropertyName, Float pDefaultValue) {
		Float value = pDefaultValue;
		String s = properties.getProperty(pPropertyName);
		if (StringUtils.isNotBlank(s)) {
			try {
				value = Float.valueOf(s);
			} catch (NumberFormatException e) {
				LOGGER.error("Impossible to convert float property: {}", pPropertyName, e);
			}
		}
		return value;
	}

	/**
	 * Retourne la valeur de la propriété passée en param&egrave;tre sous forme d'un
	 * booléen
	 *
	 * @param pPropertyName La nom de la propriété
	 * @return La valeur de la propriété
	 */
	public boolean getPrimitiveBooleanProperty(String pPropertyName) {
		return getPrimitiveBooleanProperty(pPropertyName, false);
	}

	/**
	 * Retourne la valeur de la propriété passée en param&egrave;tre sous forme d'un
	 * booléen
	 *
	 * @param pPropertyName La nom de la propriété
	 * @param pDefaultValue valeur par défaut
	 * @return La valeur de la propriété
	 */
	public boolean getPrimitiveBooleanProperty(String pPropertyName, boolean pDefaultValue) {
		boolean value = pDefaultValue;
		String s = properties.getProperty(pPropertyName);
		if (StringUtils.isNotBlank(s)) {
			value = Boolean.parseBoolean(s);
		}
		return value;
	}

	/**
	 * Retourne la valeur de la propriété passée en param&egrave;tre sous forme d'un
	 * booléen
	 *
	 * @param pPropertyName La nom de la propriété
	 * @return La valeur de la propriété
	 */
	public Boolean getBooleanProperty(String pPropertyName) {
		return getBooleanProperty(pPropertyName, null);
	}

	/**
	 * Retourne la valeur de la propriété passée en param&egrave;tre sous forme d'un
	 * booléen
	 *
	 * @param pPropertyName La nom de la propriété
	 * @param pDefaultValue valeur par défaut
	 * @return La valeur de la propriété
	 */
	public Boolean getBooleanProperty(String pPropertyName, Boolean pDefaultValue) {
		Boolean value = pDefaultValue;
		String s = properties.getProperty(pPropertyName);
		if (StringUtils.isNotBlank(s)) {
			value = Boolean.valueOf(s);
		}
		return value;
	}

	/**
	 * Retourne la valeur de la propriété passée en param&egrave;tre sous forme d'un
	 * byte
	 *
	 * @param pPropertyName La nom de la propriété
	 * @return La valeur de la propriété
	 */
	public byte getPrimitiveByteProperty(String pPropertyName) {
		return getPrimitiveByteProperty(pPropertyName, (byte) 0);
	}

	/**
	 * Retourne la valeur de la propriété passée en param&egrave;tre sous forme d'un
	 * byte
	 *
	 * @param pPropertyName La nom de la propriété
	 * @param pDefaultValue valeur par défaut
	 * @return La valeur de la propriété
	 */
	public byte getPrimitiveByteProperty(String pPropertyName, byte pDefaultValue) {
		byte value = pDefaultValue;
		String s = properties.getProperty(pPropertyName);
		if (StringUtils.isNotBlank(s)) {
			try {
				value = Byte.parseByte(s);
			} catch (NumberFormatException e) {
				LOGGER.error("Impossible to convert byte property: {}", pPropertyName, e);
			}
		}
		return value;
	}

	/**
	 * Retourne la valeur de la propriété passée en param&egrave;tre sous forme d'un
	 * byte
	 *
	 * @param pPropertyName La nom de la propriété
	 * @param pDefaultValue valeur par défaut
	 * @return La valeur de la propriété
	 */
	public Byte getByteProperty(String pPropertyName, Byte pDefaultValue) {
		Byte value = pDefaultValue;
		String s = properties.getProperty(pPropertyName);
		if (StringUtils.isNotBlank(s)) {
			try {
				value = Byte.valueOf(s);
			} catch (NumberFormatException e) {
				LOGGER.error("Impossible to convert byte property: {}", pPropertyName, e);
			}
		}
		return value;
	}

	/**
	 * Retourne la valeur de la propriété passée en param&egrave;tre sous forme d'un
	 * byte
	 *
	 * @param pPropertyName La nom de la propriété
	 * @return La valeur de la propriété
	 */
	public Byte getByteProperty(String pPropertyName) {
		return getByteProperty(pPropertyName, null);
	}

	/**
	 * Retourne une classe correspondant é la valeur de la propriété passée en
	 * paramêtre
	 *
	 * @return La classe
	 */
	public Class<?> getClassProperty(String pKey) {
		Class<?> property = null;
		String value = properties.getProperty(pKey);
		if (value != null) {
			try {
				// remplacement du getClass().getClassLoader() afin de rechercher hors jar
				property = Thread.currentThread().getContextClassLoader().loadClass(value);
			} catch (ClassNotFoundException e) {
				throw new IllegalArgumentException("Invalid value for key :" + pKey + ":" + value, e);
			}
		}
		return property;
	}

	/**
	 * Retourne un objet File correspondant é la valeur de la propriété passée en
	 * paramêtre
	 *
	 * @return Le fichier
	 */
	public File getFileProperty(String pKey) {
		File property = null;
		String value = properties.getProperty(pKey);
		if (value != null) {
			try {
				// remplacement du getClass().getClassLoader() afin de rechercher hors jar
				URL url = Thread.currentThread().getContextClassLoader().getResource(value);
				property = new File(URLDecoder.decode(url.getPath(), UTF_8));
			} catch (UnsupportedEncodingException e) {
				throw new IllegalArgumentException("Invalid value for key :" + pKey + ":" + value, e);
			}
		}

		return property;
	}

	/**
	 * Retourne un object URL correspondant é la valeur de le propriété passée en
	 * paramêtre
	 *
	 * @return l'URL associée é la clé
	 */
	public URL getURLProperty(String pKey) {
		URL property = null;
		String value = properties.getProperty(pKey);
		if (value != null) {
			// remplacement du getClass().getClassLoader() afin de rechercher hors jar
			property = Thread.currentThread().getContextClassLoader().getResource(value);
		}
		return property;
	}

	/**
	 * Retourne une liste de valeurs associées é la clé indiquée en paramêtre. Les
	 * espaces ne sont pas autorisés dans les valeurs
	 *
	 * @param pSeparator séparateur de valeurs
	 * @return La liste de valeurs de la propriété
	 */
	public List<String> getListProperty(String pKey, String pSeparator) {
		String s = properties.getProperty(pKey);

		List<String> propertyList = null;
		if (null != s) {
			if (pSeparator != null) {
				propertyList = new ArrayList<>();
				StringTokenizer tokens = new StringTokenizer(s, pSeparator);
				while (tokens.hasMoreElements()) {
					propertyList.add(tokens.nextToken().trim());
				}
			} else {
				LOGGER.warn("Property separator can not be null");

			}
		}
		return propertyList;
	}

	/**
	 * Retourne une liste de valeurs associées é la clé indiquée en paramêtre. Les
	 * espaces ne sont pas autorisés.
	 *
	 * @return La liste de valeurs de la propriété
	 */
	public List<String> getListProperty(String pKey) {
		return this.getListProperty(pKey, DEFAULT_VALUE_SEPARATOR);
	}

	/**
	 * Retourne la liste des propriétés du manager
	 *
	 * @return liste des propriétés
	 */
	public Properties getProperties() {
		return this.properties;
	}

	/**
	 * Retourne une table de toutes les propriétés dont la clé commence par
	 * pKeyStart et préfixées par pPropertiesPrefix. Le caractéres de séparation
	 * entre le préfixe de la clé (pPropertiesPrefix) et le début de la clé
	 * (pKeyStart) est indiqué en paramêtre également (pKeySeparator). Exemples :
	 * xxx.y.1=a, xxx.y.2=b et xxx.y.3=c sont 3 propriétés,
	 * getPropertiesStartingWith("xxx", "y", ".") renvoie une map contenant [y.1,a],
	 * [y.2,b] et [y.3,c].
	 *
	 * @param pKeyStart     début de la clé
	 * @param pKeySeparator séparateur de hiérarchisation de la clé
	 * @return table
	 */
	public Map<String, Object> getPropertiesStartingWith(String pPropertiesPrefix, String pKeyStart,
			String pKeySeparator) {
		int prefixLength = pPropertiesPrefix.length() + pKeySeparator.length();

		String completeKeyStart = new StringBuffer(pPropertiesPrefix).append(pKeySeparator).append(pKeyStart)
				.toString();

		Enumeration<Object> keys = properties.keys();

		Map<String, Object> entries = new HashMap<>();

		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();

			if (key.startsWith(completeKeyStart)) {

				entries.put(key.substring(prefixLength), properties.get(key));
			}
		}
		return entries;
	}

	/**
	 * @param pKeyStart début de la clé
	 * @return table
	 */
	public Map<String, Object> getPropertiesStartingWith(String pPropertiesPrefix, String pKeyStart) {
		return getPropertiesStartingWith(pPropertiesPrefix, pKeyStart, DEFAULT_KEY_SEPARATOR);
	}

	/**
	 * @param pKeyStart début de la clé
	 * @return table
	 */
	public Map<String, Object> getPropertiesStartingWith(String pKeyStart) {
		return getPropertiesStartingWith("", pKeyStart, "");
	}

	/**
	 * Ajoute des propriétés dans le manager
	 *
	 * @param pProperties les propriétés
	 */
	public void addProperties(Map<Object, Object> pProperties) {
		properties.putAll(pProperties);
	}

	/**
	 * Charge un fichier de propriétés unique. Les données ainsi chargées sont
	 * cumulées avec les propriétés existantes.
	 *
	 * @param pPropertiesFile le fichier
	 */
	protected void loadPropertiesFile(File propertiesFile, String charsetName) {
		try (InputStream is = new FileInputStream(propertiesFile)) {

			if (charsetName == null) {
				properties.load(is);
			} else {
				properties.load(new InputStreamReader(is, charsetName));
			}

		} catch (IOException e) {
			LOGGER.error("Impossible to load the properties file as file or stream: {}", propertiesFile, e);
		}
	}

	/**
	 * Charge un fichier de propriétés unique. Les données ainsi chargées sont
	 * cumulées avec les propriétés existantes.
	 *
	 * @param pPropertiesFile le fichier
	 */
	protected void loadPropertiesFile(String propertiesFile, String charsetName) {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

		try (InputStream is = classLoader.getResourceAsStream(propertiesFile)) {
			if (is != null) {
				if (charsetName == null) {
					properties.load(is);
				} else {
					properties.load(new InputStreamReader(is, charsetName));
				}
			} else {
				LOGGER.error("Invalid ressources file {}", propertiesFile);
			}

		} catch (IOException e) {
			LOGGER.error("Impossible to load the properties file as file or stream: {}", propertiesFile, e);
		}
	}

	/**
	 * Charge les fichiers de propriétés contenus dans un répertoire de maniére
	 * récursive ou non
	 *
	 * @param pPropertiesDirectory le repértoire racine
	 * @param pRecursivly          si le chargement doit être fait en profondeur ou
	 *                             non
	 */
	protected void loadPropertiesFiles(File pPropertiesDirectory, String charsetName, boolean pRecursivly) {
		// liste tous les fichiers ayant l'extension ".properties"
		File[] propertiesFiles = pPropertiesDirectory.listFiles(pFile -> {

			boolean accepted = false;
			if (pFile.getPath().endsWith(".properties") && pFile.getPath().indexOf("hibernate") < 0) {
				accepted = true;
			}
			return accepted;
		});
		// Chacun des fichiers est lu et les proprietes sont ajoutees a celle
		// existantes
		for (int i = 0; propertiesFiles != null && i < propertiesFiles.length; i++) {
			LOGGER.info("Load properties file: {}", propertiesFiles[i]);
			loadPropertiesFile(propertiesFiles[i], charsetName);
		}

		if (pRecursivly) {
			File[] propertiesDirectories = pPropertiesDirectory.listFiles(pFile -> {

				boolean accepted = false;
				if (pFile.isDirectory()) {
					accepted = true;
				}
				return accepted;
			});

			for (int i = 0; propertiesDirectories != null && i < propertiesDirectories.length; i++) {
				loadPropertiesFile(propertiesDirectories[i], charsetName);
			}
		}
	}

}
