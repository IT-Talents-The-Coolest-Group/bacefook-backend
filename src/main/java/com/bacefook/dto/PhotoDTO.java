package com.bacefook.dto;

import com.bacefook.model.Post;

import lombok.Data;
import lombok.NonNull;

@Data
public class PhotoDTO {
	@NonNull
	private Integer id;
	@NonNull
	private String url;
	@NonNull
	private Post post;
}
