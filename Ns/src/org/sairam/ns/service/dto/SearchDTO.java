package org.sairam.ns.service.dto;

public class SearchDTO {
	
	
	private PaginationDTO  paginationDTO = new PaginationDTO();
	
	public PaginationDTO getPaginationDTO() {
		return paginationDTO;
	}
	public void setPaginationDTO(PaginationDTO paginationDTO) {
		this.paginationDTO = paginationDTO;
	}
}
