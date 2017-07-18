package com.spiderman.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {

	private static SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式

	public static String getCurrentTime() {
		String time = timeFormat.format(Calendar.getInstance().getTime());
		return time;
	}

	/**
	 *
	 * @param format = "yyyy-MM-dd" or "yyyy-MM-dd HH:mm:ss"
	 * @return
	 */
	public static String getCurrentTime(String format) {
		SimpleDateFormat sdFormat = new SimpleDateFormat(format);
		String time = sdFormat.format(Calendar.getInstance().getTime());
		return time;
	}

	public static String getTimeBeforeDays(int day) {
		Date currentDate = Calendar.getInstance().getTime();
		String time = timeFormat.format(getDateBefore(currentDate, day));
		return time;
	}
	
	public static String getDateBeforeDays(int day) {
		Date currentDate = Calendar.getInstance().getTime();
		String time = dateFormat.format(getDateBefore(currentDate, day));
		return time;
	}

	/**
	 * 得到几天前的时间
	 *
	 * @param d
	 * @param day
	 * @return
	 */
	public static Date getDateBefore(Date d, int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
		return now.getTime();
	}

	/**
	 * 得到几天后的时间
	 *
	 * @param d
	 * @param day
	 * @return
	 */
	public static Date getDateAfter(Date d, int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
		return now.getTime();
	}

	public static Date getCurrentDate(){
		return new Date();
	}


	public static Date stringToDate(String date){
		try{
			return dateFormat.parse(date);
		}catch(Exception e){
			return null;
		}
	}

}
