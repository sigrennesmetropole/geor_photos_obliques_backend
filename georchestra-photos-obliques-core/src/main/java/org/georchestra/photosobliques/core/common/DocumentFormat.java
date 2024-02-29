/**
 *
 */
package org.georchestra.photosobliques.core.common;

import lombok.Getter;

/**
 * @author FNI18300
 *
 */
public enum DocumentFormat {

	/** Mime-type PDF */
	PDF("application/pdf", "pdf"),
	/** Mime-type Excel */
	EXCEL("application/vnd.ms-excel", "xls"),
	/** Mime-type HTML */
	HTML("text/html", "html"),
	/** XML */
	XML("application/xml", "xml"),
	/** Mime-type Text */
	TEXT("text/plain", "txt"),
	/** Mime-type CSV */
	CSV("text/csv", "csv"),
	/** Mime-type ZIP */
	ZIP("application/zip", "zip"),
	/** Mime-type json */
	JSON("application/json", "json"),
	/** Mime type JPEG */
	JPEG("image/jpg", "jpg"),
	/** Mime type PNG */
	PNG("image/png", "png"),
	/** Mime type GIF */
	GIF("image/gif", "gif"),
	/** Docx */
	DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document", "docx"),
	DOC("application/msword", "doc"), DOT("application/msword", "dot"),
	DOTX("application/vnd.openxmlformats-officedocument.wordprocessingml.template", "dotx");

	@Getter
	private String typeMime;

	@Getter
	private String extension;

	/**
	 * Constructeur pour FormatDocumentEnum
	 *
	 * @param format
	 */
	private DocumentFormat(String format, String extension) {
		this.typeMime = format;
		this.extension = extension;
	}

	/**
	 * @param prefix
	 * @return le nom d'un fichier avec son extension
	 */
	public String generateFileName(String prefix) {
		if (prefix == null) {
			throw new IllegalArgumentException("le nom du fichier ne peut être null");
		}
		return new StringBuffer(prefix).append('.').append(getExtension()).toString();
	}

	/**
	 * @param typeMime
	 * @return le format pour le type mime
	 */
	public static DocumentFormat lookupFromMimeType(String typeMime) {
		DocumentFormat result = null;
		for (DocumentFormat formatDocumentEnum : values()) {
			if (formatDocumentEnum.getTypeMime().equalsIgnoreCase(typeMime)) {
				result = formatDocumentEnum;
			}
		}
		return result;
	}

	/**
	 * @param extension
	 * @return le format pour l'extension
	 */
	public static DocumentFormat lookupFromExtension(String extension) {
		DocumentFormat result = null;
		for (DocumentFormat formatDocumentEnum : values()) {
			if (formatDocumentEnum.getExtension().equalsIgnoreCase(extension)) {
				result = formatDocumentEnum;
			}
		}
		return result;
	}
}
