package com.bacefook.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name="users")
public class User {
	@Id
	private int id;
	private String email;
	private String password;
	private String firstName;
	private String lastName;
	private LocalDate birthday;
	private int gender;
}
