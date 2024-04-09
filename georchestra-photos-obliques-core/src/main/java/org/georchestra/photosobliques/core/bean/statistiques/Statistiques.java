package org.georchestra.photosobliques.core.bean.statistiques;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Statistiques {

    private Long id;

    private String result;

    private String url;

    private String query;

    private LocalDateTime when;

    private String who;

    private Long duration;

    private StatistiquesData data;
}
