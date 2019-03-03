package com.bacefook.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NonNull;

@Data
public class ChangePasswordDTO {
	@JsonIgnore
	private Integer userId;
	@NonNull
	private String oldPassword;
	@NonNull
	private String newPassword;
	@NonNull
	private String confirmPassword;
}
