package com.gae.yotube.service;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang.StringUtils;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.Span;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.TableRow;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.appengine.repackaged.com.google.common.collect.Lists;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class HtmlReader {
	
	List<VideoDTO>  videoDTOS=Lists.newArrayList();
	public static class VideoDTO  implements  Serializable{
		public String name;
		public List<VideoSrcDTO>  videoSrcDTOs=Lists.newArrayList();
	}
	public static class VideoSrcDTO  implements  Serializable{
		public String name;
		public List<LinkDTO>  linkDTOs=Lists.newArrayList();
	}
	
	public static class LinkDTO implements  Serializable{
		public String name;
		public String url;
	}
	static  XStream xstream = new XStream(new DomDriver());
	static String url="http://www.telugunagar.com/AllVideos?CHOICE=Telugu+Movies&OPTION=New+Releases";
	
	static ExecutorService executor = Executors.newFixedThreadPool(60);
	
	public static List<VideoDTO> readTeluguNagar(String text) throws Exception{
		List<VideoDTO>  videoDTOs=Lists.newArrayList();
		 Parser  parser= new Parser(text);
		 NodeList moviesNodeList= parser.parse(new HasAttributeFilter("id", "Movies_dlMovies"));
		 NodeList tdNodeList = moviesNodeList.extractAllNodesThatMatch (new HasAttributeFilter("class","datalistnavlblstyle"),true);
		 for(NodeIterator  iterator=tdNodeList.elements() ; iterator.hasMoreNodes();){
			 VideoDTO  videoDTO= new VideoDTO();
			 Node node= iterator.nextNode().getChildren().extractAllNodesThatMatch(new TagNameFilter("span")).elementAt(0);
			 String name=((Span)node).getStringText();
			 String url="http://www.telugunagar.com/Movie.aspx?MName=[]&MSection=Telugu Movies";
			 url=StringUtils.replace(url, "[]", name.trim());
			 //System.out.println(name);
			 videoDTO.name=name.trim();
			 parseMovie(url, videoDTO);
			 videoDTOs.add(videoDTO);
		 }
		 
		 return videoDTOs;
		 
	}
	static void parseMovie(String  url, VideoDTO  videoDTO) throws Exception{
		try{	
			Parser  parser= new Parser(url);
			 NodeList gvStyleNodeList= parser.extractAllNodesThatMatch(new HasAttributeFilter("class", "gvStyle"));
			 for(NodeIterator  iterator=gvStyleNodeList.elements() ; iterator.hasMoreNodes();){
				 Node  gvStyle=iterator.nextNode();
				 NodeList trTags= gvStyle.getChildren().extractAllNodesThatMatch(new TagNameFilter("tr"));
				 for(NodeIterator  trTagIterator=trTags.elements() ; trTagIterator.hasMoreNodes();){
					 VideoSrcDTO  videoSrcDTO= new VideoSrcDTO();
					 TableRow tableRow=(TableRow)trTagIterator.nextNode();
					 NodeList videoDetailsNode= tableRow.getChildren().extractAllNodesThatMatch(new HasAttributeFilter("class","VideoDetails"), true);
					 if(videoDetailsNode.size()>0){
						 TableColumn videoDetailsColumn= (TableColumn)videoDetailsNode.elementAt(0);
						 if(videoDetailsColumn.getStringText().contains("Download"))continue;
						 //System.out.println("linkText "  +videoDetailsColumn.getStringText());
						 videoSrcDTO.name=videoDetailsColumn.getStringText();
					 }
					 NodeList anchors= tableRow.getChildren().extractAllNodesThatMatch(new TagNameFilter("a"), true);
					 for(NodeIterator  anchorIterator=anchors.elements() ; anchorIterator.hasMoreNodes();){
						 LinkDTO  linkDTO= new LinkDTO();
						 Node  node=anchorIterator.nextNode();
						 LinkTag  linkTag=(LinkTag)node;
						 if(StringUtils.isBlank(linkTag.getAttribute("onclick"))){
							// System.out.println(linkTag.getLink()+" "+linkTag.getLinkText());
							 linkDTO.name=linkTag.getLinkText();
							 System.out.println("   link "+linkTag.getLink() +"  count"+MyThreadLocal.get());
							 System.out.println("   parsing" +"  count"+MyThreadLocal.get());
							 try{
								 Parser  parser2= new Parser(linkTag.extractLink());
								 System.out.println(" done parsing"+"  count"+MyThreadLocal.get());
								 NodeList embedNodeList= parser2.extractAllNodesThatMatch(new TagNameFilter("embed"));
								 if(embedNodeList.size()>0){
									 TagNode  tagNode=(TagNode)embedNodeList.elementAt(0);
									 //System.out.println(tagNode.getAttribute("src"));
									 linkDTO.url=tagNode.getAttribute("src");
									 videoSrcDTO.linkDTOs.add(linkDTO);
								 }
							 }catch (Exception e) {
									continue;
							}	 
						 }
					 }
					 if(!videoSrcDTO.linkDTOs.isEmpty())
						 videoDTO.videoSrcDTOs.add(videoSrcDTO);
				 }
			 }
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	static void  handleHtmlForm(String url) throws Exception{
		 final WebClient webClient = new WebClient();
		 HtmlPage page = webClient.getPage(url);
		 boolean scan=true;
		 int count=0;
		 
		 while(scan){
			 String text=page.asXml();
			 //new FutureTask<Void>(new MyRunnable(text), null);
			 System.out.println("starting new thread"+count);
			 executor.execute(new MyRunnable(text, count));
			 //readTeluguNagar(text);
			 //String xml=xstream.toXML(videoDTOs);
			 //System.out.println(xml);
//			 List<String> links= Lists.newArrayList(com.google.common.collect.Iterables.transform(videoDTOs, new Function<VideoDTO, String>() {
//				 @Override
//				public String apply(VideoDTO videoDTO) {
//					 String links=com.google.common.base.Joiner.on(",").join(com.google.common.collect.Iterables.transform(videoDTO.videoSrcDTOs, new Function<VideoSrcDTO, String>() {
//						 @Override
//						public String apply(VideoSrcDTO videoSrcDTO) {
//							 String srcLinks=Joiner.on("|").join(com.google.common.collect.Iterables.transform(videoSrcDTO.linkDTOs, new Function<LinkDTO, String>() {
//								 @Override
//								public String apply(LinkDTO linkDTO) {
//									return linkDTO.name +"-"+linkDTO.url;
//								}
//							}));
//							 String value="{0}[{1}]";
//							return MessageFormat.format(value, videoSrcDTO.name,srcLinks);
//						}
//					}));
//					String value="title[{0}],links[{1}]";
//					return MessageFormat.format(value, videoDTO.name,links);
//				}
//			}));
//			 for(String link :links){
//				 System.out.println(link);
//			 }
//			 System.out.println("writing to file");
//			 org.apache.commons.io.FileUtils.writeLines(file,Lists.newArrayList(xstream.toXML(videoDTOs)));
//			 System.out.println("done writing");
//			 System.out.println("end&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
			 
			 HtmlAnchor htmlAnchor= (HtmlAnchor)page.getElementById("Movies_lnkbtnNext1");
			 if(htmlAnchor!=null){
				 String disabled=htmlAnchor.getAttribute("disabled");
				 if(StringUtils.isBlank(disabled))
					 page=htmlAnchor.click();
				 else scan=false;
			 }
			 count++;
			 //scan=false;
		 }
		 executor.shutdown();
		 System.out.println("waiting for all threads");
		 while (!executor.isTerminated()){}
		 
		 webClient.closeAllWindows();
	}
	
	
	private static class MyRunnable  implements  Runnable{
		String text;
		int count;
		public MyRunnable(String text , int count) {
			this.text=text;
			this.count=count;
		}
		@Override
		public void run() {
			try{
				MyThreadLocal.set(count);
				List<VideoDTO> videoDTOs= readTeluguNagar(text);
				String xml=xstream.toXML(videoDTOs);
				org.apache.commons.io.FileUtils.writeLines(new File("c://dev//links//telugu-nagar-"+String.valueOf(count)+".xml"),Lists.newArrayList(xml));
			}catch (Exception e) {
				System.out.println("failed "+count+e.getMessage());
			}
		}
	}
	
	
	public static void main(String s[]) throws Exception{
		System.setProperty ("sun.net.client.defaultReadTimeout", "70000");
		System.setProperty ("sun.net.client.defaultConnectTimeout", "70000");
		 xstream.alias("videodto", VideoDTO.class);
		 xstream.alias("videoSrcDTO", VideoSrcDTO.class);
		 xstream.alias("vinkDTO", LinkDTO.class);
		
		HtmlReader  htmlReader= new HtmlReader();
//		htmlReader.readTeluguNagar(url);
		//htmlReader.parseMovie("http://www.telugunagar.com/Movie.aspx?MName=Oosaravelli-Movie-2011&MSection=Telugu Movies");
		String  urls[]={"http://www.telugunagar.com/AllVideos?CHOICE=Telugu+Movies&OPTION=New+Releases",
				"http://www.telugunagar.com/AllVideos?CHOICE=Telugu Movies&amp;OPTION=Recently Added"};
		htmlReader.handleHtmlForm(url);
	}
	
	public static class MyThreadLocal {

		   private static ThreadLocal<Integer> tLocal = new ThreadLocal<Integer>();

		  public static void set(Integer  count) {
		    tLocal.set(count);
		  }

		  public static Integer get() {
		    return tLocal.get();
		  }
	}
	
}
