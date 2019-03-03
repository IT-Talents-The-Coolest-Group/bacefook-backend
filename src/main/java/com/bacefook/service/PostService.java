package com.bacefook.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bacefook.model.Post;
import com.bacefook.model.User;
import com.bacefook.repository.PostsRepository;

@Service
public class PostService {
	@Autowired
	private PostsRepository postsRepo;
	
	public List<Post> findAllPostsByUser(User poster) {
		return postsRepo.findAllByUserId(poster.getId());
	}
	
	public void savePost(Post post) {
		postsRepo.save(post);
	}
}
