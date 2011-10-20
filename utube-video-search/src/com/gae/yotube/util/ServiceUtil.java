package com.gae.yotube.util;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gae.yotube.service.DefaultVideoService;
import com.gae.yotube.service.HtmlReader;
import com.gae.yotube.service.HtmlReader.LinkDTO;
import com.gae.yotube.service.HtmlReader.VideoSrcDTO;
import com.gae.yotube.service.VideoService;
import com.gae.yotube.xml.GroovyXml;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class ServiceUtil {
	
	public static final VideoService  VIDEO_SERVICE=new DefaultVideoService();
	public static final  GroovyXml  GROOVY_XML= new GroovyXml();
	
	public static 	 XStream xstream = new XStream(new DomDriver());
	static{
		 xstream.alias("videoSrcDTO", VideoSrcDTO.class);
		 xstream.alias("vinkDTO", LinkDTO.class);
	}
	
	
	public static List<VideoSrcDTO>  fromXml(String xml){
		return (List<HtmlReader.VideoSrcDTO>)xstream.fromXML(xml);
	}
	
	public static boolean isXMLLike(String inXMLStr) {

        boolean retBool = false;
        Pattern pattern;
        Matcher matcher;

        // REGULAR EXPRESSION TO SEE IF IT AT LEAST STARTS AND ENDS
        // WITH THE SAME ELEMENT
        final String XML_PATTERN_STR = "<(\\S+?)(.*?)>(.*?)</\\1>";

        // IF WE HAVE A STRING
        if (inXMLStr != null && inXMLStr.trim().length() > 0) {

            // IF WE EVEN RESEMBLE XML
            if (inXMLStr.trim().startsWith("<")) {

                pattern = Pattern.compile(XML_PATTERN_STR,
                Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);

                // RETURN TRUE IF IT HAS PASSED BOTH TESTS
                matcher = pattern.matcher(inXMLStr);
                retBool = matcher.matches();
            }
        // ELSE WE ARE FALSE
        }

        return retBool;
    }
}
