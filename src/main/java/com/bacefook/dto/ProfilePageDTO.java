package com.bacefook.dto;

import java.util.List;

import lombok.Data;
import lombok.NonNull;

@Data
public class ProfilePageDTO {
	@NonNull
	private UserSummaryDTO user;
	@NonNull
	private List<PostDTO> userPosts;
	@NonNull
	private Integer friendsCount;
	@NonNull
	private Integer friendsRequestsCount;
}
