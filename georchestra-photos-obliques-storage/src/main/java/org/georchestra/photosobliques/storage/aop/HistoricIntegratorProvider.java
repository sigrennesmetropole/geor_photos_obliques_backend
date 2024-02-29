/**
 *
 */
package org.georchestra.photosobliques.storage.aop;

import org.hibernate.integrator.spi.Integrator;
import org.hibernate.jpa.boot.spi.IntegratorProvider;

import java.util.List;

/**
 * @author FNI18300
 *
 */
public class HistoricIntegratorProvider implements IntegratorProvider {

	@Override
	public List<Integrator> getIntegrators() {
		return List.of(new HistoricIntegrator());
	}

}
