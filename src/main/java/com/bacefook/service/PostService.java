package com.bacefook.service;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

	public List<Post> findAllPostsByUser(User poster) {
		return postsRepo.findAllByPosterId(poster.getId());
	}
	
	public List<Post> findAllPostsByUserId(Integer posterId){
		return postsRepo.findAllByPosterId(posterId);
	}
	
	public void savePost(Post post) {
		postsRepo.save(post);
	}
	
	public Post findPostById(Integer postId) throws ElementNotFoundException {
		try {
		Post post = postsRepo.findById(postId).get();
		return post;
		}catch(NoSuchElementException e) {
			throw new ElementNotFoundException("No such post!");
		}
	}

	public List<User> getAllUsersWhoLikedAPostById(Integer postId) {
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
	
	public List<Post> getAllPostsWhichSharePostId(Integer postId) {
		return postsRepo.findAllBySharesPostId(postId);
	}
	
	
}
