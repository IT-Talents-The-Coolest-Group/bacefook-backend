package com.bacefook.utility;

import java.time.LocalDateTime;

public class TimeConverter {
	
	private enum TimeUnit {
		YEARS("years ago"),
		MONTHS("months ago"),
		DAYS("days ago"),
		HOURS("hours ago"),
		MINUTES("minutes ago"),
		JUST_NOW("just now");
		 
		private String unit;
		
		private TimeUnit(String unit) {
			this.unit = unit;
		}
		
		@Override
		public String toString() {
			return this.unit;
		}
	}

	public static String convertTimeToString(LocalDateTime postingTime) {
		LocalDateTime now = LocalDateTime.now();
		int time;
		
		time = now.getYear() - postingTime.getYear();
		if (time > 0) {
			return time + " " + TimeUnit.YEARS;
		}
		time = now.getMonthValue() - postingTime.getMonthValue();
		if (time > 0) {
			return time + " " + TimeUnit.MONTHS;
		}
		time = now.getDayOfMonth() - postingTime.getDayOfMonth();
		if (time > 0) {
			return time + " " + TimeUnit.DAYS;
		}
		time = now.getHour() - postingTime.getHour();
		if (time > 0) {
			return time + " " + TimeUnit.HOURS;
		}
		time = now.getMinute() - postingTime.getMinute();
		if (time > 0) {
			return time + " " + TimeUnit.MINUTES;
		}
		else {
			return TimeUnit.JUST_NOW.toString();
		}
	}

}
