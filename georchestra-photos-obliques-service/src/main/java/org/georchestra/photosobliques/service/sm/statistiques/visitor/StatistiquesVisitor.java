package org.georchestra.photosobliques.service.sm.statistiques.visitor;

import org.georchestra.photosobliques.core.bean.statistiques.StatistiquesData;
import org.springframework.stereotype.Component;

@Component
public interface StatistiquesVisitor {
    boolean accept(String methodName);
    StatistiquesData process(Object response);
}
