package com.myprojects.logdisplay.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import com.caucho.hessian.server.HessianServlet;

public class DefaultLogService  extends  HessianServlet implements LogFileService {
	
	DataSource dataSource= SpringApplicationContext.getBean("dataSource");
	@Override
	public void createOrUpdateLog(String body) {
		JdbcTemplate select = new JdbcTemplate(dataSource);
	    Long id=select.query("select  id  from   logfile where created_on  = CURRENT_DATE() ", new ResultSetExtractor<Long>(){
	    	@Override
	    	public Long extractData(ResultSet rs) throws SQLException,
	    			DataAccessException {
	    		while(rs.next()){
	    			return rs.getLong("id");
	    		}
	    		return null;
	    	}
	    });
	    if(id==null){
	    	create(body);
	    }else update(body, id);
	    System.out.println(body);
	    
	}
	
	void create(String body){
		JdbcTemplate insert = new JdbcTemplate(dataSource);
		insert.update("INSERT INTO logfile  (created_on, body) VALUES(?,?)",
				new Object[] { new Date(new java.util.Date().getTime()), body });
	}
	
	void update(String body, Long id){
		JdbcTemplate insert = new JdbcTemplate(dataSource);
		insert.update("update  logfile  set body=? where id=? ",
				new Object[] { body, id});
	}

	
	
}
