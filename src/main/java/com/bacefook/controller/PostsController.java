package com.bacefook.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bacefook.model.Post;
import com.bacefook.service.PostService;

@RestController
public class PostsController extends GlobalExceptionHandler{

	@Autowired
	private PostService postsService;

	@PostMapping("/posts")
	public int addPostToUser(@RequestBody Post post) {//TODO BaseController is not catching Exceptions 
		post.setPostingTime(LocalDateTime.now());
		postsService.savePost(post);
		return post.getId();
	}
	// TODO "post request" a new post from a user

	// TODO get all posts of a specific user
}
