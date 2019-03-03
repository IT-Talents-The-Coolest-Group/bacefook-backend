package com.bacefook.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bacefook.model.Post;

@Repository
public interface PostsRepository extends JpaRepository<Post, Integer> {
	public List<Post> findAllByUserId(Integer posterId);
}
