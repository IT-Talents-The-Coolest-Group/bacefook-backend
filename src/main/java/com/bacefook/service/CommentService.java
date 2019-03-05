package com.bacefook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bacefook.model.Comment;
import com.bacefook.repository.CommentsRepository;

@Service
public class CommentService {
	
	@Autowired
	private CommentsRepository commentsRepo;
	
	public void saveComment(Comment comment) {
		commentsRepo.save(comment);
	}
	
}
