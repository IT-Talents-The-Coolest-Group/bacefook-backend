package com.bacefook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bacefook.model.PostLikes;

@Repository
public interface PostLikesRepository extends JpaRepository<PostLikes, Integer> {
	
	// TODO get likes count by post id
	
	// TODO get users who have liked a post 
	
	// TODO  like a post
}
