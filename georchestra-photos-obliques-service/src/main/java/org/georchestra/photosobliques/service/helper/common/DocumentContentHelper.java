package org.georchestra.photosobliques.service.helper.common;

import lombok.AllArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.tika.Tika;
import org.georchestra.photosobliques.core.common.DocumentContent;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;

@Component
@AllArgsConstructor
public class DocumentContentHelper {

	public static final String DEFAULT_MIME_TYPE = "application/octet-stream";

	public static final String TEMP_FILE_EXTENSION = ".file";

	public static final String TEMP_FILE_PREFIX = "upload";

	private final FileHelper fileHelper;

	/**
	 * Convertie une {@link Resource} en un objet {@link DocumentContent} (avec le
	 * nom et le type MIME).
	 *
	 * @param body   La ressource à convertire
	 * @param asFile Indique si la ressource est un fichier
	 */
	public DocumentContent convertResource(Resource body, boolean asFile) throws IOException {
		DocumentContent documentContent = null;
		String fileName = body.getFilename();
		String mimeType = DEFAULT_MIME_TYPE;
		if (fileName != null) {
			mimeType = URLConnection.guessContentTypeFromName(fileName);
		} else {
			fileName = "unknown";
		}
		// on force la asFile pour pouvoir utiliser tika sur un fichier sans lire le
		// stream
		if (mimeType == null || body.getFilename() == null) {
			asFile = true;
		}
		documentContent = createDocumentContent(body, fileName, mimeType, asFile);
		// si le type mime est vide ou que le filename aussi on essaye une autre méthode
		// d'extraction de type mime
		if (mimeType == null || body.getFilename() == null) {
			Tika tika = new Tika();
			mimeType = tika.detect(documentContent.getFile());
			if (mimeType == null) {
				mimeType = DEFAULT_MIME_TYPE;
			}
			documentContent.setContentType(mimeType);
		}

		return documentContent;
	}

	/**
	 * Convertie un {@link MultipartFile} en un objet {@link DocumentContent} (avec le
	 * nom et le type MIME).
	 *
	 * @param multipartFile Le multipart à convertire
	 * @param asFile        Indique si la ressource est un fichier
	 */
	public DocumentContent convertResource(MultipartFile multipartFile, boolean asFile) throws IOException {
		DocumentContent documentContent = null;
		String fileName = multipartFile.getOriginalFilename();
		String mimeType = multipartFile.getContentType();
		Resource body = multipartFile.getResource();
		documentContent = createDocumentContent(body, fileName, mimeType, asFile);

		return documentContent;
	}

	private DocumentContent createDocumentContent(Resource body, String fileName, String mimeType, boolean asFile)
			throws IOException {
		DocumentContent documentContent;
		if (body.isFile()) {
			documentContent = new DocumentContent(fileName, mimeType, body.getFile());
		} else if (!asFile) {
			documentContent = new DocumentContent(fileName, mimeType, body.contentLength(), body.getInputStream());
		} else {
			File tmpFile = fileHelper.createTemporaryFile(TEMP_FILE_PREFIX, TEMP_FILE_EXTENSION);
			FileUtils.copyInputStreamToFile(body.getInputStream(), tmpFile);
			documentContent = new DocumentContent(fileName, mimeType, tmpFile);
		}
		return documentContent;
	}
}
