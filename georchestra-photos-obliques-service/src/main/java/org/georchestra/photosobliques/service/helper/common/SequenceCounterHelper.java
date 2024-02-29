/**
 *
 */
package org.georchestra.photosobliques.service.helper.common;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.georchestra.photosobliques.storage.entity.common.SequenceCounterEntity;
import org.georchestra.photosobliques.storage.repository.common.SequenceCounterRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Ce helper doit permettre de générer la partie séquentielle annuelle de
 * certaines référence<br/>
 * Si l'on a un compteur de Dossier on utilisera le code "DOSSIER"<br/>
 * Si l'on veut un compteur annuel par département on utilisera un code tel que
 * DOSSIER_<Numéro département>
 *
 * @author FNI18300
 *
 */
@Component
@RequiredArgsConstructor
public class SequenceCounterHelper {

	private static final String REFERENCE_PREFIX_VERSION_FORMAT = "%s-%04d%04d-%d";

	private static final String REFERENCE_PREFIX_CODE_VERSION_FORMAT = "%s-%s-%04d%04d-%d";

	private static final String REFERENCE_VERSION_FORMAT = "%04d%04d-%d";

	private static final String REFERENCE_CODE_VERSION_FORMAT = "%s-%04d%04d-%d";

	private static final String REFERENCE_PREFIX_CODE_FORMAT = "%s-%s-%04d%04d";

	private static final String REFERENCE_PREFIX_FORMAT = "%s-%04d%04d";

	private static final String REFERENCE_CODE_FORMAT = "%s-%04d%04d";

	private static final String REFERENCE_FORMAT = "%04d%04d";

	private final SequenceCounterRepository sequenceCounterRepository;

	@Transactional(readOnly = false)
	public String getNewReference(String prefix, String code) {
		return getNewReference(prefix, code, LocalDate.now().getYear(), false);
	}

	@Transactional(readOnly = false)
	public String getNewCodeReference(String prefix, String code) {
		return getNewReference(prefix, code, LocalDate.now().getYear(), true);
	}

	@Transactional(readOnly = false)
	public String getNewReference(String prefix, String code, int year, boolean withCode) {
		if (StringUtils.isNotEmpty(prefix)) {
			if (withCode) {
				return String.format(REFERENCE_PREFIX_CODE_FORMAT, prefix, code, year, reserveSequenceId(code));
			} else {
				return String.format(REFERENCE_PREFIX_FORMAT, prefix, year, reserveSequenceId(code));
			}
		} else {
			if (withCode) {
				return String.format(REFERENCE_CODE_FORMAT, code, year, reserveSequenceId(code));
			} else {
				return String.format(REFERENCE_FORMAT, year, reserveSequenceId(code));
			}
		}
	}

	@Transactional(readOnly = false)
	public String getNewCodeReferenceVersion(String prefix, String code, int version) {
		return getNewReferenceVersion(prefix, code, LocalDate.now().getYear(), version, true);
	}

	@Transactional(readOnly = false)
	public String getNewReferenceVersion(String prefix, String code, int year, int version, boolean withCode) {
		if (StringUtils.isNotEmpty(prefix)) {
			if (withCode) {
				return String.format(REFERENCE_PREFIX_CODE_VERSION_FORMAT, prefix, code, year, reserveSequenceId(code),
						version);
			} else {
				return String.format(REFERENCE_PREFIX_VERSION_FORMAT, prefix, year, reserveSequenceId(code), version);
			}
		} else {
			if (withCode) {
				return String.format(REFERENCE_CODE_VERSION_FORMAT, code, year, reserveSequenceId(code), version);
			} else {
				return String.format(REFERENCE_VERSION_FORMAT, year, reserveSequenceId(code), version);
			}
		}
	}

	@Transactional(readOnly = false)
	public long reserveSequenceId(String code) {
		int year = LocalDate.now().getYear();
		return reserveSequenceId(code, year);
	}

	@Transactional(readOnly = false)
	public long reserveSequenceId(String code, int year) {
		long value = 1;
		SequenceCounterEntity sequenceCounter = sequenceCounterRepository.findOneByCode(code);

		if (sequenceCounter == null) {
			sequenceCounter = buildSequenceCounterEntity(code, year);
		} else {
			if (sequenceCounter.getAnnee() != year) {
				// changement d'année on créé une nouvelle entrée
				sequenceCounter = buildSequenceCounterEntity(code, year);
			} else {
				// A la fin de la transaction, le save de l'entité (plus bas) entraine
				// l'incrementation
				// de la valeur du compteur (annoté avec @Version dans l'entité
				// SequenceCounterEntity)
				value = sequenceCounter.getValue() + 1;
			}
		}

		sequenceCounterRepository.save(sequenceCounter);

		return value;
	}

	/**
	 * Crée une nouvelle entité {@link SequenceCounterEntity} pour un code et une
	 * année donnée
	 *
	 * @param code
	 * @param year année
	 * @return Entité {@link SequenceCounterEntity}
	 */
	private SequenceCounterEntity buildSequenceCounterEntity(String code, int year) {
		SequenceCounterEntity sequenceCounter = new SequenceCounterEntity();
		sequenceCounter.setUuid(UUID.randomUUID());
		sequenceCounter.setAnnee(year);
		sequenceCounter.setCode(code);
		sequenceCounter.setValue(1);
		return sequenceCounter;
	}
}
