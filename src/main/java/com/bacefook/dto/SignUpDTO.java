package com.bacefook.dto;

import java.time.LocalDate;

import lombok.Data;
import lombok.NonNull;

@Data
public class SignUpDTO {
	@NonNull
	private String gender;
	@NonNull
	private String email;
	@NonNull
	private String firstName;
	@NonNull
	private String lastName;
	@NonNull
	private String password;
	@NonNull
	private String passwordConfirmation;
	@NonNull
	private LocalDate birthday;
}
