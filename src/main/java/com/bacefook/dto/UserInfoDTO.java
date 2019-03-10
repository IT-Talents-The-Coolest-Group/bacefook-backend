package com.bacefook.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDTO {
	@NonNull
	private Integer cityId;
	private Integer profilePhotoId;
	private Integer coverPhotoId;
	@NonNull
	private String phone;
}
