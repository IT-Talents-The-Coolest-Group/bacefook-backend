package com.bacefook.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bacefook.exception.PostNotFoundException;
import com.bacefook.model.Post;
import com.bacefook.model.User;
import com.bacefook.repository.PostsRepository;

@Service
public class PostService {
	@Autowired
	private PostsRepository postsRepo;
	
	public List<Post> findAllPostsByUser(User poster) {
		return postsRepo.findAllByPosterId(poster.getId());
	}
	
	public List<Post> findAllPostsByUserId(Integer posterId){
		return postsRepo.findAllByPosterId(posterId);
	}
	
	public void savePost(Post post) {
		postsRepo.save(post);
	}
	
	public Post findPostById(Integer postId) throws PostNotFoundException { //TODO Global Handling
		try {
		Post post = postsRepo.findById(postId).get();
		return post;
		}catch(NoSuchElementException e) {
			throw new PostNotFoundException("No such post!");
		}
	}
}
