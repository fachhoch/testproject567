package com.gae.yotube.service.model;

import java.io.Serializable;

public class PaginationDTO implements Serializable {
	
	
	private int count;
	
	private int first;
	
	public PaginationDTO(int count, int first) {
		this.count=count;
		this.first=first;
	}
	
	
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
