package org.sairam.ns.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatUtil {
	
	
	public static  String formatDate(Date  date){
		return new SimpleDateFormat("MM/dd/yyyy").format(date);
	}
	
}