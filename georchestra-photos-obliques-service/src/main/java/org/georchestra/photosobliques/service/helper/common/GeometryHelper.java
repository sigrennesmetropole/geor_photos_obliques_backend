/**
 *
 */
package org.georchestra.photosobliques.service.helper.common;

import lombok.extern.slf4j.Slf4j;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.WKTReader;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.georchestra.photosobliques.service.exception.GeometryException;
import org.springframework.stereotype.Component;

/**
 * @author FNI18300
 *
 */
@Component
@Slf4j
public class GeometryHelper {

	public static final double M2_TO_ARE = 100.0d;
	public static final double M2_TO_HECTARE = 10000.0d;
	public static final double M2_TO_KM2 = 1000000.0d;
	public static final String EPSG_4326 = "EPSG:4326";
	public static final String EPSG_32632 = "EPSG:32632";

	/**
	 * Extraction de la géométrie à partir de sa représentation WKT
	 *
	 * @param wktGeometry
	 * @return
	 */
	public Geometry convertGeometry(String wktGeometry) {
		if (wktGeometry == null) {
			return null;
		}
		WKTReader reader = new WKTReader();
		try {
			return reader.read(wktGeometry);
		} catch (Exception e) {
			log.warn("Failed to convert geometry from apicadastre " + wktGeometry, e);
			return null;
		}
	}

	public Double computeSurfaceWGS84(Geometry geometry)
			throws GeometryException {
		return computeSurface(geometry, EPSG_4326);
	}

	/**
	 * Calcule en mètre2 de la surface
	 *
	 * @param geometry
	 * @return
	 * @throws GeometryException
	 */
	public Double computeSurface(Geometry geometry, String crsSource) throws GeometryException {
		Double surface = 0.0d;
		if (geometry != null) {
			try {// conversion sur un projection en metre
				CoordinateReferenceSystem sourceCRS = CRS.decode(crsSource);
				CoordinateReferenceSystem targetCRS = CRS.decode(EPSG_32632);

				MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS);
				Geometry targetGeometry = JTS.transform(geometry, transform);

				surface = targetGeometry.getArea();
			} catch (Exception e) {
				throw new GeometryException("Failed to get geometry area", e);
			}
		}
		return surface;
	}

	public Double convertm2tokm2(Double value) {
		return value / M2_TO_KM2;
	}

	public Double convertm2toHectare(Double value) {
		return value / M2_TO_HECTARE;
	}

	public Double convertm2toAre(Double value) {
		return value / M2_TO_ARE;
	}

}
