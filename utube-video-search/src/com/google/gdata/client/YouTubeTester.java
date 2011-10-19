package com.google.gdata.client;

import java.io.File;
import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import com.gae.yotube.service.LinksReader;
import com.gae.yotube.service.LinksReader.LinksDTO;
import com.google.common.collect.Lists;

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
		
		
		//String url=parameters.get(PARAM_URL);
		System.setProperty ("sun.net.client.defaultReadTimeout", "70000");
		System.setProperty ("sun.net.client.defaultConnectTimeout", "70000");
		final String url="http://telugufreelinks.blogspot.com/search/label/telugu%20movies?max-results=100";
		LinksReader  linksReader= new LinksReader();
		LinksDTO linksDTO= linksReader.readBharath("http://www.bharatmovies.com/telugu/watch/telugu-movies-new.htm");
		List<String>  lines=Lists.newArrayList();
		for(String key :linksDTO.getLinks().keySet()){
			String line="title[{0}],links[{1}]";
			line=MessageFormat.format(line, key,linksDTO.getLinks().get(key));
			//System.out.println(line+" "+name);
			lines.add(line);
			org.apache.commons.io.FileUtils.writeLines(new File("c://dev//links/"+"new-additions"+".txt"), lines);			
		}
//		Callable<List<String>> callable= new Callable<List<String>>() {
//			@Override
//			public List<String> call() throws Exception {
//				LinksReader.NextLinksGrabber  linksReader= new LinksReader.NextLinksGrabber();
//				linksReader.read(url);
//				return linksReader.getNextLinks();
//			}
//		};
//		Future<List<String>> future= executor.submit(callable);
//		List<String> links=future.get() ;
//		int count=1;
//		for(String  link :links  ){
//			executor.execute(new MyRunnable(link,String.valueOf(count)));
//			count++;
//		}
//		executor.shutdown();
//		while (!executor.isTerminated()) {
//			
//		}
//		System.out.println("Finished all threads");
//	}
//	
//	static ExecutorService executor = Executors.newFixedThreadPool(200);
//	
//	public static  class MyRunnable implements Runnable {
//		String url;
//		String name;
//		public MyRunnable(String url,final  String name ) {
//			this.url=url;
//			this.name=name;
//		}
//		@Override
//		public void run() {
//			try{
//				LinksReader  linksReader=new LinksReader();
//				System.out.println("start reading "+name+"  "+url);
//				LinksDTO linksDTO= linksReader.readTeluguFreeLinks(url);
//				System.out.println("finish reading "+name);
//
//				List<String>  lines=Lists.newArrayList();
//				for(String key:linksDTO.getLinks().keySet()){
//					String line="title[{0}],links[{1}]";
//					line=MessageFormat.format(line, key,linksDTO.getLinks().get(key));
//					System.out.println(line+" "+name);
//					lines.add(line);
//				}
//				if(lines.isEmpty())return;
//				org.apache.commons.io.FileUtils.writeLines(new File("c://dev//links/"+name+".txt"), lines);
//			}catch (Exception e) {
//				System.out.println(e);
//			}
//		}
	}
}
