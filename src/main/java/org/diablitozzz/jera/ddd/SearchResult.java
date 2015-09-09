package org.diablitozzz.jera.ddd;

import java.util.List;

public class SearchResult<T extends Model> {

	public List<T> items;
	public int totalCount;

}
