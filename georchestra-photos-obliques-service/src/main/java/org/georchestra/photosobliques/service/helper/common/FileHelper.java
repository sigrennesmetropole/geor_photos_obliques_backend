/**
 *
 */
package org.georchestra.photosobliques.service.helper.common;

import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.georchestra.photosobliques.core.common.DocumentContent;
import org.georchestra.photosobliques.core.common.DocumentFormat;
import org.georchestra.photosobliques.service.exception.UnzipException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @author FNI18300
 *
 */
@Component
public class FileHelper {

	public static final String DOUBLE_POINT = "..";

	public static final String TEMP_FILE_EXTENSION = ".gen";

	public static final String TEMP_FILE_PREFIX = "tmp";

	@Value("${photos_obliques.temporary.directory:}")
	@Getter
	private String temporaryDirectory = null;

	public File createTemporaryFile(String prefix, String extension) throws IOException {
		File outputFile = null;
		if (StringUtils.isNotEmpty(temporaryDirectory)) {
			outputFile = File.createTempFile(prefix, extension, new File(temporaryDirectory));
		} else {
			outputFile = File.createTempFile(prefix, extension);
		}
		return outputFile;
	}

	public File createTemporaryFile(String extension) throws IOException {
		return createTemporaryFile(TEMP_FILE_PREFIX, extension);
	}

	public File createTemporaryFile() throws IOException {
		return createTemporaryFile(TEMP_FILE_PREFIX, TEMP_FILE_EXTENSION);
	}

	/**
	 * Parse l'ensemble des entrées du ZIP pour extraire
	 *
	 * @param zipFile le nom du ZIP
	 * @return un tableau contenant l'ensemble des File extraits
	 */
	public List<File> unzipFile(File zipFile) throws UnzipException {

		// On vérifie que le ZIP sera extrait là où on le pense
		try {
			if (zipFile.getCanonicalPath().contains(DOUBLE_POINT)) {
				throw new UnzipException(
						"Erreur, le zip fourni ne sera pas extrait là où il est sensé arriver, ceci est uen attaque");
			}
		} catch (IOException e) {
			throw new UnzipException("Erreur, lors du contrôle de la destination d'extraction", e);
		}

		List<File> files = new ArrayList<>();

		// Ouverture des flux
		try (FileInputStream fInput = new FileInputStream(zipFile);
				ZipInputStream zipInput = new ZipInputStream(fInput)) {

			// Parcours de toutes les entrées dans le ZIP
			ZipEntry entry = zipInput.getNextEntry();
			while (entry != null) {

				// Parse de l'entrée
				String entryName = entry.getName();
				File file = new File(temporaryDirectory + File.separator + entryName);

				// Si répertoire
				if (entry.isDirectory()) {

					// On créée le sous répertoire s'il n'existe pas pour extraire les fichiers
					// dedans
					File newDir = new File(file.getAbsolutePath());
					if (!newDir.exists()) {
						boolean success = newDir.mkdirs();

						// Si on y arrive pas, erreur
						if (!success) {
							throw new UnzipException(
									"Impossible de créer le sous répertoire d'une entrée du zip à extraire");
						}
					}
				}
				// Si fichier on l'extrait
				else {
					extractFileFromZip(file, zipInput);
					files.add(file);
				}

				// Passage à l'entrée suivante
				zipInput.closeEntry();
				entry = zipInput.getNextEntry();
			}

			zipInput.closeEntry();
			return files;

		} catch (IOException e) {
			throw new UnzipException("Erreur lors du dézippage, une exception s'est produite", e);
		}
	}

	/**
	 * Extrait le contenu du fichier fourni vers une destination
	 *
	 * @param file     fichier de destination
	 * @param zipInput le zip extrait
	 * @throws UnzipException si echec
	 */
	private void extractFileFromZip(File file, ZipInputStream zipInput) throws UnzipException {

		// Ouverture des flux
		byte[] buffer = new byte[2048];

		try (FileOutputStream fOutput = new FileOutputStream(file)) {
			int count;
			while ((count = zipInput.read(buffer)) > 0) {
				fOutput.write(buffer, 0, count);
			}
		} catch (Exception e) {
			throw new UnzipException("Erreur lors de l'extraction d'un élément du ShapeFile", e);
		}
	}

	/**
	 * Compression d'un ensemble de fichiers
	 *
	 * @param fileName
	 * @param documentContents
	 * @return
	 * @throws IOException
	 */
	public DocumentContent zipFiles(String fileName, List<DocumentContent> documentContents) throws IOException {
		File zipFile = createTemporaryFile(fileName, DocumentFormat.ZIP.getExtension());
		DocumentContent result = new DocumentContent(fileName, DocumentFormat.ZIP.getTypeMime(), zipFile);
		try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile));) {
			byte[] buffer = new byte[1024];
			if (CollectionUtils.isNotEmpty(documentContents)) {
				for (DocumentContent documentContent : documentContents) {
					ZipEntry zipEntry = new ZipEntry(documentContent.getFileName());
					zos.putNextEntry(zipEntry);
					try {
						int len;
						while ((len = documentContent.getFileStream().read(buffer)) > 0) {
							zos.write(buffer, 0, len);
						}
					} finally {
						documentContent.closeStream();
						zos.closeEntry();
					}
				}
			}
		}
		return result;
	}
}
