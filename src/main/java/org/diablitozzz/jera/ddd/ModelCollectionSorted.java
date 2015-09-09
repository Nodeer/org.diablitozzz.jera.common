package org.diablitozzz.jera.ddd;

import java.util.List;

public class ModelCollectionSorted<T extends ModelSorted> extends ModelCollection<T> {

	private final List<T> data;

	public ModelCollectionSorted(final List<T> data) {
		super(data);
		this.data = data;
	}

	public void moveAfter(final T item, final T item2) {
		int position = this.data.indexOf(item2);
		if (position < this.data.indexOf(item)) {
			position++;
		}

		this.moveTo(item, position);

	}

	public void moveBefore(final T item, final T item2) {
		int position = this.data.indexOf(item2);
		if (position > this.data.indexOf(item)) {
			position--;
		}

		this.moveTo(item, position);
	}

	public void moveFirst(final T item) {
		this.moveTo(item, 0);
	}

	public void moveLast(final T item) {
		this.moveTo(item, this.data.size() - 1);
	}

	public void moveTo(final T item, int position) {

		if (position < 0) {
			position = 0;
		}
		if (position >= this.data.size()) {
			position = this.data.size() - 1;
		}

		this.data.remove(item);
		this.data.add(position, item);

		this.sortItems();
	}

	private void sortItems() {

		int i = this.data.size();
		for (final T model : this.data) {
			model.setOrderColumn(i--);
		}
	}
}
