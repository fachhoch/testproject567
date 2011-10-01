package org.sairam.ns.service.dto;

import java.io.Serializable;

public class PaginationDTO implements Serializable {
	
	int offset;
	
	int limit;

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	
	
	
}
