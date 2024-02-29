package org.georchestra.photosobliques.core.bean.historic;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@ToString
public class HistoricDataPhotosObliquesCriteria {

    private UUID whichUuid;

    private String whichType;

    private LocalDateTime dateMin;

    private LocalDateTime dateMax;

    private String who;
}
