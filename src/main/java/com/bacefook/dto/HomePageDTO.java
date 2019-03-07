package com.bacefook.dto;

import java.util.HashMap;
import java.util.List;

import lombok.Data;
import lombok.NonNull;

@Data
public class HomePageDTO {
	@NonNull
	private HashMap<String,UserDTO> loggedUser;
	@NonNull
	private HashMap<String,List<PostDTO>> allPosts;
}
