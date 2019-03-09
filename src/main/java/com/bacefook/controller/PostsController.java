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
import com.bacefook.dto.NavigationBarDTO;
import com.bacefook.dto.PostContentDTO;
import com.bacefook.dto.PostDTO;
import com.bacefook.dto.ProfilePageDTO;
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
	/**
	 * retrieves info for home page and navigation bar
	 ***/
	@GetMapping("/home")
	public HomePageDTO homePage(HttpServletRequest request) 
			throws UnauthorizedException, ElementNotFoundException {

		Integer userId = SessionManager.getLoggedUser(request);
		User loggedUser = userService.findById(userId);
		
		NavigationBarDTO navUser = new NavigationBarDTO();
		this.mapper.map(loggedUser, navUser);
		navUser.setFriendRequestsCount(userService.findAllFromRequestsTo(userId).size());
		navUser.setProfilePhotoUrl(userService.findProfilePhotoUrl(userId));
		
		UserSummaryDTO user = new UserSummaryDTO();
		mapper.map(loggedUser, user);
//		user.setFriendsCount(userService.getFriendsCountOF(userId));
		List<Post> posts = postsService.findAllPostsFromFriends(userId);
		List<PostDTO> allFriendsPosts = new ArrayList<PostDTO>(posts.size());
		for (Post post : posts) {
			PostDTO postDTO = new PostDTO();
			this.mapper.map(post, postDTO);
			String posterFullName = userService.findById(post.getPosterId()).getFullName();
			postDTO.setPosterFullName(posterFullName);
			allFriendsPosts.add(postDTO);
		}

		HomePageDTO home = new HomePageDTO(navUser, user, allFriendsPosts);

		return home;
	}
	
	@GetMapping("/profile")
	public ProfilePageDTO profilePage(@RequestParam Integer profileId,HttpServletRequest request) throws UnauthorizedException, ElementNotFoundException {
		ProfilePageDTO profile = new ProfilePageDTO();
		if(SessionManager.isLogged(request)) {
			Integer userId = SessionManager.getLoggedUser(request);
			User loggedUser = userService.findById(userId);
			
			NavigationBarDTO navUser = new NavigationBarDTO();
			this.mapper.map(loggedUser, navUser);
			navUser.setFriendRequestsCount(userService.findAllFromRequestsTo(userId).size());
			navUser.setProfilePhotoUrl(userService.findProfilePhotoUrl(userId));
			
			profile.setNavBar(navUser);
		}	
			//TODO logged user addiotional info
			UserSummaryDTO user = new UserSummaryDTO();
			mapper.map(userService.findById(profileId), user);
			user.setProfilePhotoUrl(userService.findProfilePhotoUrl(profileId));
			user.setFriendsCount(userService.getFriendsCountOF(profileId));
			
			List<Post> posts = postsService.findAllByUserId(profileId);
			List<PostDTO> userPosts = new ArrayList<PostDTO>(posts.size());
			for (Post post : posts) {
				PostDTO postDTO = new PostDTO();
				this.mapper.map(post, postDTO);
				String posterFullName = userService.findById(post.getPosterId()).getFullName();
				postDTO.setPosterFullName(posterFullName);
				userPosts.add(postDTO);
			}
			profile.setUser(user);
			profile.setUserPosts(userPosts);
//			profile.setFriendsCount(friendsCount);
			
			return profile;
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
//			dto.setFriendsCount(userService.getFriendsCountOF(user.getId()));
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

		if (content.getContent().isEmpty()) {
			throw new ElementNotFoundException("Cannot update post with empty content!");
		}
		
		Integer userId = SessionManager.getLoggedUser(request);
		Post post = postsService.findById(postId);
		
		if (!post.getPosterId().equals(userId)) {
			throw new UnauthorizedException("Cannot update someone else's post!");
		}
		post.setContent(content.getContent());
		postsService.save(post);
	}

}
