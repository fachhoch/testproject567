package com.gae.yotube;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;

import com.gae.yotube.service.HtmlReader;
import com.gae.yotube.service.HtmlReader.LinkDTO;
import com.gae.yotube.service.HtmlReader.VideoDTO;
import com.gae.yotube.service.HtmlReader.VideoSrcDTO;
import com.gae.yotube.service.model.Video;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.repackaged.com.google.common.collect.Lists;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class ReadTeluguNagarServlet extends HttpServlet {

	private static final Logger log = Logger.getLogger(ReadTeluguNagarServlet.class
			.getName());

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		try {
			
	       	 XStream xstream = new XStream(new DomDriver());
			 xstream.alias("videodto", VideoDTO.class);
			 xstream.alias("videoSrcDTO", VideoSrcDTO.class);
			 xstream.alias("vinkDTO", LinkDTO.class);

			final WebClient webClient = new WebClient();
			 HtmlPage page = webClient.getPage("http://www.telugunagar.com/AllVideos?CHOICE=Telugu+Movies&OPTION=New+Releases");
			 String text=page.asXml();
			 
			 List<VideoDTO> videoDTOs= HtmlReader.readTeluguNagar(text);
			 webClient.closeAllWindows();
			 for(VideoDTO  videoDTO  :videoDTOs){
        		Video  video=new Video();
        		video.setTitle(videoDTO.name);
        		if(videoDTOs.isEmpty())continue;
        		String links=xstream.toXML(videoDTO.videoSrcDTOs);
        		video.setLink(links);
        		Queue queue = QueueFactory.getQueue("subscription-queue");
        		TaskOptions taskOptions= TaskOptions.Builder.withUrl("/gaejsignupsubscriber");
        		taskOptions.param(GAEJSignupSubscriberServlet.PARAM_REQ_HANDLER, GAEJSignupSubscriberServlet.RequestHandler.VALIDATE.toString());
        		taskOptions.param(GAEJSignupSubscriberServlet.PARAM_VIDEOS, 
        				Base64.encodeBase64(GAEJSignupSubscriberServlet.toByteArray(Lists.newArrayList(video))));
        		queue.add(taskOptions);
        	}
			 
		} catch (Exception ex) {
			log.log(Level.SEVERE, "Uncaught exception", ex);
		}
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

}
