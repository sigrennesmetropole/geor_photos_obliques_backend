/**
 * SHOM - Search
 */
package org.georchestra.photosobliques.facade.util;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * @author FNI18300
 *
 */
public class UnpagedSortable implements Pageable {

	private final Sort sort;

	public static UnpagedSortable of(Sort sort) {
		return new UnpagedSortable(sort);
	}

	protected UnpagedSortable(Sort sort) {
		this.sort = sort;
	}

	@Override
	public boolean isPaged() {
		return false;
	}

	@Override
	public Pageable previousOrFirst() {
		return this;
	}

	@Override
	public Pageable next() {
		return this;
	}

	@Override
	public boolean hasPrevious() {
		return false;
	}

	@Override
	public Sort getSort() {
		return sort;
	}

	@Override
	public int getPageSize() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getPageNumber() {
		throw new UnsupportedOperationException();
	}

	@Override
	public long getOffset() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Pageable first() {
		return this;
	}

	@Override
	public Pageable withPage(int pageNumber) {

		if (pageNumber == 0) {
			return this;
		}

		throw new UnsupportedOperationException();
	}

}
