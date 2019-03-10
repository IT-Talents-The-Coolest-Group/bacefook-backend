package com.bacefook.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfilePageDTO {
	private NavigationBarDTO navBar;
	private UserInfoDTO userInfo;
	@NonNull
	private UserSummaryDTO user;
	@NonNull
	private List<PostDTO> userPosts;
}
