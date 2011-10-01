package com.gae.yotube;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

public class GAEJCronServlet extends HttpServlet {

	private static final Logger log = Logger.getLogger(GAEJCronServlet.class
			.getName());

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		try {
			log.info("Cron Job has been executed");
//			for(Video video :VideoHelper.getVideos() ){
//				ServiceUtil.VIDEO_SERVICE.addVideo(video);
//			}
			Queue queue = QueueFactory.getQueue("subscription-queue");
			queue.add(TaskOptions.Builder.withUrl("/gaejsignupsubscriber").param(GAEJSignupSubscriberServlet.PARAM_REQ_HANDLER, "READ_MIRCHI"));
			queue.add(TaskOptions.Builder.withUrl("/gaejsignupsubscriber").param(GAEJSignupSubscriberServlet.PARAM_REQ_HANDLER, "READ_TELUGU_LINKS"));
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
