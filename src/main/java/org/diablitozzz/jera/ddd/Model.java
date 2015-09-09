package org.diablitozzz.jera.ddd;

abstract public class Model {

	@Override
	public boolean equals(final Object object) {

		if (object == null) {
			return false;
		}
		//сравнение на классы - для наследования
		if (!object.getClass().equals(this.getClass())) {
			return false;
		}
		final Model item = (Model) object;
		//persist persist
		if (Check.isNotEmpty(item.getIdAsObject()) && Check.isNotEmpty(this.getIdAsObject())) {
			return Check.isEquals(item.getIdAsObject(), this.getIdAsObject());
		}
		//persist not-perist
		if (Check.isEmpty(item.getIdAsObject()) && Check.isNotEmpty(this.getIdAsObject())) {
			return false;
		}
		//persist not-perist
		if (Check.isNotEmpty(item.getIdAsObject()) && Check.isEmpty(this.getIdAsObject())) {
			return false;
		}
		//not-persist not-perist
		return super.equals(item);
	}

	protected abstract Object getIdAsObject();

	@Override
	public int hashCode() {

		final Object id = this.getIdAsObject();
		if (id == null) {
			return super.hashCode();
		}
		return id.hashCode();
	}

	@Override
	public String toString() {

		final StringBuilder builder = new StringBuilder();
		builder.append("class: ");
		builder.append(this.getClass().getName());
		builder.append(" id: ");
		builder.append(this.getIdAsObject());
		return builder.toString();
	}

}
