package com.myprojects.logdisplay;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.google.appengine.repackaged.com.google.common.collect.Maps;
import com.myprojects.logdisplay.util.SpringApplicationContext;

@SuppressWarnings("serial")
public class LogdisplayServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		    JdbcTemplate  jdbcTemplate= new JdbcTemplate((DataSource)SpringApplicationContext.getBean("dataSource"));
		    List<Map<Long, String>>  results= jdbcTemplate.query(" select id,created_on from logfile order by id desc ", new RowMapper<Map<Long, String>>(){
		    	@Override
		    	public Map<Long, String> mapRow(ResultSet rs, int rowNum)
		    			throws SQLException {
		    		Map<Long, String>  result= Maps.newHashMap();
		    		result.put(rs.getLong("id"), new SimpleDateFormat("MM/dd/yyyy").format(rs.getDate("created_on"))); 
		    		return result;
		    	}
		    });
		    req.setAttribute("results", results);
		    String destination  ="/display.jsp";  
		    try{
		    	getServletContext().getRequestDispatcher(destination).forward(req, resp);
		    }catch (Exception e) {
				throw new RuntimeException(e);
			}
		    
	}
}
