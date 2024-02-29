/**
 *
 */
package org.georchestra.photosobliques.service.exception;

/**
 * @author FNI18300
 *
 */
public class ParcelleCadastraleUsedByProgrammeException extends UsedByOtherException {

	private static final long serialVersionUID = 925256224388964853L;

	private static final String VALIDATION_ERROR_PARCELLE_CADASTRALE_USED_BY_PROGRAMME = "validation.error.parcelle-cadastrale.used-by-programme";

	public ParcelleCadastraleUsedByProgrammeException(Throwable cause) {
		super(VALIDATION_ERROR_PARCELLE_CADASTRALE_USED_BY_PROGRAMME, cause);
	}

	public ParcelleCadastraleUsedByProgrammeException() {
		super(VALIDATION_ERROR_PARCELLE_CADASTRALE_USED_BY_PROGRAMME);
	}

}
