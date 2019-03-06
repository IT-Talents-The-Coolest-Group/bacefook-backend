package com.bacefook.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class UserDTO {
	@NonNull
	private String firstName;
//	@NonNull //TODO
	private String profilePictureUrl;
}
