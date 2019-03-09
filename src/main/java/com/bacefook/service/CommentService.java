package com.bacefook.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bacefook.dto.CommentContentDTO;
import com.bacefook.exception.ElementNotFoundException;
import com.bacefook.model.Comment;
import com.bacefook.model.User;
import com.bacefook.repository.CommentsRepository;

@Service
public class CommentService {
	
	@Autowired
	private CommentsRepository commentsRepo;
	private ModelMapper mapper = new ModelMapper();

	public Comment save(Comment comment) {
		return commentsRepo.save(comment);
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

	
	public void replyTo(User user, Integer commentId, CommentContentDTO commentContentDto) 
			throws ElementNotFoundException {
		
		Comment reply = new Comment(user.getId(), findById(commentId).getPostId(), 
				commentContentDto.getContent(), LocalDateTime.now());
		
		reply.setCommentedOnId(commentId);
		reply = save(reply);
		
	}

}
