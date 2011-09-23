package com.gae.yotube;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;


public class GAEJCreateTaskServlet extends HttpServlet {
	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Queue queue = QueueFactory.getQueue("subscription-queue");
		queue.add(TaskOptions.Builder.withUrl("/gaejsignupsubscriber").param("noOfTimes", req.getParameter("noOfTimes")));
		resp.getWriter().println("Successfully created a Task in the Queue");
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	}
}
