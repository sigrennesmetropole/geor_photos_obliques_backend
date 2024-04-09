package org.georchestra.photosobliques.service.sm.statistiques;

import lombok.AllArgsConstructor;
import org.georchestra.photosobliques.core.bean.statistiques.Statistiques;
import org.georchestra.photosobliques.service.mapper.StatistiquesMapper;
import org.georchestra.photosobliques.storage.statistiques.repository.StatistiquesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class StatistiquesServicesImpl implements StatistiquesServices {

    private StatistiquesRepository statistiquesRepository;
    private StatistiquesMapper statistiquesMapper;

    @Override
    public void saveStatistiques(Statistiques statistiques) {
        statistiquesRepository.save(statistiquesMapper.dtoToEntity(statistiques));
    }

}
