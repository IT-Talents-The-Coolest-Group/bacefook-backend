package com.bacefook.dto;

import java.time.LocalDateTime;

import com.bacefook.utility.TimeConverter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
	
	private Integer id;
	
	private String posterFullName;
	
	private Integer sharesPostId;
	
	@NonNull
	private String content;
	@NonNull
	private String postingTime;

	public void setPostingTime(LocalDateTime postingTime) {
		this.postingTime = TimeConverter.convertTimeToString(postingTime);
	}
}
