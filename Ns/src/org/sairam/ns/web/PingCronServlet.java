package org.sairam.ns.web;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PingCronServlet extends HttpServlet {

	private static final Logger log = Logger.getLogger(PingCronServlet.class
			.getName());

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		try {
			log.info("ping Cron Job has been executed");			
			log.info("doen job");
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
