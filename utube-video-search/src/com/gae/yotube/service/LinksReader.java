package com.gae.yotube.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;

import com.gae.yotube.EmbedTag;
import com.google.appengine.repackaged.com.google.common.base.Joiner;
import com.google.appengine.repackaged.com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class LinksReader {
	
	private static final Logger log = Logger.getLogger(LinksReader.class.getName()); 

	public LinksReader() {
		System.setProperty ("sun.net.client.defaultReadTimeout", "70000");
		System.setProperty ("sun.net.client.defaultConnectTimeout", "70000");
	}
	
	
	public void readTeluguLinks() throws Exception{
		readTeluguFreeLinks("http://telugufreelinks.blogspot.com/search/label/telugu%20movies?max-results=100");
	}
	
	public LinksDTO  readMirci9(String url) throws Exception{
		log.info(" url "+url);
		LinksDTO  linksDTO= new LinksDTO();
		NodeList  mainContentDivs =parse(url);

		NodeList  navigationList=mainContentDivs.extractAllNodesThatMatch(new HasAttributeFilter("class", "navigation"), true);
		NodeList previousLinkNode= navigationList.extractAllNodesThatMatch(new NodeFilter() {
			public boolean accept(Node node) {
				//System.out.println(node.getClass());
				if(!(node instanceof  LinkTag))return false;
				LinkTag  linkTag=(LinkTag)node;
				return linkTag.getLinkText().indexOf("Previous ")!=-1;
			}
		}, true);
		for(NodeIterator  iterator=previousLinkNode.elements() ; iterator.hasMoreNodes();){
			Node node=iterator.nextNode();	
			LinkTag tag=(LinkTag)node;
			//readMirci9(tag.extractLink());
			linksDTO.nextLink=tag.extractLink();
		}

		try{
			NodeList  postThumbnailDiv=mainContentDivs.extractAllNodesThatMatch(new HasAttributeFilter("class","Post thumbnailview"), true);
			NodeList  anchorTags=postThumbnailDiv.extractAllNodesThatMatch(new NodeFilter() {
				public boolean accept(Node node) {
					if(node instanceof LinkTag){
						LinkTag  linkTag=(LinkTag)node;
						String id=linkTag.getAttribute("id");
						return id==null ?  false: id.indexOf("textLink")!=-1;
					}
					return false;
				}
			}, true);
			for(Node node   :anchorTags.toNodeArray()){
				LinkTag  linkTag =(LinkTag)  node;
				//System.out.println(linkTag.getLinkText()+"  "+);
				String videolink=getVideoLink(linkTag.extractLink());
				if(videolink!=null){
					linksDTO.links.put(linkTag.getLinkText(),videolink);
				}
				//log.info(linkTag.getLinkText()+" "+videolink);
			}
		}catch (Exception e) {
			log.log(Level.SEVERE,"failed", e);
			log.log(Level.SEVERE,"ignore exception and continue");
			
		}	
		return linksDTO;
	}
	 private String  getVideoLink(String url) {
		 try{
			Parser  parser= new Parser(url);
			NodeList  nodeList= parser.parse(new TagNameFilter("embed"));
			for(NodeIterator  iterator=nodeList.elements() ; iterator.hasMoreNodes();){
				Node node=iterator.nextNode();	
				Tag tag=(Tag)node;
				return tag.getAttribute("src");
			}
		 }catch (Exception e) {
				log.log(Level.SEVERE,"failed", e);
				log.log(Level.SEVERE,"ignore exception and continue");
		}	
		return null;
	}
	
	 public  static  class LinksDTO implements  Serializable {
		 Map<String, String>  links= Maps.newHashMap();
		 String nextLink;
		 public Map<String, String> getLinks() {
			return links;
		}
		 
		 public String getNextLink() {
			return nextLink;
		}
	 }
	 
	 
	
	 private NodeList parse(String url) throws Exception{
		Parser  parser= new Parser(url);
		NodeList  nodeList =parser.parse(new HasAttributeFilter("id","MainContent"));
		return nodeList;
	 }
	
	 public  LinksDTO readTeluguFreeLinks(String url)throws Exception{
		 //log.log(Level.INFO,"|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
		 LinksDTO  linksDTO= new LinksDTO();
		 
		 Parser  parser= new Parser(url);
		 NodeList pageNodeList= parser.parse(new HasAttributeFilter("class", "widget Blog"));
		 NodeList blogPostNodeList= pageNodeList.extractAllNodesThatMatch(new HasAttributeFilter("class", "blog-posts hfeed"), true);
		 NodeList  h3NodeLists=blogPostNodeList.extractAllNodesThatMatch(new HasAttributeFilter("class", "new"), true);
		 
		 for(NodeIterator  iterator=h3NodeLists.elements() ; iterator.hasMoreNodes();){
			Node node=iterator.nextNode();	
			LinkTag  linkTag=(LinkTag)node.getChildren().elementAt(0);
			//log.info("name "+linkTag.getLinkText());
			try{
				NodeList videoLinkNodeList= new Parser(linkTag.extractLink())
				.parse(new HasAttributeFilter("class", "post-body entry-content"))
				.extractAllNodesThatMatch(new NodeFilter() {
					@Override
					public boolean accept(Node node) {
						if(node  instanceof  LinkTag){
							LinkTag  linkTag=(LinkTag)node;
							return StringUtils.contains(linkTag.extractLink(), "http://telugufreelinks.ulmb.com/");
							//return StringUtils.contains(linkTag.extractLink(), "telugufreelinks.ulmb.com/");
						}
						return false;
					}
				}, true);
				List links= teluguFreeLinksVideoLink(videoLinkNodeList);
				if(!links.isEmpty()){
					linksDTO.links.put(linkTag.getLinkText(), Joiner.on(',').join(links));
				}
			}catch (Exception e) {
				log.log(Level.SEVERE,"uncaught",e);
				continue;
			}
		 }
		 //NodeList blogPager= pageNodeList.extractAllNodesThatMatch(new HasAttributeFilter("id", "blog-pager"), true);
		 NodeList  olderNodeList=pageNodeList.extractAllNodesThatMatch(new HasAttributeFilter("id","Blog11_blog-pager-older-link"), true);
		 for(NodeIterator  iterator=olderNodeList.elements() ; iterator.hasMoreNodes();){
				Node node=iterator.nextNode();	
				LinkTag  linkTag=(LinkTag)node;
				//readTeluguFreeLinks(linkTag.getLink());
				linksDTO.nextLink=linkTag.getLink();
				//log.log(Level.INFO,linksDTO.nextLink);
		 }
		 //log.log(Level.INFO,"**************************************************************************************************");
		 return linksDTO;
	 }

	 private List teluguFreeLinksVideoLink(NodeList  srcNodeList)  throws Exception{
		List<String>  links= new ArrayList<String>();
		for(NodeIterator  iterator=srcNodeList.elements() ; iterator.hasMoreNodes();){
				Node node=iterator.nextNode();	
				LinkTag  linkTag=(LinkTag)node;
				String url=StringUtils.remove(linkTag.getLink(), "amp;");
				String videoLink=getVideoLink(url);
				if(videoLink!=null){
					links.add(videoLink);	
				}
		 }
		 return links;
	}
	 public  List<String> readNextLinks(String url)throws Exception{
		 List<String>  urls= Lists.newArrayList();
		 Parser  parser= new Parser(url);
		 NodeList pageNodeList= parser.parse(new HasAttributeFilter("class", "widget Blog"));
		 NodeList  olderNodeList=pageNodeList.extractAllNodesThatMatch(new HasAttributeFilter("id","Blog11_blog-pager-older-link"), true);
		 for(NodeIterator  iterator=olderNodeList.elements() ; iterator.hasMoreNodes();){
				Node node=iterator.nextNode();	
				LinkTag  linkTag=(LinkTag)node;
				//readTeluguFreeLinks(linkTag.getLink());
				//linksDTO.nextLink=linkTag.getLink();
				//log.log(Level.INFO,linkTag.getLink());
				urls.add(linkTag.getLink());
				readNextLinks(linkTag.getLink());
		 }
		 return urls;

	 }
	
	 
	 public static class NextLinksGrabber  {
		 List<String>  nextLinks= Lists.newArrayList();
		public void read(String url) throws Exception{
			 Parser  parser= new Parser(url);
			 NodeList pageNodeList= parser.parse(new HasAttributeFilter("class", "widget Blog"));
			 NodeList  olderNodeList=pageNodeList.extractAllNodesThatMatch(new HasAttributeFilter("id","Blog11_blog-pager-older-link"), true);
			 for(NodeIterator  iterator=olderNodeList.elements() ; iterator.hasMoreNodes();){
					Node node=iterator.nextNode();	
					LinkTag  linkTag=(LinkTag)node;
					//log.log(Level.INFO,linkTag.getLink());
					nextLinks.add(linkTag.getLink());
					read(linkTag.getLink());
			 }
		 }
		public List<String>  getNextLinks(){
			 return nextLinks;
		 }
	 }
	 
	 public  LinksDTO readBharath(String url) throws Exception{
		 LinksDTO  linksDTO= new LinksDTO();
		 Parser  parser= new Parser(url);
		 NodeList divNodes= parser.parse(new HasAttributeFilter("id", "cmain")).extractAllNodesThatMatch(new NodeFilter() {
			@Override
			public boolean accept(Node node) {
				if(node instanceof Div){
					String id=((Div)node).getAttribute("id");
					return id!=null&&(id.equals("L1")||id.equals("L2"));
				}
				return false;
			}
		}, true);
		for(NodeIterator  divNodesIterator=divNodes.elements() ; divNodesIterator.hasMoreNodes();) {
			Node divNode=divNodesIterator.nextNode();	
			NodeList list = new NodeList ();
			NodeFilter filter = new TagNameFilter ("A");
			for (NodeIterator e = divNode.getChildren().elements(); e.hasMoreNodes ();)
			      e.nextNode ().collectInto (list, filter);
			for(NodeIterator  aNodesIterator=list.elements() ; aNodesIterator.hasMoreNodes();) {
				LinkTag  linkTag=(LinkTag)aNodesIterator.nextNode();
				linksDTO.links.put(linkTag.getLinkText(), parseBharathLink(linkTag.getLink()));
			}
		}
		 return linksDTO;
	 }
	 
	 private String  parseBharathLink(String url) throws Exception{
		 List<String>  links= Lists.newArrayList();
		 NodeList embedNodeList= new Parser(url).parse(new HasAttributeFilter("id", "cmain")).extractAllNodesThatMatch(new TagNameFilter("embed"), true);
			for(NodeIterator  embedNodeListIterator=embedNodeList.elements() ; embedNodeListIterator.hasMoreNodes();) {
				TagNode  tagNode=(TagNode)embedNodeListIterator.nextNode();
				links.add(tagNode.getAttribute("src"));
			}
		 return Joiner.on(",").join(links);
	 }
}
