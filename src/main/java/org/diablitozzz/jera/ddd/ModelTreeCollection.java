package org.diablitozzz.jera.ddd;

import java.util.Collection;

public class ModelTreeCollection<T extends TreeModel<T>> extends ModelCollection<T> {

	public ModelTreeCollection(final Collection<T> data) {
		super(data);
	}

	public boolean containsByIdInherited(final Object id) {
		return this.getByIdInherited(id) != null;
	}

	public boolean containsBySpecInherited(final Specification<T> spec) {
		return this.getBySpecInherited(spec) != null;
	}

	/**
	 * Ищет по id рекурсивно 
	 */
	public T getByIdInherited(final Object id) {
		return this.getByIdInherited(id, this);
	}

	@SuppressWarnings("unchecked")
	private T getByIdInherited(final Object id, final ModelTreeCollection<T> collection) {

		for (final T child : collection.getCollection()) {

			if (Check.isEquals(child.getIdAsObject(), id)) {
				return child;
			}

			if (child.getChildren().isNotEmpty()) {

				final T itemInChild = this.getByIdInherited(id, (ModelTreeCollection<T>) child.getChildren());
				if (itemInChild != null) {
					return itemInChild;
				}
			}

		}
		return null;
	}

	/**
	 * Ищет по спецификации рекурсивно 
	 */
	public T getBySpecInherited(final Specification<T> spec) {
		return this.getBySpecInherited(spec, this);
	}

	@SuppressWarnings("unchecked")
	private T getBySpecInherited(final Specification<T> spec, final ModelTreeCollection<T> collection) {

		for (final T child : collection.getCollection()) {
			if (spec.isSatisfiedBy(child)) {
				return child;
			}
			if (child.getChildren().isNotEmpty()) {
				final T itemInChild = this.getBySpecInherited(spec, (ModelTreeCollection<T>) child.getChildren());
				if (itemInChild != null) {
					return itemInChild;
				}
			}
		}
		return null;
	}

	public T matchInherited(final Matcher<T> matcher) {
		return this.matchInherited(matcher, this);
	}

	@SuppressWarnings("unchecked")
	private T matchInherited(final Matcher<T> matcher, final ModelTreeCollection<T> collection) {

		for (final T child : collection.getCollection()) {

			if (matcher.isMatch(child)) {
				return child;
			}

			if (child.getChildren().isNotEmpty()) {
				final T itemInChild = this.matchInherited(matcher, (ModelTreeCollection<T>) child.getChildren());
				if (itemInChild != null) {
					return itemInChild;
				}
			}

		}
		return null;
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
