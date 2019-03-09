package com.bacefook.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bacefook.dao.PostDAO;
import com.bacefook.dto.PostContentDTO;
import com.bacefook.exception.ElementNotFoundException;
import com.bacefook.model.Post;
import com.bacefook.model.PostLike;
import com.bacefook.model.User;
import com.bacefook.repository.PostLikesRepository;
import com.bacefook.repository.PostsRepository;
import com.bacefook.repository.UsersRepository;

@Service
public class PostService {

	@Autowired
	private PostsRepository postsRepo;
	@Autowired
	private PostLikesRepository postLikesRepo;
	@Autowired
	private UsersRepository usersRepo;
	@Autowired
	private PostDAO postDao;

	public List<Post> findAllPostsFromFriends(Integer loggerId) {//TODO should we return postDTO
		List<Integer> postIds = postDao.getAllPostsIdByFriends(loggerId);
		List<Post> posts = new ArrayList<>();
		for (Integer id : postIds) {
			Optional<Post> post = postsRepo.findById(id);
			if (post.isPresent()) {
				posts.add(post.get());
			}
		}
		return posts;
	}

	public List<Post> findAllByUser(User poster) {
		return postsRepo.findAllByPosterIdOrderByPostingTimeDesc(poster.getId());
	}
	
	public List<Post> findAllByUserId(Integer posterId){
		return postsRepo.findAllByPosterIdOrderByPostingTimeDesc(posterId);
	}
	
	public Post save(Post post) {
		return postsRepo.save(post);
	}
	
	public Post findById(Integer postId) throws ElementNotFoundException {
		try {
			Post post = postsRepo.findById(postId).get();
			return post;
		} catch (NoSuchElementException e) {
			throw new ElementNotFoundException("No such post!");
		}
	}

	public List<User> findAllUsersWhoLikedAPost(Integer postId) {
		List<PostLike> postLikes = postLikesRepo.findAllByPostId(postId);

		List<User> users = new LinkedList<>();

		for (PostLike like : postLikes) {
			Optional<User> optionalUser = usersRepo.findById(like.getUserId());

			if (optionalUser.isPresent()) {
				users.add(optionalUser.get());
			}
		}
		return users;
	}

	public void likePost(Integer userId, Integer postId) {
		postLikesRepo.save(new PostLike(userId, postId));
	}
	
	public List<Post> findAllWhichSharePostId(Integer postId) {
		return postsRepo.findAllBySharesPostId(postId);
	}

	
	public boolean existsById(Integer sharesPostId) {
		return postsRepo.existsById(sharesPostId);
	}

	public Integer save(Integer sharesPostId, int posterId, PostContentDTO postContentDto) 
			throws ElementNotFoundException {
		
		if (!existsById(sharesPostId)) {
			throw new ElementNotFoundException("Cannot share a post that does not exist!");
		}
		
		Post post = new Post(posterId, postContentDto.getContent(), LocalDateTime.now());
		post.setSharesPostId(sharesPostId);
		
		return save(post).getId();
	}

}
