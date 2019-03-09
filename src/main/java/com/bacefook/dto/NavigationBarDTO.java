package com.bacefook.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NavigationBarDTO {
//	@NonNull
//	private Integer id; //TODO id?
	@NonNull
	private String firstName;
	private String profilePhotoUrl;
	@NonNull
	private Integer friendRequestsCount;
}
