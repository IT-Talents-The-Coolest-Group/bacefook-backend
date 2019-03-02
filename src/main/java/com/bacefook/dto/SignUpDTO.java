package com.bacefook.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class SignUpDTO {
	@NotNull private int genderId;
	@NotNull private String email;
	@NotNull private String firstName;
	@NotNull private String lastName;
	@NotNull private String password;
	@NotNull private String passwordConfirmation;
	@NotNull private LocalDate birthday;
}
