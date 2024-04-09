package org.georchestra.photosobliques.service.sm.photo;

import jakarta.persistence.EntityManager;
import org.georchestra.photosobliques.core.bean.PhotoOblique;
import org.georchestra.photosobliques.core.bean.photo.PhotoObliqueSearchCriteria;
import org.georchestra.photosobliques.core.common.DocumentContent;
import org.georchestra.photosobliques.service.PhotosObliquesSpringBootTest;
import org.georchestra.photosobliques.service.exception.AppServiceException;
import org.georchestra.photosobliques.service.helper.common.FileHelper;
import org.georchestra.photosobliques.service.helper.common.GeometryHelper;
import org.georchestra.photosobliques.storage.phototheque.entity.PhotoObliqueEntity;
import org.georchestra.photosobliques.storage.phototheque.repository.photo.PhotoObliqueRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.locationtech.jts.geom.Geometry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.TransactionManager;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@PhotosObliquesSpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PhotoObliqueServiceTest {

    @Autowired
    PhotoObliqueService photoObliqueService;
    @Autowired
    FileHelper fileHelper;


    private static final String WKT_SAINT_GREGOIRE = "POLYGON ((-1.704287 48.145014, -1.704287 48.156925, -1.666183 48.156925, -1.666183 48.145014, -1.704287 48.145014))";
    private static final String WKT_CESSON_SEVIGNE = "POLYGON ((-1.620674 48.132757, -1.58257 48.13333, -1.586174 48.118549, -1.597502 48.111901, -1.622562 48.110411, -1.625136 48.121299, -1.620674 48.132757))";
    private static final String WKT_PLACE_REPUBLIQUE = "POLYGON ((-1.680222 48.110211, -1.67783 48.110189, -1.67783 48.109355, -1.680157 48.10934, -1.680222 48.110211))";
    private static final String WKT_COLOMBIER = "POLYGON ((-1.684343 48.105611, -1.678099 48.106385, -1.677584 48.104379, -1.681725 48.10382, -1.684192 48.104594, -1.684343 48.105611))";
    private static final String WKT_RENNES = "POLYGON ((-1.711137 48.114767, -1.701182 48.129205, -1.662735 48.132642, -1.639735 48.12508, -1.626004 48.105368, -1.641452 48.095509, -1.658959 48.090693, -1.685391 48.089776, -1.708391 48.100783, -1.711137 48.114767))";
    private static final String WKT_RENNES_METROPOLE = "POLYGON ((-1.812948 47.982568, -1.807456 48.089088, -1.892589 48.197218, -1.686622 48.257599, -1.524595 48.228332, -1.439462 48.155093, -1.51361 48.04871, -1.51361 47.971537, -1.694861 47.967859, -1.812948 47.982568))";


    @BeforeAll
    public static void setup(@Autowired PhotoObliqueRepository photoObliqueRepository,
                             @Autowired GeometryHelper geometryHelper) {
        PhotoObliqueEntity photoOblique1 = new PhotoObliqueEntity();
        photoOblique1.setId("Photo-0001");
        photoOblique1.setFile("Photo-0001.png");
        photoOblique1.setMention("REPUBLIQUE");
        photoOblique1.setDownloadable(1);
        photoOblique1.setProvider("Provider 1");
        photoOblique1.setOwner("Owner 1");
        photoOblique1.setYear(2020);
        photoOblique1.setDate(LocalDateTime.of(2020,1,1, 12, 0));
        Geometry geometryRepublique = geometryHelper.convertGeometry(WKT_PLACE_REPUBLIQUE);
        geometryRepublique.setSRID(3948);
        photoOblique1.setShape(geometryRepublique);
        photoObliqueRepository.save(photoOblique1);

        PhotoObliqueEntity photoOblique2 = new PhotoObliqueEntity();
        photoOblique2.setId("Photo-0002");
        photoOblique2.setFile("Photo-0002.png");
        photoOblique2.setDownloadable(1);
        photoOblique2.setMention("COLOMBIER");
        photoOblique2.setProvider("Provider 1");
        photoOblique2.setOwner("Owner 1");
        photoOblique2.setYear(2021);
        photoOblique2.setDate(LocalDateTime.of(2021,1,1, 12, 0));
        Geometry geometryColombier = geometryHelper.convertGeometry(WKT_COLOMBIER);
        geometryColombier.setSRID(3948);
        photoOblique2.setShape(geometryColombier);
        photoObliqueRepository.save(photoOblique2);

        PhotoObliqueEntity photoOblique3 = new PhotoObliqueEntity();
        photoOblique3.setId("Photo-0003");
        photoOblique3.setFile("Photo-0003.png");
        photoOblique3.setDownloadable(1);
        photoOblique3.setMention("Saint Grégoire");
        photoOblique3.setProvider("Provider 2");
        photoOblique3.setOwner("Owner 2");
        photoOblique3.setYear(2022);
        photoOblique3.setDate(LocalDateTime.of(2022,1,1, 12, 0));
        Geometry geometryStGregoire = geometryHelper.convertGeometry(WKT_SAINT_GREGOIRE);
        geometryStGregoire.setSRID(3948);
        photoOblique3.setShape(geometryStGregoire);
        photoObliqueRepository.save(photoOblique3);

        PhotoObliqueEntity photoOblique4 = new PhotoObliqueEntity();
        photoOblique4.setId("Photo-0004");
        photoOblique4.setFile("Photo-0004.png");
        photoOblique4.setDownloadable(1);
        photoOblique4.setMention("Cesson");
        photoOblique4.setProvider("Provider 2");
        photoOblique4.setOwner("Owner 2");
        photoOblique4.setYear(2022);
        photoOblique4.setDate(LocalDateTime.of(2023,1,1, 12, 0));
        Geometry geometryCesson = geometryHelper.convertGeometry(WKT_CESSON_SEVIGNE);
        geometryCesson.setSRID(3948);
        photoOblique4.setShape(geometryCesson);
        photoObliqueRepository.save(photoOblique4);

        PhotoObliqueEntity photoOblique5 = new PhotoObliqueEntity();
        photoOblique5.setId("Photo-0005");
        photoOblique5.setFile("Photo-0005.png");
        photoOblique5.setDownloadable(0);
        photoOblique5.setShape(geometryCesson);
        photoOblique5.setProvider("Provider 3");
        photoOblique5.setOwner("Owner 3");
        photoOblique5.setYear(2024);
        photoObliqueRepository.save(photoOblique5);

        PhotoObliqueEntity photoOblique6 = new PhotoObliqueEntity();
        photoOblique6.setId("Photo-0006");
        photoOblique6.setFile("Photo-0006.png");
        photoOblique6.setDownloadable(1);
        photoOblique6.setMention("Cesson");
        photoOblique6.setProvider("Provider 2");
        photoOblique6.setOwner("Owner 2");
        photoOblique6.setYear(2022);
        photoOblique6.setDate(LocalDateTime.of(2023,1,1, 12, 0));
        photoOblique6.setShape(geometryCesson);
        photoObliqueRepository.save(photoOblique6);
    }

    @Test
    void testSearchPhotosRennesMetropole() throws AppServiceException {
        PhotoObliqueSearchCriteria criteria = new PhotoObliqueSearchCriteria();
        criteria.setGeometry(WKT_RENNES_METROPOLE);

        List<PhotoOblique> photos = photoObliqueService.searchPhotoOblique(criteria, Pageable.ofSize(20));
        assertEquals(5, photos.size());

    }

    @Test
    void testSearchPhotosRennes() throws AppServiceException {
        PhotoObliqueSearchCriteria criteria = new PhotoObliqueSearchCriteria();
        criteria.setGeometry(WKT_RENNES);

        List<PhotoOblique> photos = photoObliqueService.searchPhotoOblique(criteria, Pageable.ofSize(20));
        assertEquals(2, photos.size());

    }

    @Test
    void testCountPhotos() throws AppServiceException {
        PhotoObliqueSearchCriteria criteria = new PhotoObliqueSearchCriteria();
        criteria.setGeometry(WKT_RENNES_METROPOLE);

        assertEquals(5, photoObliqueService.countPhotoObliques(criteria));

    }

    @Test
    void testSearchOwners() throws AppServiceException {
        List<String> owners = photoObliqueService.searchOwners(WKT_RENNES);

        assertNotNull(owners);
        assertEquals(1, owners.size());
        assertTrue(owners.contains("Owner 1"));

        owners = photoObliqueService.searchOwners(WKT_RENNES_METROPOLE);

        assertNotNull(owners);
        assertEquals(2, owners.size());
        assertTrue(owners.containsAll(Arrays.asList("Owner 1", "Owner 2")));
        assertTrue(owners.contains("Owner 2"));

    }

    @Test
    void testSearchProviders() throws AppServiceException {
        List<String> providers = photoObliqueService.searchProviders(WKT_RENNES);

        assertNotNull(providers);
        assertEquals(1, providers.size());
        assertTrue(providers.contains("Provider 1"));

        providers = photoObliqueService.searchProviders(WKT_RENNES_METROPOLE);

        assertNotNull(providers);
        assertEquals(2, providers.size());
        assertTrue(providers.containsAll(Arrays.asList("Provider 1", "Provider 2")));
        assertTrue(providers.contains("Provider 2"));

    }

    @Test
    void testSearchYears() throws AppServiceException {
        List<Integer> years = photoObliqueService.searchYears(WKT_RENNES);

        assertNotNull(years);
        assertEquals(2, years.size());
        assertTrue(years.containsAll(Arrays.asList(2020, 2021)));

        years = photoObliqueService.searchYears(WKT_RENNES_METROPOLE);

        assertNotNull(years);
        assertEquals(3, years.size());
        assertFalse(years.contains(2024));
        assertTrue(years.containsAll(Arrays.asList(2020, 2021, 2022)));

    }

    @Test
    void testDownloadOnePhoto() throws AppServiceException {
        List<String> photoIDs = List.of("Photo-0001");
        DocumentContent documentContent = photoObliqueService.downloadPhotos(photoIDs, null, null);

        assertNotNull(documentContent);
        assertNotNull(documentContent.getFile());

        List<File> returnedFiles = fileHelper.unzipFile(documentContent.getFile());
        returnedFiles.forEach(File::deleteOnExit);

        assertEquals(2, returnedFiles.size());

        List<String> filesNames = returnedFiles.stream().map(File::getName).toList();
        assertTrue(filesNames.contains("Photo-0001.png"));
        assertTrue(filesNames.contains("Photo-0001.txt"));

    }

    @Test
    void testDownloadPhotoNotFound() throws AppServiceException {
        List<String> photoIDs = List.of("Photo-0006");
        DocumentContent documentContent = photoObliqueService.downloadPhotos(photoIDs, null, null);

        assertNotNull(documentContent);
        assertNotNull(documentContent.getFile());

        List<File> returnedFiles = fileHelper.unzipFile(documentContent.getFile());
        returnedFiles.forEach(File::deleteOnExit);
        assertEquals(1, returnedFiles.size());
        assertEquals("Photo-0006.txt", returnedFiles.get(0).getName());

    }

    @Test
    void testDownloadPhotos() throws AppServiceException {
        List<String> photoIDs = Arrays.asList("Photo-0001", "Photo-0002", "Photo-0003", "Photo-0004", "Photo-0005", "Photo-0006");
        DocumentContent documentContent = photoObliqueService.downloadPhotos(photoIDs, "zipName", null);

        assertNotNull(documentContent);
        assertNotNull(documentContent.getFile());
        assertEquals("zipName", documentContent.getFileName());

        List<File> returnedFiles = fileHelper.unzipFile(documentContent.getFile());
        returnedFiles.forEach(File::deleteOnExit);

    }

    @Test
    void testDownloadPhotosPrefix() throws AppServiceException {
        List<String> photoIDs = Arrays.asList("Photo-0001", "Photo-0002", "Photo-0003", "Photo-0004", "Photo-0005", "Photo-0006");
        DocumentContent documentContent = photoObliqueService.downloadPhotos(photoIDs, null, "prefix");
        documentContent.getFile().deleteOnExit();

        assertNotNull(documentContent);
        assertNotNull(documentContent.getFile());

        List<File> returnedFiles = fileHelper.unzipFile(documentContent.getFile());
        returnedFiles.forEach(File::deleteOnExit);

        for (File returnedFile : returnedFiles) {
            assertTrue(returnedFile.getName().contains("prefix"));
        }

    }

    @Test
    void testMaxCartDownloadPhotos() throws AppServiceException {
        List<String> photoIDs = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            photoIDs.add("Photo-00" + i);
        }
        DocumentContent documentContent = photoObliqueService.downloadPhotos(photoIDs, null, "prefix");
        documentContent.getFile().deleteOnExit();

        assertNotNull(documentContent);
        assertNotNull(documentContent.getFile());

        photoIDs.add("Photo-0021");

        assertThrows(AppServiceException.class, () -> photoObliqueService.downloadPhotos(photoIDs, null, null));
    }

}
