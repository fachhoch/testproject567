package com.gae.yotube.service.model;

import java.io.Serializable;

public class SearchDTO implements Serializable {
	
	
	private String name;
	
	private PaginationDTO  paginationDTO ;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public PaginationDTO getPaginationDTO() {
		return paginationDTO;
	}

	public void setPaginationDTO(PaginationDTO paginationDTO) {
		this.paginationDTO = paginationDTO;
	}
	
	
	
}
