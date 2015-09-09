package org.diablitozzz.jera.ddd;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

public class ModelCollection<T extends Model> implements Iterable<T> {

	public static interface Matcher<T> {

		public boolean isMatch(T object);
	}

	private final Collection<T> data;

	public ModelCollection(final Collection<T> data) {
		this.data = data;
	}

	public boolean contains(final Object object) {

		for (final T item : this) {
			if (item.equals(object)) {
				return true;
			}
		}
		return false;
	}

	public boolean containsById(final Object id) {
		return this.getById(id) != null;
	}

	public boolean containsBySpec(final Specification<T> spec) {
		return this.getBySpec(spec) != null;
	}

	public T getById(final Object id) {

		for (final T item : this.data) {

			if (Check.isEquals(item.getIdAsObject(), id)) {
				return item;
			}
		}
		return null;
	}

	public T getBySpec(final Specification<T> spec) {

		for (final T item : this.data) {
			if (spec.isSatisfiedBy(item)) {
				return item;
			}
		}
		return null;
	}

	public Collection<T> getCollection() {
		return Collections.unmodifiableCollection(this.data);
	}

	public T getFirst() {
		return this.data.iterator().next();
	}

	public boolean isEmpty() {
		return this.data.isEmpty();
	}

	public boolean isNotEmpty() {
		return !this.data.isEmpty();
	}

	@Override
	public Iterator<T> iterator() {
		return this.data.iterator();
	}

	public T match(final Matcher<T> matcher) {

		for (final T item : this.data) {
			if (matcher.isMatch(item)) {
				return item;
			}
		}
		return null;
	}

	public int size() {
		return this.data.size();
	}

	@Override
	public String toString() {

		final StringBuilder out = new StringBuilder();
		for (final T item : this) {
			out.append(item.toString());
			out.append(" | ");
		}
		return out.toString();
	}
}
