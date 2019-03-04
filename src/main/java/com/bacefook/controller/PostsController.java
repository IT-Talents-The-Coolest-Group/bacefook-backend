package com.bacefook.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bacefook.dto.PostContentDTO;
import com.bacefook.dto.PostDTO;
import com.bacefook.exception.PostNotFoundException;
import com.bacefook.exception.UnauthorizedException;
import com.bacefook.model.Post;
import com.bacefook.service.PostService;

@RestController
public class PostsController {

	@Autowired
	private PostService postsService;

	@PostMapping("/posts/{posterId}")
	public int addPostToUser(@PathVariable("posterId")Integer posterId,@RequestBody PostContentDTO postContentDto, HttpServletRequest request) throws UnauthorizedException { // Exceptions
		if (SessionManager.isLogged(request)) {
			//TODO validate if properties are not empty
			Post post = new Post(posterId, postContentDto.getContent(), LocalDateTime.now());
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
	//TODO get post by id
	@PutMapping("/posts")
	public void updateStatus(@RequestParam("postId")Integer postId,@RequestBody String content ,HttpServletRequest request) throws UnauthorizedException, PostNotFoundException {
		if(SessionManager.isLogged(request)) {
			System.out.println(content);
			Post post = postsService.findPostById(postId);
			if(content.isEmpty()) {
				throw new PostNotFoundException("Cannot update post with empty content!");
			}
			post.setContent(content);
			postsService.savePost(post);
		}else {
			throw new UnauthorizedException("You are not logged! Please log in before you can update your posts.");
		}
	}

}
