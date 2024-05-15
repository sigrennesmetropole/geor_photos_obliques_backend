package org.georchestra.photosobliques.service.sm.photo;

import jakarta.persistence.Tuple;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.georchestra.photosobliques.core.bean.PhotoOblique;
import org.georchestra.photosobliques.core.bean.photo.PhotoObliqueSearchCriteria;
import org.georchestra.photosobliques.core.common.DocumentContent;
import org.georchestra.photosobliques.service.exception.AppServiceBadRequestException;
import org.georchestra.photosobliques.service.exception.AppServiceException;
import org.georchestra.photosobliques.service.exception.AppServiceExceptionsStatus;
import org.georchestra.photosobliques.service.helper.common.FileHelper;
import org.georchestra.photosobliques.service.helper.common.GeometryHelper;
import org.georchestra.photosobliques.service.mapper.PhotoObliqueMapper;
import org.georchestra.photosobliques.service.sm.configuration.ConfigurationService;
import org.georchestra.photosobliques.service.st.generator.datamodel.GenerationFormat;
import org.georchestra.photosobliques.storage.phototheque.entity.PhotoObliqueEntity;
import org.georchestra.photosobliques.storage.phototheque.repository.photo.PhotoObliqueCustomRepository;
import org.locationtech.jts.geom.Geometry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author FNI18300
 *
 */
@Service
@Transactional(readOnly = true)
@Slf4j
public class PhotoObliqueServiceImpl implements PhotoObliqueService {

	private final PhotoObliqueCustomRepository photoObliqueCustomRepository;
	private final PhotoObliqueMapper photoObliqueMapper;
	private final GeometryHelper geometryHelper;
	private final ConfigurationService configurationService;
	private final FileHelper fileHelper;

	public PhotoObliqueServiceImpl(PhotoObliqueCustomRepository photoObliqueCustomRepository,
								   PhotoObliqueMapper photoObliqueMapper,
								   GeometryHelper geometryHelper,
								   ConfigurationService configurationService,
								   FileHelper fileHelper,
								   @Qualifier("photothequeTransactionManager") TransactionManager transactionManager) {
		this.photoObliqueCustomRepository = photoObliqueCustomRepository;
		this.photoObliqueMapper = photoObliqueMapper;
		this.geometryHelper = geometryHelper;
		this.configurationService = configurationService;
		this.fileHelper = fileHelper;
	}

	@Override
	public List<PhotoOblique> searchPhotoOblique(PhotoObliqueSearchCriteria photoObliqueSearchCriteria, Pageable pageable) throws AppServiceException {
		checkGeometry(photoObliqueSearchCriteria.getGeometry());
		List<Tuple> photosObliquesEntities = photoObliqueCustomRepository.searchPhotosObliquesWithRelevance(photoObliqueSearchCriteria,
				configurationService.getApplicationConfiguration().getToleranceAngle(), pageable);

		return mapAndFillPhotoOblique(photosObliquesEntities);
	}


	private List<PhotoOblique> mapAndFillPhotoOblique(List<Tuple> photosObliquesEntities) {
		List<PhotoOblique> photoObliques = new ArrayList<>();

		for (Tuple tuple : photosObliquesEntities) {
			PhotoObliqueEntity entity = (PhotoObliqueEntity) tuple.get(0);
			PhotoOblique photoOblique = photoObliqueMapper.entityToDto(entity, configurationService);

			photoOblique.setRelevance((Double) tuple.get(1));
			photoObliques.add(photoOblique);
		}

		return photoObliques;
	}


	@Override
	public Integer countPhotoObliques(PhotoObliqueSearchCriteria photoObliqueSearchCriteria) throws AppServiceException {
		checkGeometry(photoObliqueSearchCriteria.getGeometry());
		return photoObliqueCustomRepository.countPhotosObliques(photoObliqueSearchCriteria, configurationService.getApplicationConfiguration().getToleranceAngle());
	}

	private void checkGeometry(String wkt) throws AppServiceBadRequestException {
		Geometry geometry = geometryHelper.convertGeometry(wkt);
		if(geometry == null) {
			throw new AppServiceBadRequestException("Aucune géométrie n'a été trouvée");
		}
	}

	@Override
	public List<String> searchOwners(String geometryWKT) throws AppServiceException {
		checkGeometry(geometryWKT);
		return photoObliqueCustomRepository.searchOwners(geometryWKT);
	}

	@Override
	public List<String> searchProviders(String geometryWKT) throws AppServiceException {
		checkGeometry(geometryWKT);
		return photoObliqueCustomRepository.searchProviders(geometryWKT);
	}

	@Override
	public List<Integer> searchYears(String geometryWKT) throws AppServiceException {
		checkGeometry(geometryWKT);
		return photoObliqueCustomRepository.searchYears(geometryWKT);
	}

	@Override
	public DocumentContent downloadPhotos(List<String> photoIds, String zipName, String prefix) throws AppServiceException {
		String path = configurationService.getApplicationConfiguration().getAccesPhotosHD();
		Integer maxCartSize = configurationService.getApplicationConfiguration().getMaxCartSize();
		if(photoIds.size() > maxCartSize) {
			throw new AppServiceBadRequestException("Le nombre de photo est supérieur au maximum autorisé (" + maxCartSize + ")");
		}
		String fileName;
		if(StringUtils.isEmpty(zipName)) {
			// Formater la date et l'heure en une chaîne de caractères
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_H-mm");
			fileName = LocalDateTime.now().format(formatter);
		} else {
			fileName = zipName;
		}

		try {
			List<DocumentContent> files = getFilesFromPhotoIds(photoIds, path, prefix);
			return fileHelper.zipFiles(fileName, files);
		} catch (IOException e) {
			throw new AppServiceException("Échec lors de la création du zip", AppServiceExceptionsStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Recherche les entités photos par IDs et extrait les photos d'un répertoire donné
	 * @param photoIds Les IDs
	 * @param path Le chemin du répertoire contenant les photos
	 * @param prefix si non null, renomme tous les fichiers en ajoutant le prefix
	 * @return List<DocumentContent>
	 * @throws IOException
	 */
	private List<DocumentContent> getFilesFromPhotoIds(List<String> photoIds, String path, String prefix) throws IOException {
		List<DocumentContent> documentContents = new ArrayList<>();
		List<Tuple> filesNameAndMention = photoObliqueCustomRepository.getFilesNameById(photoIds);

		List<String> fileNames = new ArrayList<>();

		//Si prefix n'est pas null, on renomme tous les fichiers en l'ajoutant
		prefix = StringUtils.isNotEmpty(prefix)?prefix:"";
		for (Tuple tuple : filesNameAndMention) {
			String searchedFileName = (String) tuple.get(0);
			File file = new File(path, searchedFileName);

			// Pour éviter l'erreur lors du zip si plusieurs photos pointent vers le même fichier
			String newFileName;
			if(fileNames.contains(searchedFileName)) {
				String[] splitName = searchedFileName.split("\\.");
				long count = fileNames.stream().filter(name -> name.equals(searchedFileName)).count();
				newFileName = prefix + splitName[0] + "(" + count + ")." + splitName[1];
			} else {
				newFileName = prefix + searchedFileName;
			}
			fileNames.add(searchedFileName);
			DocumentContent documentContent = new DocumentContent(newFileName, GenerationFormat.JPEG.getTypeMime(), file.length(), file);

			//generation du fichier text mention portant le même nom que la photo
			String mention = (String) tuple.get(1);
			String mentionName = GenerationFormat.TEXT.generateFileName(newFileName.split("\\.")[0]);

			File mentionFile = fileHelper.createTemporaryFile(mentionName, ".txt");

			try {
				if(documentContent.getFileStream() != null) {
					writeTextToFile(mention, mentionFile);
					documentContents.add(documentContent);
				}
			} catch (FileNotFoundException e) {
				//Photo présente en bdd mais absente dans le dossier
				log.warn("photo ( " + searchedFileName + " ) introuvable" + "dans le dossier : " + path);
				writeTextToFile("Fichier introuvable", mentionFile);
			}
			documentContents.add(new DocumentContent(mentionName, GenerationFormat.TEXT.getTypeMime(), mentionFile.length(), mentionFile));
		}
		return documentContents;
	}

	/**
	 * Genère un fichier texte et y écrit le texte
	 * @param content
	 * @param file
	 * @throws IOException
	 */
	private void writeTextToFile(String content, File file) throws IOException{
		byte[] strToBytes = content.getBytes();

		try (FileOutputStream fOutput = new FileOutputStream(file)) {
			fOutput.write(strToBytes);
		}
	}

}
