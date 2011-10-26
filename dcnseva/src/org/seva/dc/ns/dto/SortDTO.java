package org.seva.dc.ns.dto;

import java.io.Serializable;

public class SortDTO  implements  Serializable {
	
	
	private boolean asc;
	
	private String column;
	
	private String defaultSortCol;

	public boolean isAsc() {
		return asc;
	}

	public void setAsc(boolean asc) {
		this.asc = asc;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}
	
	public String getDefaultSortCol() {
		return defaultSortCol;
	}
	public void setDefaultSortCol(String defaultSortCol) {
		this.defaultSortCol = defaultSortCol;
	}
}
