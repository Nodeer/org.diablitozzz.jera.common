package org.diablitozzz.jera.db;

public class DbArray {

	private final String type;
	private final Object[] elements;

	public DbArray(final String type, final Object[] elements) {
		this.type = type;
		this.elements = elements;
	}

	public Object[] getElements() {
		return this.elements;
	}

	public String getType() {
		return this.type;
	}

	public int size() {
		return this.elements.length;
	}
}
