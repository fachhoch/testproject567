package org.seva.dc.ns.util;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.seva.dc.ns.domain.User;

public class NSUtil {
	
	public static final int RECORDS_PER_PAGE=20;
	
	public static String formatUser(User  user){
		return MessageFormat.format("{0} {1}", user.getFirstName(),user.getLastName());
	}

	public static  String formatDate(Date  date){
		return new SimpleDateFormat("MM/dd/yyyy").format(date);
	}

	
}
