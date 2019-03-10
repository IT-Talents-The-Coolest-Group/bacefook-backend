package com.bacefook.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bacefook.model.PostLike;

@Repository
public interface PostLikesRepository extends JpaRepository<PostLike, Integer> {
	
	public List<PostLike> findAllByPostId(Integer postId);
	
	public PostLike findByUserIdAndPostId(Integer userId,Integer postId);
	
}
