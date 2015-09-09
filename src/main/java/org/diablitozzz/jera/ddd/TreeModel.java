package org.diablitozzz.jera.ddd;

abstract public class TreeModel<T> extends Model {

	abstract public ModelTreeCollection<?> getChildren();

}
