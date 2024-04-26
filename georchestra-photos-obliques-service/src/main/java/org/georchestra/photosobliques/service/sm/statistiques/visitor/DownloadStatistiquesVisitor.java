package org.georchestra.photosobliques.service.sm.statistiques.visitor;

import lombok.extern.slf4j.Slf4j;
import org.georchestra.photosobliques.core.bean.statistiques.StatistiquesData;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
@Slf4j
public class DownloadStatistiquesVisitor extends AbstractStatistiquesVisitor {
    private static final String HEADER_FILESIZE = "File-Size";

    public DownloadStatistiquesVisitor() {
        super("downloadPhotos");
    }

    @Override
    public StatistiquesData process(Object object) {
        ResponseEntity<Resource> response = (ResponseEntity<Resource>) object;
        Map<String, String> data = new HashMap<>();

        response.getHeaders();
        List<String> values = response.getHeaders().get(HEADER_FILESIZE);
        if(values != null) {
            String fileSizeStr = values.get(0);
            Long fileSize = Long.valueOf(fileSizeStr);
            data.put("zipSize", formatBytes(fileSize));
        }
        return new StatistiquesData(data);
    }

    public static String formatBytes(long bytes) {
        double kilobytes = bytes / 1024.0;
        double megabytes = kilobytes / 1024.0;
        double gigabytes = megabytes / 1024.0;

        String unit;
        double value;

        if (gigabytes >= 1.0) {
            unit = "Go";
            value = gigabytes;
        } else if (megabytes >= 1.0) {
            unit = "Mo";
            value = megabytes;
        } else if (kilobytes >= 1.0) {
            unit = "Ko";
            value = kilobytes;
        } else {
            unit = "octets";
            value = bytes;
        }

        return String.format("%.2f %s", value, unit);
    }
}
