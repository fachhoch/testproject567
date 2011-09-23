package com.gae.utube.test;

import java.awt.print.Printable;

import com.gae.yotube.service.YouTubeManager
import com.gae.yotube.service.model.Video;
import com.gae.yotube.service.model.YouTubeMedia;
import com.gae.yotube.service.model.YouTubeVideo;
import com.google.appengine.repackaged.com.google.common.collect.Iterables;
import com.google.appengine.repackaged.com.google.common.collect.Lists;
import com.google.gdata.client.YouTubeReader;

public class VideoHelper {
	
		public static List<Video>  getVideos(int nofOfTimes){
			List<Video>  videos= Lists.newArrayList();
			YouTubeReader.getResults(nofOfTimes).each {
				def feeds=new XmlSlurper().parseText(it);
				def entry=feeds.entry.findAll{it.group.duration.@seconds.toInteger()>3600}
				entry.collect {
						Video  video= new Video();
						video.setLink (it.group.player.@url.toString());
						video.setTitle (it.group.title.toString());
						videos.add(video);
				}
			}
			return videos;
		}
}
