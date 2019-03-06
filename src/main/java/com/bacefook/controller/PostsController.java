package com.bacefook.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bacefook.dto.UserSummaryDTO;
import com.bacefook.dto.PostContentDTO;
import com.bacefook.dto.PostDTO;
import com.bacefook.exception.ElementNotFoundException;
import com.bacefook.exception.UnauthorizedException;
import com.bacefook.model.Post;
import com.bacefook.model.User;
import com.bacefook.service.PostService;
import com.bacefook.service.UserService;
import com.bacefook.utility.TimeConverter;

@CrossOrigin
@RestController
public class PostsController {

	@Autowired
	private PostService postsService;
	@Autowired
	private UserService userService;

	@PostMapping("/postlikes")
	public ResponseEntity<Object> likeAPost(@RequestParam("postId") Integer postId, HttpServletRequest request)
			throws UnauthorizedException {
		int userId = SessionManager.getLoggedUser(request).getId();
		postsService.likePost(userId, postId);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping("/postlikes")
	public ResponseEntity<List<UserSummaryDTO>> getAllUsersWhoLikedPost(@RequestParam("postId") Integer postId) {
		List<User> users = postsService.findAllUsersWhoLikedAPost(postId);
		List<UserSummaryDTO> returnUsers = new ArrayList<UserSummaryDTO>();
		for (User user : users) {
			UserSummaryDTO dto = new UserSummaryDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getFriends().size());
			returnUsers.add(dto);
		}
		return new ResponseEntity<List<UserSummaryDTO>>(returnUsers, HttpStatus.OK);
	}

	@GetMapping("/postlikes-size")
	public ResponseEntity<Integer> getLikesCountOnPost(@RequestParam("postId") Integer postId) {
		List<User> likers = postsService.findAllUsersWhoLikedAPost(postId);
		return new ResponseEntity<Integer>(likers.size(), HttpStatus.OK);
	}

	@PostMapping("/posts")
	public ResponseEntity<Object> createPost(@RequestBody PostContentDTO postContentDto, HttpServletRequest request)
			throws UnauthorizedException { // Exceptions
		int posterId = SessionManager.getLoggedUser(request).getId();
		// TODO validate if properties are not empty

		Post post = new Post(posterId, postContentDto.getContent(), LocalDateTime.now());
		postsService.save(post);
		return new ResponseEntity<>(post.getId(), HttpStatus.OK);
	}

	@GetMapping("/posts")
	public ResponseEntity<List<PostDTO>> getAllPostsOfUser(@RequestParam("posterId") int posterId,
			HttpServletRequest request) throws UnauthorizedException, ElementNotFoundException {

		// TODO check if user is friend with poster
//		SessionManager.getLoggedUser(request);

		List<Post> posts = postsService.findAllByUserId(posterId);
		List<PostDTO> returnedPosts = new ArrayList<>();

		for (Post post : posts) {
			String posterFullName = userService.findById(post.getPosterId()).getFullName();

			String timeOfPosting = TimeConverter.convertTimeToString(post.getPostingTime());

			PostDTO postDto = new PostDTO(post.getId(),posterFullName, post.getSharesPostId(), post.getContent(), timeOfPosting);

			returnedPosts.add(postDto);
		}
		return new ResponseEntity<>(returnedPosts, HttpStatus.OK);

	}

	@PutMapping("/posts")
	public ResponseEntity<String> updateStatus(@RequestParam("postId") Integer postId, @RequestBody PostContentDTO content,
			HttpServletRequest request) throws UnauthorizedException, ElementNotFoundException {
		
		if (!SessionManager.isLogged(request)) {
			throw new UnauthorizedException("You are not logged in! Please log in before trying to update your posts");
		}	
		if (content.getContent().isEmpty()) {
			throw new ElementNotFoundException("Cannot update post with empty content!");
		}
		
		System.out.println(content);

		Post post = postsService.findById(postId);
		post.setContent(content.getContent());
		postsService.save(post);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
