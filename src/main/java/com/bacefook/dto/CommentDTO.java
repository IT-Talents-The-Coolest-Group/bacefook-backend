package com.bacefook.dto;

import java.time.LocalDateTime;

import com.bacefook.utility.TimeConverter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
	private Integer id;
	private Integer comentedOnId;
	private String posterFullName;
	@NonNull
	private String content;
	@NonNull
	private String postingTime;

	public void setPostingTime(LocalDateTime postingTime) {
		this.postingTime = TimeConverter.convertTimeToString(postingTime);
	}
}
