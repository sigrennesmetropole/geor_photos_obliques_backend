package org.georchestra.photosobliques.service.helper.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.Tika;
import org.georchestra.photosobliques.core.common.DocumentContent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.net.URLConnection;

@Component
@RequiredArgsConstructor
@Slf4j
public class ResourceHelper {


	public static final String DEFAULT_MIME_TYPE = "application/octet-stream";

	private final ResourceLoader resourceLoader;

	@Value("${temporary.directory:${java.io.tmpdir}}")
	private String temporaryDirectory;

	@Value("${spring.config.additional-location:}")
	private String additionalLocation;

	@Nonnull
	public File copyResourceToTempFile(Resource resource) throws IOException {
		val tempFile = File.createTempFile("search", FilenameUtils.getExtension(resource.getFilename()),
				new File(temporaryDirectory));
		FileUtils.copyInputStreamToFile(resource.getInputStream(), tempFile);
		return tempFile;
	}

	@Nonnull
	public DocumentContent convertToDocumentContent(Resource resource) throws IOException {
		File resourceFile = copyResourceToTempFile(resource);
		String fileName = resource.getFilename();
		String mimeType = DEFAULT_MIME_TYPE;
		if (fileName != null) {
			mimeType = URLConnection.guessContentTypeFromName(fileName);
		} else {
			fileName = "unknown";
		}
		// si le type mime est vide ou que le filename aussi on essaye une autre méthode
		// d'extraction de type mime
		if (mimeType == null || resource.getFilename() == null) {
			Tika tika = new Tika();
			mimeType = tika.detect(resourceFile);
			if (mimeType == null) {
				mimeType = DEFAULT_MIME_TYPE;
			}
		}
		return new DocumentContent(fileName, mimeType, resourceFile);
	}

	@Nonnull
	public Resource getResourceFromAdditionalLocationOrFromClasspath(String filename) {
		val classpathLocation = "classpath:" + filename;

		// Fichier dans l'arborescence de fichiers, si existant
		if (StringUtils.isNotEmpty(additionalLocation)) {
			val externalLocation = additionalLocation + filename;
			val externalResource = resourceLoader.getResource(externalLocation);
			if (externalResource.exists()) {
				return externalResource;
			} else {
				log.info(String.format("External resource %s not found. Using classpath resource instead : %s",
						externalLocation, classpathLocation));
			}
		}

		// Fichier dans le classpath sinon
		return resourceLoader.getResource(classpathLocation);
	}
}
