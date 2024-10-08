package org.georchestra.photosobliques.storage.phototheque.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.locationtech.jts.geom.Geometry;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * User entity
 */
@Table(name = "v_photooblique_emprise")
@Setter
@Getter
@ToString
@NoArgsConstructor
@SuperBuilder
@Entity
public class  PhotoObliqueEntity implements Serializable {

	@Serial
	private static final long serialVersionUID = 6229807753841014436L;

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "fichier")
	private String file;

	@Column(name = "annee")
	private int year;

	@Column(name = "date_")
	private LocalDateTime date;

	@Column(name = "heure")
	private String time;

	@Column(name = "comment")
	private String comment;

	@Column(name = "angle_deg")
	private BigDecimal angleDegree;

	@Column(name = "angle_grd")
	private BigDecimal angleGradient;

	@Column(name = "presta")
	private String provider;

	@Column(name = "proprio")
	private String owner;

	@Column(name = "telecharg")
	private int downloadable;

	@Column(name = "mention")
	private String mention;

	@Column(name = "commune")
	private String town;

	@Column(name = "shape", columnDefinition = "public.geometry")
	private Geometry shape;

	@Column(name = "taille_fichier")
	private Integer fileSize;

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		PhotoObliqueEntity entity = (PhotoObliqueEntity) o;

		return Objects.equals(id, entity.id);
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result;
		return result;
	}
}
