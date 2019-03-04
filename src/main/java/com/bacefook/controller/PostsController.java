package com.bacefook.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bacefook.dto.PostDTO;
import com.bacefook.exception.UnauthorizedException;
import com.bacefook.model.Post;
import com.bacefook.service.PostService;

@RestController
public class PostsController {

	@Autowired
	private PostService postsService;

	@PostMapping("/posts")
	public int addPostToUser(@RequestBody Post post, HttpServletRequest request) throws UnauthorizedException { // Exceptions
		if (SessionManager.isLogged(request)) {
			post.setPostingTime(LocalDateTime.now());
			postsService.savePost(post);
			return post.getId();
		} else {
			throw new UnauthorizedException("You are not logged! Please log in before you can add post.");
		}
	}

	@GetMapping("/posts")
	public List<PostDTO> getAllPostsByUser(@RequestParam("posterId") Integer posterId,HttpServletRequest request) throws UnauthorizedException {
		if(SessionManager.isLogged(request)) {
		List<Post> posts = postsService.findAllPostsByUserId(posterId);
		List<PostDTO> returnedPosts = new ArrayList<>();
		for (Post post : posts) {
			PostDTO postDto = new PostDTO(post.getPosterId(), post.getSharesPostId(), post.getContent(),
					post.getPostingTime());
			returnedPosts.add(postDto);
		}
		return returnedPosts;
		}else {
			throw new UnauthorizedException("You are not logged! Please log in before you can see your posts.");
		}
	}

}
