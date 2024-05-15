/**
 *
 */
package org.georchestra.photosobliques.service.helper.common;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.georchestra.photosobliques.core.common.DocumentContent;
import org.georchestra.photosobliques.core.common.DocumentFormat;
import org.georchestra.photosobliques.service.exception.UnzipException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * @author FNI18300
 *
 */
@Component
@Slf4j
public class FileHelper {

	public static final String DOUBLE_POINT = "..";

	public static final String TEMP_FILE_EXTENSION = ".gen";

	public static final String TEMP_FILE_PREFIX = "tmp";

	@Value("${zipfile.thresholder.entries:10000}")
	protected int thresholderEntries = 10000;

	@Value("${zipfile.thresholder.size:1000000000}")
	protected int thresholderSize = 1000000000; // 1 GB

	@Value("${zipfile.thresholder.ratio:10}")
	protected double thresholderRatio = 10;

	@Value("${photos_obliques.temporary.directory:}")
	@Getter
	private String temporaryDirectory = null;

	public File createTemporaryFile(String prefix, String extension) throws IOException {
		File outputFile;
		if (StringUtils.isNotEmpty(temporaryDirectory)) {
			outputFile = File.createTempFile(prefix, extension, new File(temporaryDirectory));
		} else {
			outputFile = File.createTempFile(prefix, extension);
		}
		outputFile.deleteOnExit();
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
	public List<File> unzipFile(File zippedFile) throws UnzipException {

		// On vérifie que le ZIP sera extrait là où on le pense
		checkUnzipPath(zippedFile);

		int totalSizeArchive = 0;
		int totalEntryArchive = 0;
		List<File> files = new ArrayList<>();

		// Ouverture des flux
		try (ZipFile zipFile = new ZipFile(zippedFile)) {

			Enumeration<? extends ZipEntry> entries = zipFile.entries();
			// Parcours de toutes les entrées dans le ZIP
			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();

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
				} else {
					// Si fichier on l'extrait
					totalSizeArchive += extractFileFromZip(file, zipFile, entry);
					files.add(file);
				}

				totalEntryArchive++;

				if (checkUnzipBomb(zippedFile, totalSizeArchive, totalEntryArchive)) {
					break;
				}
			}

		} catch (IOException e) {
			throw new UnzipException("Erreur lors du dézippage, une exception s'est produite", e);
		}
		return files;
	}

	private boolean checkUnzipBomb(File zippedFile, int totalSizeArchive, int totalEntryArchive) {
		boolean result = false;
		if (totalSizeArchive > thresholderSize) {
			// the uncompressed data size is too much for the application resource capacity
			log.warn("the uncompressed data size is too much for the application resource capacity:" + zippedFile);
			result = true;
		}

		if (totalEntryArchive > thresholderEntries) {
			// too much entries in this archive, can lead to inodes exhaustion of the system
			log.warn("too much entries in this archive, can lead to inodes exhaustion of the system:" + zippedFile);
			result = true;
		}
		return result;
	}

	private void checkUnzipPath(File zippedFile) throws UnzipException {
		try {
			if (zippedFile.getCanonicalPath().contains(DOUBLE_POINT)) {
				throw new UnzipException(
						"Erreur, le zip fourni ne sera pas extrait là où il est sensé arriver, ceci est uen attaque");
			}
		} catch (IOException e) {
			throw new UnzipException("Erreur, lors du contrôle de la destination d'extraction", e);
		}
	}

	/**
	 * Extrait le contenu du fichier fourni vers une destination
	 *
	 * @param file     fichier de destination
	 * @param zipInput le zip extrait
	 * @throws UnzipException si echec
	 * @throws IOException
	 */
	private int extractFileFromZip(File file, ZipFile zipFile, ZipEntry zipEntry) throws UnzipException, IOException {
		int nBytes = -1;
		byte[] buffer = new byte[2048];
		int totalSizeEntry = 0;
		int totalSizeArchive = 0;

		// Ouverture des flux
		try (InputStream zipInput = new BufferedInputStream(zipFile.getInputStream(zipEntry));
				FileOutputStream fOutput = new FileOutputStream(file)) {
			while ((nBytes = zipInput.read(buffer)) > 0) { // Compliant
				fOutput.write(buffer, 0, nBytes);
				totalSizeEntry += nBytes;
				totalSizeArchive += nBytes;

				double compressionRatio = (double) totalSizeEntry / zipEntry.getCompressedSize();
				if (compressionRatio > thresholderRatio) {
					log.warn("Looks like zipbomb (ratio)");
					break;
				}
			}
		} catch (Exception e) {
			throw new UnzipException("Erreur lors de l'extraction d'un élément du ShapeFile", e);
		}
		return totalSizeArchive;
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
		long fileSize = documentContents.stream().map(DocumentContent::getFileSize).reduce(0L, Long::sum);
		DocumentContent result = new DocumentContent(fileName, DocumentFormat.ZIP.getTypeMime(), fileSize, zipFile);
		try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile))) {
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
