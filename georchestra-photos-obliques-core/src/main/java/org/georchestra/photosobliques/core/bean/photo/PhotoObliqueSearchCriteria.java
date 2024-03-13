package org.georchestra.photosobliques.core.bean.photo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString
public class PhotoObliqueSearchCriteria {

    private String geometry;
    private Integer startDate;
    private Integer endDate;
    private Double angleDegre;
    private String provider;
    private String owner;

}
