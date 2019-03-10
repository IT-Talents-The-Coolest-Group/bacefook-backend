package com.bacefook.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bacefook.dao.PostDAO;
import com.bacefook.dao.ProfilePhotoDAO;
import com.bacefook.dao.UserDAO;
import com.bacefook.dto.UserSummaryDTO;
import com.bacefook.exception.AlreadyContainsException;
import com.bacefook.model.PostLike;
import com.bacefook.model.User;
import com.bacefook.repository.PostLikesRepository;
import com.bacefook.repository.UsersRepository;

@Service
public class PostLikesService {
	@Autowired
	private PostLikesRepository postLikesRepo;
	@Autowired
	private PostDAO postDao;
	@Autowired
	private UsersRepository usersRepo;
	@Autowired
	private UserDAO userDao;
	@Autowired
	private ProfilePhotoDAO profilePhotoDao;
	ModelMapper mapper = new ModelMapper();

	/**
	 * add row to post_likes table
	 * @throws AlreadyContainsException 
	 **/
	public void likePost(Integer userId, Integer postId) throws AlreadyContainsException {
		PostLike like = postLikesRepo.findByUserIdAndPostId(userId, postId);
		if(like!=null) {
			throw new AlreadyContainsException("You have already liked this post!");
		}
		postLikesRepo.save(new PostLike(userId, postId));
	}
	public int unlikeAPost(Integer postId, Integer userId) {
		return postDao.unlikePost(postId, userId);
	}

	/**
	 * get all user who liked post with id, firstName, lastName, friendsCount and
	 * profilePhotoUrl
	 **/
	public List<UserSummaryDTO> findAllUsersWhoLikedAPost(Integer postId) {
		List<PostLike> postLikes = postLikesRepo.findAllByPostId(postId);
		List<UserSummaryDTO> dtos = new ArrayList<>();
		for (PostLike like : postLikes) {
			Optional<User> optionalUser = usersRepo.findById(like.getUserId());
			if (optionalUser.isPresent()) {
				UserSummaryDTO dto = new UserSummaryDTO();
				this.mapper.map(optionalUser.get(), dto);
				dto.setProfilePhotoUrl(profilePhotoDao.findProfilePhotoUrl(optionalUser.get().getId()).get(0));
				dto.setFriendsCount(userDao.findAllFriendsOf(optionalUser.get().getId()).size());
				dtos.add(dto);
			}
		}
		return dtos;
	}

}
