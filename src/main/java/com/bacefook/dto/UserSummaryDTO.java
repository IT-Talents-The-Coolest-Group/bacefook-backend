package com.bacefook.dto;

import org.springframework.lang.NonNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
public class UserSummaryDTO {
	
	private Integer id;
	@NonNull 
	private String firstName;
	@NonNull 
	private String lastName;
	private String profilePhotoUrl;
	private Integer friendsCount;	
	

}
