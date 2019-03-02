package com.bacefook.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

//@Data
@AllArgsConstructor
@Getter
@Setter
@ToString
public class LoginDTO {
	@NonNull private String email;
	@NonNull private String password;
}
