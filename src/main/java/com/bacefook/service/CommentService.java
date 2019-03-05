package com.bacefook.service;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bacefook.exception.ElementNotFoundException;
import com.bacefook.model.Comment;
import com.bacefook.repository.CommentsRepository;

@Service
public class CommentService {
	
	@Autowired
	private CommentsRepository commentsRepo;
	
	public void saveComment(Comment comment) {
		commentsRepo.save(comment);
	}
	public Comment findCommentById(Integer commentId) throws ElementNotFoundException {
		//TODO Global Handling
		//TODO commentId!=null
		try {
		Comment comment = commentsRepo.findById(commentId).get();
		return comment;
		}catch(NoSuchElementException e) {
			throw new ElementNotFoundException("No such comment!");
		}
	}
	
}
