package com.gae.yotube.service;

import java.util.List;

import com.gae.yotube.service.model.PaginationDTO;
import com.gae.yotube.service.model.SearchDTO;
import com.gae.yotube.service.model.Video;
import com.google.appengine.repackaged.com.google.common.base.Function;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;

public class DefaultVideoService implements VideoService {
	
	
	public DefaultVideoService() {
		ObjectifyService.register(Video.class);
	}
	
	@Override
	public Video addVideo(Video video) {
		Objectify ofy = ObjectifyService.begin();
		ofy.put(video);		
		return video;
	}

	@Override
	public List<Video> getVideos(PaginationDTO  paginationDTO) {
		Objectify ofy = ObjectifyService.begin();
		Query<Video>  query=ofy.query(Video.class);
		query.offset(paginationDTO.getFirst());
		query.limit(paginationDTO.getCount());
		return query.list();
	}
	
	@Override
	public int getTotalVidoes(SearchDTO  searchDTO) {
		return new Search(searchDTO){
			@Override
			void applyPagination() {
			}
		}.query.count();
	}
	
	@Override
	public List<Video> getVideos(SearchDTO searchDTO) {
		return new Search(searchDTO).query.list();
	}
	
	private static  class Search{
		SearchDTO  searchDTO;
		Query<Video>  query;
		public Search(SearchDTO  searchDTO) {
			this.searchDTO=searchDTO;
			Objectify ofy = ObjectifyService.begin();
			this.query=ofy.query(Video.class);
			applyFilters();
			applyPagination();
		}
		void applyFilters(){
			if(searchDTO.getName()!=null){
				query.filter("title >=", searchDTO.getName()); 
				query.filter("title <", searchDTO.getName() + "\uFFFD"); 
			}
		}
		void applyPagination(){
			query.offset(searchDTO.getPaginationDTO().getFirst());
			query.limit(searchDTO.getPaginationDTO().getCount());
		}
	}
	
}
