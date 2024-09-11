 package org.georchestra.photosobliques.storage.statistiques.entity;

 import jakarta.persistence.Column;
 import jakarta.persistence.Convert;
 import jakarta.persistence.Entity;
 import jakarta.persistence.GeneratedValue;
 import jakarta.persistence.GenerationType;
 import jakarta.persistence.Id;
 import jakarta.persistence.Table;
 import lombok.Getter;
 import lombok.NoArgsConstructor;
 import lombok.Setter;
 import lombok.ToString;
 import lombok.experimental.SuperBuilder;
 import org.georchestra.photosobliques.core.bean.statistiques.StatistiquesData;
 import org.georchestra.photosobliques.storage.statistiques.converter.StatistiquesAttributeConverter;

 import java.io.Serial;
 import java.io.Serializable;
 import java.time.LocalDateTime;
 import java.util.Objects;

 /**
 * User entity
 */

@Table(name = "statistiques_photos_obliques")
@Setter
@Getter
@ToString
@NoArgsConstructor
@SuperBuilder
@Entity
public class StatistiquesEntity implements Serializable {

	@Serial
	private static final long serialVersionUID = 8103560742221958895L;

	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	 @Column(name="result")
	 private String result;

	 @Column(name="url")
	 private String url;

	 @Column(name="query")
	 private String query;

	 @Column(name="when_")
	 private LocalDateTime when;

	 @Column(name="who")
	 private String who;

	 @Column(name="duration")
	 private Long duration;

	 @Convert(converter = StatistiquesAttributeConverter.class)
	 @Column(name="data")
	 private StatistiquesData data;


	 @Override
	 public boolean equals(Object o) {
		 if (this == o) return true;
		 if (o == null || getClass() != o.getClass()) return false;

		 StatistiquesEntity that = (StatistiquesEntity) o;

		 return Objects.equals(id, that.id);
	 }

	 @Override
	 public int hashCode() {
		 return id != null ? id.hashCode() : 0;
	 }
 }
