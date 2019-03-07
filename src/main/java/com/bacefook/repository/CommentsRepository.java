package com.bacefook.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bacefook.model.Comment;

@Repository
public interface CommentsRepository extends JpaRepository<Comment, Integer>{

	public List<Comment> findAllByPostId(Integer posterId);
	
	// TODO order by 
	public List<Comment> findAllByCommentedOnId(Integer commentedOnId);
	
}
