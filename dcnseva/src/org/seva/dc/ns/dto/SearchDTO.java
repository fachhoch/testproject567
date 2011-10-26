package org.seva.dc.ns.dto;

public class SearchDTO {
	
	
	private PaginationDTO  paginationDTO = new PaginationDTO();
	
	private SortDTO  sortDTO = new SortDTO();
	
	
	public PaginationDTO getPaginationDTO() {
		return paginationDTO;
	}
	public void setPaginationDTO(PaginationDTO paginationDTO) {
		this.paginationDTO = paginationDTO;
	}
	public SortDTO getSortDTO() {
		return sortDTO;
	}
	
	public void setSortDTO(SortDTO sortDTO) {
		this.sortDTO = sortDTO;
	}
	
}
