package com.bacefook.model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {
	private int id;
	private String email;
	private String password;
	private String firstName;
	private String lastName;
	private LocalDate birthday;
	private int gender;
}
