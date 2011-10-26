package org.seva.dc.ns.dto;

import java.io.Serializable;

public class PaginationDTO implements Serializable {
	
private int first;
	
	private int count;
	
	
	public int getCount() {
		return count;
	}
	
	public void setCount(int count) {
		this.count = count;
	}
	
	public int getFirst() {
		return first;
	}
	
	public void setFirst(int first) {
		this.first = first;
	}

	
	
	
}
