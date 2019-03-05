package com.bacefook.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
public class PostsController {

	@Autowired
	private PostService postsService;

	@PostMapping("/posts")
	public int addPostToUser(@RequestBody PostContentDTO postContentDto, HttpServletRequest request)
			throws UnauthorizedException { // Exceptions
		int posterId = SessionManager.getLoggedUser(request).getId();
		// TODO validate if properties are not empty
		// TODO should we cast posterId from Object or method getLoggedUser should
		// return int
		Post post = new Post(posterId, postContentDto.getContent(), LocalDateTime.now());
		postsService.savePost(post);
		return post.getId();
//		} else {
//			throw new UnauthorizedException("You are not logged in! Please log in before you can add a post.");
//		}
	}

	@GetMapping("/posts")
	public List<PostDTO> getAllPostsByUser(@RequestHeader("posterId") int posterId,HttpServletRequest request) throws UnauthorizedException {
//		int posterId = SessionManager.getLoggedUser(request).getId();
		List<Post> posts = postsService.findAllPostsByUserId(posterId);
		List<PostDTO> returnedPosts = new ArrayList<>();
		for (Post post : posts) {
			PostDTO postDto = new PostDTO(post.getPosterId(), post.getSharesPostId(), post.getContent(),
					post.getPostingTime());
			returnedPosts.add(postDto);
		}
		return returnedPosts;
//		} else {
//			throw new UnauthorizedException("You are not logged! Please log in before you can see your posts.");
//		}
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
