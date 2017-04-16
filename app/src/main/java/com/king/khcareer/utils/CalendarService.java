package com.king.khcareer.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarService {


	public static int getLatestMondayOfYear(long date) {
		int day = 0;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(date));
		day = calendar.get(Calendar.DAY_OF_YEAR);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		day = day - (dayOfWeek - Calendar.MONDAY);
		return day;
	}
	/**
	 *
	 * @param year
	 * @param month
	 * @param day if>0, then find monday till this day. if -1, then find all monday in this month
	 * @return
	 */
	public static List<String> getMondayList(int year, int month, int day) {

		List<String> list = null;
		int maxDay = 0;
		if (day > 0) {
			maxDay = day;
		}
		else {
			switch (month) {
				case 1:
				case 3:
				case 5:
				case 7:
				case 8:
				case 10:
				case 12:
					maxDay = 31;
					break;
				case 4:
				case 6:
				case 9:
				case 11:
					maxDay = 30;
					break;
				case 2:
					maxDay = 28;
					break;
			}
		}
		list = new ArrayList<String>();
		for (int i = maxDay; i > 0; i --) {
			if (isMonday(year, month, i)) {
				list.add("" + i);
			}
		}
		return list;
	}

	private static boolean isMonday(int year, int month, int day) {

		/*
		 蔡勒公式：w=y+[y/4]+[c/4]-2c+[26(m+1)/10]+d-1
		符号意义：
		w：星期； w对7取模得：0-星期日，1-星期一，2-星期二，3-星期三，4-星期四，5-星期五，6-星期六
		c：世纪-1（前两位数）
		y：年（后两位数）
		m：月（m大于等于3，小于等于14，即在蔡勒公式中，某年的1、2月要看作上一年的13、14月来计算，比如2003年1月1日要看作2002年的13月1日来计算）
		d：日
		[ ]代表取整，即只要整数部分。
		例：中华人民共和国成立100周年纪念日那天（2049年10月1日）是星期几？
		w=y+[y/4]+[c/4]-2c+[26(m+1)/10]+d-1=49+[49/4]+[20/4]-2×20+[26×(10+1)/10]+1-1=49+[12.25]+5-40+[28.6] =49+12+5-40+28 =54 (除以7余5)
		即2049年10月1日（100周年国庆）是星期五。
		 */
		if ( month == 1 || month == 2) {
			year --;
			month += 12;
		}

		int c = year / 100;
		int y = year - c * 100;
		int sum = (c/4)-2*c+(y+y/4)+(13*(month+1)/5)+day-1;
		while (sum < 0) {//like 2015.3.2, it's monday but sum=-6
			sum += 7;
		}
		if (sum % 7 == 1) {
			return true;
		}
		return false;
	}

}
