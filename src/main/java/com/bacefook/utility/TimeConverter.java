package com.bacefook.utility;

import java.time.LocalDateTime;

public class TimeConverter {

	public static String convertTimeToString(LocalDateTime postingTime) {
		LocalDateTime now = LocalDateTime.now();
		int time;
		
		time = now.getYear() - postingTime.getYear();
		if (time > 0) {
			return time + " years";
		}
		time = now.getMonthValue() - postingTime.getMonthValue();
		if (time > 0) {
			return time + " months";
		}
		time = now.getDayOfMonth() - postingTime.getDayOfMonth();
		if (time > 0) {
			return time + " days";
		}
		time = now.getHour() - postingTime.getHour();
		if (time > 0) {
			return time + " hours";
		}
		time = now.getMinute() - postingTime.getMinute();
		if (time > 0) {
			return time + " minutes";
		}
		else {
			return "Just now";
		}
	}

}
