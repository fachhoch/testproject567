package com.saibaba.links;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;

public class Mirchi9Reader {
	
	private Map<String, String>  links= new HashMap<String, String>();
	private final Log log = LogFactory.getLog(getClass()); 
	
	Map<String, String>  getLinks(){
		return links;
	}

	public Mirchi9Reader() {
		System.setProperty ("sun.net.client.defaultReadTimeout", "70000");
		System.setProperty ("sun.net.client.defaultConnectTimeout", "70000");
		try{
			readMirci9("http://www.mirchi9.com/cinemas/");
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	private void  readMirci9(String url) throws Exception{
		System.out.println("url " + url);
		log.debug(" url "+url);
		NodeList  mainContentDivs =parse(url);
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
			log.debug(linkTag.getLinkText()+" "+videolink);
			links.put(linkTag.getLinkText(),videolink);
		}
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
			readMirci9(tag.extractLink());
		}
	}
	 private String  getVideoLink(String url) throws Exception{
		Parser  parser= new Parser(url);
		NodeList  nodeList= parser.parse(new TagNameFilter("embed"));
		for(NodeIterator  iterator=nodeList.elements() ; iterator.hasMoreNodes();){
			Node node=iterator.nextNode();	
			Tag tag=(Tag)node;
			return tag.getAttribute("src");
		}
		return url+"-"+"*********************";
	}
	
	
	 private NodeList parse(String url) throws Exception{
		Parser  parser= new Parser(url);
		NodeList  nodeList =parser.parse(new HasAttributeFilter("id","MainContent"));
		return nodeList;
	 }
	
	
	
}
