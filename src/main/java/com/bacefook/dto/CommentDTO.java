package com.bacefook.dto;

import java.time.LocalDateTime;

import com.bacefook.utility.TimeConverter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class CommentDTO {
	@NonNull
	private Integer postId;
//TODO commented on property
	@NonNull
	private String content;
	@NonNull
	private String postingTime;

	public void setPostingTime(LocalDateTime postingTime) {
		this.postingTime = TimeConverter.convertTimeToString(postingTime);
	}
}
