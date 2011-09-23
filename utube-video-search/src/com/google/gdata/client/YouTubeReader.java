package com.google.gdata.client;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.retep.nosockHttpClient.GAEConnectionManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jdom.xpath.XPath;
import org.xml.sax.InputSource;

import com.google.appengine.repackaged.com.google.common.collect.Lists;

public class YouTubeReader {
	
	public static String getResponse(String startIndex) throws Exception {
		
		HttpClient httpclient = new DefaultHttpClient(new GAEConnectionManager());
		//HttpClient httpclient = new DefaultHttpClient();
		List<NameValuePair> qparams = new ArrayList<NameValuePair>();
		qparams.add(new BasicNameValuePair("q", "telugu movies"));
		qparams.add(new BasicNameValuePair("fields", "entry(media:group(media:title,media:player,yt:duration))"));
		qparams.add(new BasicNameValuePair("max-results", "50"));
		qparams.add(new BasicNameValuePair("start-index", startIndex));
		
		URI uri = URIUtils.createURI("http", "gdata.youtube.com", -1, "feeds/api/videos", URLEncodedUtils.format(qparams, "UTF-8"), null);
		HttpGet httpget = new HttpGet(uri);
		//System.out.println(httpget.getURI());
		HttpResponse response = httpclient.execute(httpget);
		HttpEntity entity = response.getEntity();
		String value=EntityUtils.toString(entity);
		//System.out.println(value);
		return value;//StringUtils.removeStart(value, "<?xml version='1.0' encoding='UTF-8'?>");
	}
	
	
	
	public static List<String> getResults(int noOfTimes) throws Exception{
		List<String>  results= Lists.newArrayList();
		int count=0;
		while(count<noOfTimes){
			int startIndex=(50*count)+1;
			results.add(getResponse(String.valueOf(startIndex)));
			count++;
		}
		return results;
	}
	
	
	
	public static void parser(String xml)  throws Exception{
//		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//		factory.setNamespaceAware(true); // never forget this!
//		DocumentBuilder builder = factory.newDocumentBuilder();
//		Document doc = builder.parse(new InputSource(new ByteArrayInputStream(xml.getBytes())));
		
		org.jdom.input.SAXBuilder saxBuilder = new org.jdom.input.SAXBuilder();
		org.jdom.Document jdomDocument =saxBuilder.build(new InputSource(new ByteArrayInputStream(xml.getBytes())));
		XPath  xPath=XPath.newInstance("/feed/entry/media:group");
		xPath.addNamespace("media", "http://search.yahoo.com/mrss/");
		xPath.addNamespace("yt", "http://gdata.youtube.com/schemas/2007");
		xPath.addNamespace("feed", "http://www.w3.org/2005/Atom");
		
		java.util.List nodeList =xPath.selectNodes(jdomDocument);
		
		Iterator iter=nodeList.iterator();
		while(iter.hasNext()) {
		    org.jdom.Element element = (org.jdom.Element) iter.next();
		    //element.setAttribute("section", "Java Technology");
		    System.out.println(element);
		}
		//Document doc = builder.parse(new File("e://dev//gdata/sample.xml"));
//		XPathFactory xPathFactory= XPathFactory.newInstance();
//		XPath xpath = xPathFactory.newXPath();
//		XPathExpression expr = xpath.compile("/feed/entry");
//		Object result = expr.evaluate(doc, XPathConstants.NODESET);
//		System.out.println(result);
//		NodeList nodes = (NodeList) result;
//	    for (int i = 0; i < nodes.getLength(); i++) {
//	        System.out.println(nodes.item(i).getNodeValue()); 
//	    }		
	}
	public static void main (String s []) throws Exception{
//		String xml=getResponse();
//		parser(xml);
	}
}
