package org.georchestra.photosobliques.core.bean.statistiques;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Setter
@Getter
@ToString
@AllArgsConstructor
public class StatistiquesData {

    private Map<String, String> data;
}
