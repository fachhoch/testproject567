package com.gae.yotube.service;

import java.util.List;

import com.gae.yotube.service.model.PaginationDTO;
import com.gae.yotube.service.model.SearchDTO;
import com.gae.yotube.service.model.Video;

public interface VideoService {
	
	
	Video  addVideo(Video  video);
	
	List<Video>  getVideos(PaginationDTO  paginationDTO);

	List<Video>  getVideos(SearchDTO  searchDTO);

	int getTotalVidoes(SearchDTO searchDTO);
}
