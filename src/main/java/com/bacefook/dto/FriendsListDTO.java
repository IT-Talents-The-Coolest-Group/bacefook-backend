package com.bacefook.dto;

import org.springframework.lang.NonNull;

import lombok.Data;

@Data
public class FriendsListDTO {
	@NonNull private Integer id;
	@NonNull private String firstName;
	@NonNull private String lastName;
	@NonNull private int friendsCount;
	

}
