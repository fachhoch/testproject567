package com.sanskrit.app;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GAEJCronServlet extends HttpServlet {

	private static final Logger log = Logger.getLogger(GAEJCronServlet.class
			.getName());

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		try {
			log.info("Cron Job has been executed");
			SankritFileReader  sankritFileReader =new SankritFileReader();
			sankritFileReader.sendEmail();
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
