package com.bacefook.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import com.bacefook.dto.UserInfoDTO;
import com.bacefook.exception.AlreadyContainsException;
import com.bacefook.exception.ElementNotFoundException;
import com.bacefook.exception.UnauthorizedException;
import com.bacefook.model.Post;
import com.bacefook.model.User;
import com.bacefook.service.PostLikesService;
import com.bacefook.service.PostService;
import com.bacefook.service.UserService;

@CrossOrigin(origins = "http://bacefook.herokuapp.com")
@RestController
public class PostsController {

	@Autowired
	private PostService postsService;
	@Autowired
	private PostLikesService postsLikeService;
	@Autowired
	private UserService userService;
	private ModelMapper mapper = new ModelMapper();

	/**
	 * retrieves info for home page and navigation bar
	 ***/
	@GetMapping("/home")
	public HomePageDTO homePage(HttpServletRequest request) throws UnauthorizedException, ElementNotFoundException {

		Integer userId = SessionManager.getLoggedUser(request);
		User loggedUser = userService.findById(userId);

		NavigationBarDTO navUser = new NavigationBarDTO();
		this.mapper.map(loggedUser, navUser);
		navUser.setFriendRequestsCount(userService.findAllFromRequestsTo(userId).size());
		navUser.setProfilePhotoUrl(userService.findProfilePhotoUrl(userId));

		UserSummaryDTO user = new UserSummaryDTO();
		mapper.map(loggedUser, user);
//		user.setFriendsCount(userService.getFriendsCountOF(userId));
		List<PostDTO> posts = postsService.findAllPostsFromFriends(userId);
		HomePageDTO home = new HomePageDTO(navUser, user, posts);
		return home;
	}

	@GetMapping("/profile")
	public ProfilePageDTO profilePage(@RequestParam Integer profileId, HttpServletRequest request)
			throws UnauthorizedException, ElementNotFoundException {
		ProfilePageDTO profile = new ProfilePageDTO();
		if (SessionManager.isLogged(request)) {
			Integer userId = SessionManager.getLoggedUser(request);
			User loggedUser = userService.findById(userId);

			NavigationBarDTO navUser = new NavigationBarDTO();
			this.mapper.map(loggedUser, navUser);
			navUser.setFriendRequestsCount(userService.findAllFromRequestsTo(userId).size());
			navUser.setProfilePhotoUrl(userService.findProfilePhotoUrl(userId));
			UserInfoDTO info = userService.getInfoByUserId(userId);

			profile.setNavBar(navUser);
			profile.setUserInfo(info);
		}
		UserSummaryDTO user = new UserSummaryDTO();
		mapper.map(userService.findById(profileId), user);
		user.setProfilePhotoUrl(userService.findProfilePhotoUrl(profileId));
		user.setFriendsCount(userService.getFriendsCountOF(profileId));

		List<PostDTO> posts = postsService.findAllByUserId(profileId);
//			for (Post post : posts) {
//				PostDTO postDTO = new PostDTO();
//				this.mapper.map(post, postDTO);
//				String posterFullName = userService.findById(post.getPosterId()).getFullName();
//				postDTO.setPosterFullName(posterFullName);
//				userPosts.add(postDTO);
//			}
		profile.setUser(user);
		profile.setUserPosts(posts);
//			profile.setFriendsCount(friendsCount);

		return profile;
	}

	@PostMapping("/postlikes")
	public void likeAPost(@RequestParam("postId") Integer postId, HttpServletRequest request)
			throws UnauthorizedException, AlreadyContainsException {
		int userId = SessionManager.getLoggedUser(request);
		postsLikeService.likePost(userId, postId);
	}

	@GetMapping("/postlikes")
	public List<UserSummaryDTO> getAllUsersWhoLikedPost(@RequestParam("postId") Integer postId) {
		return postsLikeService.findAllUsersWhoLikedAPost(postId);
	}
	@DeleteMapping("/postlikes/unlike")
	public String unlikeAComment(@RequestParam("postId")Integer postId,HttpServletRequest request) throws UnauthorizedException {
		int userId = SessionManager.getLoggedUser(request);
		int rows = postsLikeService.unlikeAPost(postId, userId);
		if(rows>0) {
			return "Post was unliked!";
		}else {
			return "Could not unlike post.";
		}
	}

	@GetMapping("/postlikes-size")
	public Integer getLikesCountOnPost(@RequestParam("postId") Integer postId) {
		return postsLikeService.findAllUsersWhoLikedAPost(postId).size();
	}

	@PostMapping("/posts")
	public Integer createPost(@RequestBody PostContentDTO contentDto, HttpServletRequest request)
			throws UnauthorizedException, ElementNotFoundException {
		int posterId = SessionManager.getLoggedUser(request);
		String content = contentDto.getContent();
		if (content == null || content.isEmpty()) {
			throw new ElementNotFoundException("Write something before posting!");
		}
		return postsService.save(posterId, content);
	}

	@DeleteMapping("/posts/delete")
	public String deletePostById(@RequestParam("postId") Integer id, HttpServletRequest request)
			throws UnauthorizedException, ElementNotFoundException {
		int userId = SessionManager.getLoggedUser(request);
		if (!postsService.isPostedByUserId(userId, postsService.findById(id))) {
			throw new ElementNotFoundException("User have no rights for this post!");
		}
		postsService.deletePost(id);
		return "Post was deleted";
	}

	@PostMapping("/postshares")
	public Integer sharePost(@RequestParam("sharesPostId") Integer sharesPostId,
			@RequestBody PostContentDTO postContentDto, HttpServletRequest request)
			throws UnauthorizedException, ElementNotFoundException {
		int posterId = SessionManager.getLoggedUser(request);
		return postsService.saveSharing(sharesPostId, posterId, postContentDto);
	}

	@GetMapping("/postshares")
	public List<PostDTO> getAllPostShares(@RequestParam("postId") Integer postId){
		return postsService.findAllWhichSharePostId(postId);
	}

	@GetMapping("/posts")
	public List<PostDTO> getAllPostsOfUser(@RequestParam("posterId") int posterId) throws ElementNotFoundException {
		return postsService.findAllByUserId(posterId);
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
		postsService.update(post);
	}

}
