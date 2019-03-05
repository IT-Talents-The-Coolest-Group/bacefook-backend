package com.bacefook.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bacefook.dto.PostContentDTO;
import com.bacefook.dto.PostDTO;
import com.bacefook.exception.ElementNotFoundException;
import com.bacefook.exception.UnauthorizedException;
import com.bacefook.model.Post;
import com.bacefook.service.PostService;
import com.bacefook.service.UserService;

@RestController
public class PostsController {

	@Autowired
	private PostService postsService;
	@Autowired
	private UserService userService;
	
	@PostMapping("/posts")
	public ResponseEntity<Object> addPostToUser(@RequestBody PostContentDTO postContentDto, HttpServletRequest request)
			throws UnauthorizedException { // Exceptions
		int posterId = SessionManager.getLoggedUser(request).getId();
		// TODO validate if properties are not empty
		
		Post post = new Post(posterId, postContentDto.getContent(), LocalDateTime.now());
		postsService.savePost(post);
		return new ResponseEntity<>(post.getId(), HttpStatus.OK);
	}

	@GetMapping("/posts")
	public ResponseEntity<List<PostDTO>> getAllPostsByUser(@RequestHeader("posterId") 
		int posterId,HttpServletRequest request) 
			throws UnauthorizedException, ElementNotFoundException {
		
		// TODO check if user is friend with poster
		SessionManager.getLoggedUser(request);
	
		List<Post> posts = postsService.findAllPostsByUserId(posterId);
		List<PostDTO> returnedPosts = new ArrayList<>();
		
		for (Post post : posts) {
			String posterFullName = userService.
					findUserById(post.getPosterId()).getFullName();
			
			String timeOfPosting = PostDTO.convertTimeToString(post.getPostingTime());
			
			PostDTO postDto = new PostDTO(posterFullName, 
					post.getSharesPostId(), post.getContent(), timeOfPosting);
			
			returnedPosts.add(postDto);
		}
		return new ResponseEntity<>(returnedPosts, HttpStatus.OK);

	}

	// get post by id
	@PutMapping("/posts")
	public void updateStatus(@RequestParam("postId") Integer postId, @RequestBody PostContentDTO content,
			HttpServletRequest request) throws UnauthorizedException, ElementNotFoundException {
		if (SessionManager.isLogged(request)) {
			System.out.println(content);
			Post post = postsService.findPostById(postId);

			if (content.getContent().isEmpty()) {
				throw new ElementNotFoundException("Cannot update post with empty content!");
			}
			post.setContent(content.getContent());
			postsService.savePost(post);
		} else {
			throw new UnauthorizedException("You are not logged in! Please log in before trying to update your posts.");
		}
	}

}
