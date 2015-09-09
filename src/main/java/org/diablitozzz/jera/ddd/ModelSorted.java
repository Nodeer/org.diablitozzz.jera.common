package org.diablitozzz.jera.ddd;

abstract public class ModelSorted extends Model {

	abstract public Long getOrderColumn();

	abstract protected void setOrderColumn(long orderColumn);
}
