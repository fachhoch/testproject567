package com.gae.yotube;

import java.io.IOException;
import javax.servlet.http.*;

@SuppressWarnings("serial")
public class Utube_video_searchServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().println("Hello, world");
	}
}
