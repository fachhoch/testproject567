package org.sairam.ns.util;

import java.text.MessageFormat;

import org.sairam.ns.service.model.User;

public class WebUtil {
	
	public static final int RECORDS_PER_PAGE=20;
	
	public static String formatUser(User  user){
		return MessageFormat.format("{0} {1}", user.getFirstName(),user.getLastName());
	}
	
	
}
