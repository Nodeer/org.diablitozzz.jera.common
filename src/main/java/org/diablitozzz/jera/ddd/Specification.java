package org.diablitozzz.jera.ddd;

public interface Specification<T> {

	public boolean isSatisfiedBy(T object);
}
