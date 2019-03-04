package com.bacefook.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
@Data
@AllArgsConstructor
public class PostDTO {
	@JsonIgnore //TODO if we should return poster id
	private Integer posterId;
	private Integer sharesPostId;
	@NonNull
	private String content;
	@NonNull
	private LocalDateTime postingTime;
}
