package com.bacefook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.bacefook.service.PostService;

@RestController
public class PostsController {

	@Autowired
	private PostService postsService;
	
	// TODO "post request" a new post from a user
	
	// TODO get all posts of a specific user
}
