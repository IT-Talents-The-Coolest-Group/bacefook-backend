package com.bacefook.service;

import java.util.List;
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
	
	public void save(Comment comment) {
		commentsRepo.save(comment);
	}
	
	public Comment findById(Integer commentId) throws ElementNotFoundException {
		try {
			return commentsRepo.findById(commentId).get();
		}
		catch(NoSuchElementException e) {
			throw new ElementNotFoundException("No such comment!");
		}
	}
	
	public List<Comment> findAllByPostId(Integer postId) {
		return commentsRepo.findAllByPostId(postId);
	}
	
	public List<Comment> findAllRepliesTo(Integer commentId) {
		return commentsRepo.findAllByCommentedOnIdOrderByPostingTime(commentId);
	}

}
