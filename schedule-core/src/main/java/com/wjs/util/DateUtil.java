package com.wjs.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(DateUtil.class);

	static SimpleDateFormat dayFormat = new SimpleDateFormat("yyyyMMdd");
	public static Integer addIntDate(Integer intdate, int offset) {
		
		if(null == intdate){
			return null;
		}
		Date date = DateUtils.addDays(parseIntDate(intdate), offset);
		return getIntDay(date);
	}
	
	public static Date parseIntDate(Integer intdate){
		
		try {
			Date date = dayFormat.parse(String.valueOf(intdate));
			return date;
		} catch (ParseException e) {
			LOGGER.error("format int date error");
		}
		return null;
	}
	
	public static Integer getIntDay(Date date){
		
		return Integer.valueOf(getStringDay(date));
	}
	
	public static String getStringDay(Date date){
		
		return dayFormat.format(date);
	}

}
