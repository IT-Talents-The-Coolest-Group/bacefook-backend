package com.bacefook.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class PostDTO {
	private String posterFullName;
	private Integer sharesPostId;
	@NonNull
	private String content;
	@NonNull
	private String postingTime;

	public void setPostingTime(LocalDateTime postingTime) {
		this.postingTime = convertTimeToString(postingTime);
	}

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
