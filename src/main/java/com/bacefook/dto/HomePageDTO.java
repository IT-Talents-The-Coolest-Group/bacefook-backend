package com.bacefook.dto;

import java.util.List;

import lombok.Data;
import lombok.NonNull;

@Data
public class HomePageDTO {
	@NonNull
	private UserSummaryDTO loggedUser;
	@NonNull
	private List<PostDTO> allPosts;
	@NonNull
	private Integer friendRequestsCount;
}
