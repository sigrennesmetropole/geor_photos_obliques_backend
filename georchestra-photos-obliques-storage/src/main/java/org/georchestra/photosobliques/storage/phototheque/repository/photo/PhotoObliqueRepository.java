package org.georchestra.photosobliques.storage.phototheque.repository.photo;

import org.georchestra.photosobliques.storage.phototheque.entity.PhotoObliqueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoObliqueRepository extends JpaRepository<PhotoObliqueEntity, String> {
}
