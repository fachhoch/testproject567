package com.gae.yotube.service;

import java.net.URL;
import java.util.List;

import com.google.gdata.client.youtube.YouTubeQuery;
import com.google.gdata.client.youtube.YouTubeService;
import com.google.gdata.data.youtube.VideoFeed;

public class YoutTubeVideoSearchService implements VideoSearchService {

	YouTubeService  youTubeService= new YouTubeService("saibaba");
	
	@Override
	public List<String> findVideos() throws Exception{
		YouTubeQuery query = new YouTubeQuery(new URL("http://gdata.youtube.com/feeds/api/videos"));
		query.setOrderBy(YouTubeQuery.OrderBy.VIEW_COUNT);
		query.setFullTextQuery("puppy");
		query.setSafeSearch(YouTubeQuery.SafeSearch.NONE);
		VideoFeed videoFeed = youTubeService.query(query, VideoFeed.class);
		return null;
		
	
	}
	
}
