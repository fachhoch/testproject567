package com.myprojects.logdisplay;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.google.appengine.repackaged.com.google.common.collect.Lists;
import com.myprojects.logdisplay.util.SpringApplicationContext;

@SuppressWarnings("serial")
public class LogFileServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
			Long  id=new Long(req.getParameter("id"));
			
			
		    JdbcTemplate  jdbcTemplate= new JdbcTemplate((DataSource)SpringApplicationContext.getBean("dataSource"));
		    String result= jdbcTemplate.queryForObject(" select body from logfile   where id= ?  ",new Object[]{id}, new RowMapper<String>(){
		    	@Override
		    	public String mapRow(ResultSet rs, int rowNum)
		    			throws SQLException {
		    		return rs.getString("body");
		    	}
		    });
		    req.setAttribute("results", Lists.newArrayList(StringUtils.split(result, "\n")));
		    String destination  ="/logFile.jsp";  
		    try{
		    	getServletContext().getRequestDispatcher(destination).forward(req, resp);
		    }catch (Exception e) {
				throw new RuntimeException(e);
			}
		    
	}
}
