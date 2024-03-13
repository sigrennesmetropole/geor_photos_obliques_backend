package org.georchestra.photosobliques.storage.repository.photo;

import org.georchestra.photosobliques.storage.entity.PhotoObliqueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoObliqueRepository extends JpaRepository<PhotoObliqueEntity, String> {
}
