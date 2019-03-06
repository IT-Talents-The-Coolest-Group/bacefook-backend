package com.bacefook.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bacefook.model.CommentLike;

public interface CommentLikesRepository extends JpaRepository<CommentLike, Integer> {

	
}
