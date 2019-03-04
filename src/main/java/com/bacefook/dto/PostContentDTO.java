package com.bacefook.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
@Data
@AllArgsConstructor
public class PostContentDTO {
	@NonNull
	private String content;
}
