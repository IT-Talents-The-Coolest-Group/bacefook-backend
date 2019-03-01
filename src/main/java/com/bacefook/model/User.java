package com.bacefook.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
	@Id
	private Integer id;
	
	private int genderId;
	private String email;
	
	private String firstName;
	private String lastName;
	private String password;
	private LocalDate birthday;
}
