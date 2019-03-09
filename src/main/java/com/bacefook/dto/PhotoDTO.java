package com.bacefook.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhotoDTO {
	@NonNull
	private Integer id;
	@NonNull
	private String url;
	@NonNull
	private Integer postId;
}
