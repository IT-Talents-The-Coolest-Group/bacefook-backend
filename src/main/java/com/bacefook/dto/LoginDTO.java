package com.bacefook.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class LoginDTO {
	@NotNull private String email;
	@NotNull private String password;
}
