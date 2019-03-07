package com.bacefook.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bacefook.dto.UserSummaryDTO;
import com.bacefook.dto.HomePageDTO;
import com.bacefook.dto.PostContentDTO;
import com.bacefook.dto.PostDTO;
import com.bacefook.exception.ElementNotFoundException;
import com.bacefook.exception.UnauthorizedException;
import com.bacefook.model.Post;
import com.bacefook.model.User;
import com.bacefook.service.PostService;
import com.bacefook.service.UserService;
import com.bacefook.utility.TimeConverter;

//@CrossOrigin(origins = "http://bacefook.herokuapp.com")
@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class PostsController {

	@Autowired
	private PostService postsService;
	@Autowired
	private UserService userService;
	private ModelMapper mapper = new ModelMapper();

	@GetMapping("/home")
	public HomePageDTO homePage(HttpServletRequest request)
			throws UnauthorizedException, ElementNotFoundException {
		
		Integer userId = SessionManager.getLoggedUser(request);
		User loggedUser = userService.findById(userId);
		UserSummaryDTO user = new UserSummaryDTO(loggedUser.getFirstName(), loggedUser.getLastName());// TODO profile picture,cover photo
		HashMap<String, UserSummaryDTO> userMap = new HashMap<>();
		
		userMap.put("loggedUser", user);
		List<Post> posts = postsService.findAllPostsFromFriends(userId);
		List<PostDTO> allFriendsPosts = new ArrayList<PostDTO>(posts.size());
		
		for (Post post : posts) {
			PostDTO postDTO = new PostDTO();
			this.mapper.map(post, postDTO);
			allFriendsPosts.add(postDTO);
		}
		
		HashMap<String, List<PostDTO>> friendsPostsMap = new HashMap<>();
		friendsPostsMap.put("friendsPosts", allFriendsPosts);
		int friendsRequests = userService.findAllFromRequestsTo(userId).size();
		Map<String, Integer> requestsMap = new HashMap<>();
		requestsMap.put("friendRequestsCount", friendsRequests);

		HomePageDTO home = new HomePageDTO(userMap, friendsPostsMap);

		return home;
	}

	@PostMapping("/postlikes")
	public void likeAPost(@RequestParam("postId") Integer postId, HttpServletRequest request)
			throws UnauthorizedException {
		int userId = SessionManager.getLoggedUser(request);
		postsService.likePost(userId, postId);
	}

	@GetMapping("/postlikes")
	public List<UserSummaryDTO> getAllUsersWhoLikedPost(@RequestParam("postId") Integer postId) {
		List<User> users = postsService.findAllUsersWhoLikedAPost(postId);
		List<UserSummaryDTO> returnUsers = new ArrayList<UserSummaryDTO>();
		for (User user : users) {
			UserSummaryDTO dto = new UserSummaryDTO(user.getFirstName(), user.getLastName());
			returnUsers.add(dto);
		}
		return returnUsers;
	}

	@GetMapping("/postlikes-size")
	public Integer getLikesCountOnPost(@RequestParam("postId") Integer postId) {
		List<User> likers = postsService.findAllUsersWhoLikedAPost(postId);
		return likers.size();
	}

	@PostMapping("/posts")
	public Integer createPost(@RequestBody PostContentDTO postContentDto, HttpServletRequest request)
			throws UnauthorizedException { // Exceptions
		int posterId = SessionManager.getLoggedUser(request);
		// TODO validate if properties are not empty

		Post post = new Post(posterId, postContentDto.getContent(), LocalDateTime.now());
		postsService.save(post);
		return post.getId();
	}

	@GetMapping("/posts")
	public List<PostDTO> getAllPostsOfUser(@RequestParam("posterId") int posterId,
			HttpServletRequest request) throws UnauthorizedException, ElementNotFoundException {

		// TODO check if user is friend with poster
		// SessionManager.getLoggedUser(request);

		List<Post> posts = postsService.findAllByUserId(posterId);
		List<PostDTO> returnedPosts = new ArrayList<>();

		for (Post post : posts) {
			String posterFullName = userService.findById(post.getPosterId()).getFullName();

			String timeOfPosting = TimeConverter.convertTimeToString(post.getPostingTime());

			PostDTO postDto = new PostDTO(post.getId(), posterFullName, post.getSharesPostId(), post.getContent(),
					timeOfPosting);

			returnedPosts.add(postDto);
		}
		return returnedPosts;

	}

	@PutMapping("/posts")
	public void updateStatus(@RequestParam("postId") Integer postId, @RequestBody PostContentDTO content,
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
	}

}
