package com.bacefook.dto;

import org.springframework.lang.NonNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendsListDTO {
//	@NonNull private Integer id;
	@NonNull private String firstName;
	@NonNull private String lastName;
	@NonNull private int friendsCount;
	

}
