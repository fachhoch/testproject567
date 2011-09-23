package com.google.gdata.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang.StringUtils;

import com.gae.utube.test.VideoHelper;
import com.gae.yotube.service.YouTubeManager;
import com.gae.yotube.service.model.Video;
import com.gae.yotube.util.ServiceUtil;
import com.gae.yotube.xml.GroovyXml;
import com.google.appengine.repackaged.com.google.common.collect.Multiset.Entry;
import com.google.gdata.data.ParseSource;
import com.google.gdata.data.media.GDataContentHandler;
import com.google.gdata.data.media.mediarss.MediaGroup;
import com.google.gdata.data.youtube.VideoEntry;
import com.google.gdata.data.youtube.VideoFeed;
import com.google.gdata.util.ContentType;
import com.google.gdata.util.ServiceException;
import com.google.gdata.wireformats.AltRegistry;
import com.google.gdata.wireformats.input.AtomDataParser;

public class YouTubeTester {
	
	public static void main(String[] args) throws Exception {
		
//		String clientID = "JavaCodeGeeks";
//		String textQuery = "telugu movies";
//		int maxResults = 10;
//		boolean filter = true;
//		int timeout = 2000;
//		
//		YouTubeManager ym = new YouTubeManager(clientID);
//		
//		//List<YouTubeVideo> videos = ym.retrieveVideos(textQuery, maxResults, filter, timeout);
//		
//		InputStream  inputStream=new FileInputStream(new File("e://dev/gdata/sample-response.xml"));
//		AltRegistry  altRegistry= new AltRegistry();
//		VideoFeed videoFeed = new AtomDataParser().parse(new ParseSource(inputStream), GDataContentHandler.getThreadInputProperties(), VideoFeed.class);
//		System.out.println(videoFeed);
//		
//		for(VideoEntry  videoEntry  :videoFeed.getEntries()){
//			MediaGroup mediaGroup= videoEntry.getMediaGroup();
//			System.out.print(mediaGroup.getTitle());
//			System.out.print(mediaGroup.getPlayer().getUrl());
//			System.out.println(mediaGroup);
//		}
		
//		for (com.google.gdata.data.Entry  entry : videoFeed.getEntries()) {
//			System.out.println(youtubeVideo.getE.getWebPlayerUrl());
//			System.out.println("Thumbnails");
//			for (String thumbnail : youtubeVideo.getThumbnails()) {
//				System.out.println("\t" + thumbnail);
//			}
//			System.out.println(youtubeVideo.getEmbeddedWebPlayerUrl());
//			System.out.println("**************************************************");
//		}
//		
		
//		for(Video video :VideoHelper.getVideos() ){
//			//ServiceUtil.VIDEO_SERVICE.addVideo(video);
//			System.out.println(video);
//		}

	}

	
}
