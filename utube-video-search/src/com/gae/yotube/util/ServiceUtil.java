package com.gae.yotube.util;

import com.gae.yotube.service.DefaultVideoService;
import com.gae.yotube.service.VideoService;
import com.gae.yotube.xml.GroovyXml;

public class ServiceUtil {
	
	public static final VideoService  VIDEO_SERVICE=new DefaultVideoService();
	public static final  GroovyXml  GROOVY_XML= new GroovyXml();
	
	
}
