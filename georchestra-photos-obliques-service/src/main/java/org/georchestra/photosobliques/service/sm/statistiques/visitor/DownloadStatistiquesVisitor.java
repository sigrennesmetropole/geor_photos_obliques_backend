package org.georchestra.photosobliques.service.sm.statistiques.visitor;

import lombok.extern.slf4j.Slf4j;
import org.georchestra.photosobliques.core.bean.statistiques.StatistiquesData;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Component
@Slf4j
public class DownloadStatistiquesVisitor extends AbstractStatistiquesVisitor {

    public DownloadStatistiquesVisitor() {
        super("downloadPhotos");
    }

    @Override
    public StatistiquesData process(Object object) {
        ResponseEntity<Resource> response = (ResponseEntity<Resource>) object;
        Map<String, String> data = new HashMap<>();
        if(response.getBody() != null) {
            try {
                data.put("zipSize", sizeInMegaBytes(response.getBody().getFile()));
                return new StatistiquesData(data);
            } catch (Exception e) {
                log.error("Echec lors de la lecture du fichier temporaire");
            }
        }
        return null;
    }

    private static String sizeInMegaBytes(File file) {
        return (double) file.length() / (1024 * 1024) + " mb";
    }
}
