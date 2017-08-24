package com.example.user.FitLife;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by MohammadrezaAsgari on 15/08/2017.
 */

public class Utils {

	private static final java.lang.String DATE_FORMAT_FOR_WEEK = "MMMM d";
	private static long END_TIME;
	private static long START_TIME;
	private static long END_WEEK;
	private static long START_WEEK;
	private static long END_MONTH;
	private static long START_MONTH;

	public static long getEndTime() {
		return END_TIME;
	}

	public static long getStartTime() {
		return START_TIME;
	}

	public static long getEndWeek() {
		return END_WEEK;
	}

	public static long getStartWeek() {
		return START_WEEK;
	}

	public static long getEndMonth() {
		return END_MONTH;
	}

	public static long getStartMonth() {
		return START_MONTH;
	}

	public void initTimes() {
		setDayTime();
		setMonthTime();
		setWeekTime();
	}

	public void setDayTime() {
		Calendar cal = Calendar.getInstance();
		Date now = new Date();
		cal.setTime(now);
		END_TIME = cal.getTimeInMillis();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		START_TIME = cal.getTimeInMillis();
	}

	public void setWeekTime() {
		Calendar calendar = Calendar.getInstance();
		Date now = new Date();
		calendar.setTime(now);
		END_WEEK = calendar.getTimeInMillis();
		calendar.add(Calendar.WEEK_OF_YEAR, -52);
		START_WEEK = calendar.getTimeInMillis();
	}

	public void setMonthTime() {
		Calendar calendar = Calendar.getInstance();
		Date now = new Date();
		calendar.setTime(now);
		END_MONTH = calendar.getTimeInMillis();
		calendar.add(Calendar.MONTH, -12);
		START_MONTH = calendar.getTimeInMillis();
	}

	public static String getDateFormatForWeek(double startDate) {
		String startOfTheWeek;

		SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT_FOR_WEEK);

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis((long) startDate);
		startOfTheWeek = formatter.format(calendar.getTime());

		return startOfTheWeek;
	}
}
