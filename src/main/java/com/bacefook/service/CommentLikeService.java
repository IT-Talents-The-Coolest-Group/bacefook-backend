package com.bacefook.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bacefook.dao.CommentDAO;
import com.bacefook.exception.AlreadyContainsException;

@Service
public class CommentLikeService {
	@Autowired
	private CommentDAO commentDAO;
	
	public void addLikeToComment(Integer commentId, Integer userId) throws AlreadyContainsException {
		List<Integer> ids = commentDAO.findCommentLikeByUserIdAndCommentId(userId, commentId);
		if(!ids.isEmpty()) {
			throw new AlreadyContainsException("You have already liked this comment!");
		}
		commentDAO.addLikeToComment(commentId, userId);
	}
	
	public int unlikeAComment(Integer commentId, Integer userId) {
		return commentDAO.unlikeComment(commentId, userId);
	}
}
