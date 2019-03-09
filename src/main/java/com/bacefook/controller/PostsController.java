package com.bacefook.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
		
		UserSummaryDTO user = new UserSummaryDTO();
		mapper.map(loggedUser, user);
		user.setFriendsCount(userService.getFriendsCountOF(userId));
		user.setProfilePhotoUrl(userService.findProfilePhotoUrl(userId));

		//TODO remove friends count from UserSummaryDTO, property is for /profile, no point setting it for the home page
		// TODO profile picture
		List<Post> posts = postsService.findAllPostsFromFriends(userId);
		List<PostDTO> allFriendsPosts = new ArrayList<PostDTO>(posts.size());
		for (Post post : posts) {
			PostDTO postDTO = new PostDTO();
			this.mapper.map(post, postDTO);
			String posterFullName = userService.findById(post.getPosterId()).getFullName();
			postDTO.setPosterFullName(posterFullName);
			allFriendsPosts.add(postDTO);
		}

		int friendsRequests = userService.findAllFromRequestsTo(userId).size();

		HomePageDTO home = new HomePageDTO(user, allFriendsPosts,friendsRequests);

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
			dto.setFriendsCount(userService.getFriendsCountOF(user.getId()));
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
	public Integer createPost(@RequestBody String content, HttpServletRequest request)
			throws UnauthorizedException, ElementNotFoundException {
	
		int posterId = SessionManager.getLoggedUser(request);
		//String content = postContentDto.getContent();
		
		if (content == null || content.isEmpty()) {
			throw new ElementNotFoundException("Write something before posting!");
		}
		
		Post post = new Post(posterId, content, LocalDateTime.now());
		postsService.save(post);
		return post.getId();
	}

	@PostMapping("/postshares")
	public Integer sharePost(@RequestParam("sharesPostId") Integer sharesPostId,
			@RequestBody PostContentDTO postContentDto, HttpServletRequest request) 
					throws UnauthorizedException, ElementNotFoundException {
		
		int posterId = SessionManager.getLoggedUser(request);
		
		return postsService.save(sharesPostId, posterId, postContentDto);
	}

	@GetMapping("/postshares")
	public List<PostDTO> getAllPostShares(@RequestParam("postId") Integer postId) throws ElementNotFoundException {
		List<Post> posts = postsService.findAllWhichSharePostId(postId);
		List<PostDTO> postsDto = new ArrayList<>();
		for (Post post : posts) {
			String posterFullName = userService.findById(post.getPosterId()).getFullName();
			PostDTO postDto = new PostDTO();
			this.mapper.map(post, postDto);
			postDto.setPosterFullName(posterFullName);
			postsDto.add(postDto);
		}
		return postsDto;
	}

	@GetMapping("/posts")
	public List<PostDTO> getAllPostsOfUser(@RequestParam("posterId") int posterId)
			throws UnauthorizedException, ElementNotFoundException {

		// TODO check if user is friend with poster
		// SessionManager.getLoggedUser(request);

		List<Post> posts = postsService.findAllByUserId(posterId);
		List<PostDTO> returnedPosts = new ArrayList<>();

		for (Post post : posts) {
			String posterFullName = userService.findById(post.getPosterId()).getFullName();

//			String timeOfPosting = TimeConverter.convertTimeToString(post.getPostingTime());
			PostDTO postDto = new PostDTO();
			this.mapper.map(post, postDto);
			postDto.setPosterFullName(posterFullName);
//			PostDTO postDto = new PostDTO(post.getId(), posterFullName, post.getSharesPostId(), post.getContent(),
//					timeOfPosting);

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
