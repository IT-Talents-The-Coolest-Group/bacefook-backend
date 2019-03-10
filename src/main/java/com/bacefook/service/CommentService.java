package com.bacefook.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bacefook.dao.CommentDAO;
import com.bacefook.dto.CommentContentDTO;
import com.bacefook.dto.CommentDTO;
import com.bacefook.exception.ElementNotFoundException;
import com.bacefook.model.Comment;
import com.bacefook.model.User;
import com.bacefook.repository.CommentsRepository;

@Service
public class CommentService {
	
	@Autowired
	private CommentsRepository commentsRepo;
	@Autowired
	private CommentDAO commentDAO;
	@Autowired
	private UserService userService;
	private ModelMapper mapper = new ModelMapper();

	public Integer save(Integer posterId, Integer postId, String content) {
		Comment comment = new Comment(posterId, postId, content, LocalDateTime.now());
		return commentsRepo.save(comment).getId();
	}
	public Integer update(Integer commentId,String content) throws ElementNotFoundException {
		Comment comment = this.findById(commentId);
		comment.setContent(content);
		return commentsRepo.save(comment).getId();
	}
	
	public int deleteComment(Integer id) {
		return commentDAO.deleteCommentById(id);
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
	
	public List<Comment> findAllByUserId(Integer userId){
		return commentsRepo.findAllByPosterId(userId);
	}
	
	public List<CommentDTO> findAllRepliesTo(Integer commentId) throws ElementNotFoundException {
		List<Comment> commentReplies = commentsRepo.findAllByCommentedOnIdOrderByPostingTime(commentId);
		List<CommentDTO> replies = new ArrayList<>(commentReplies.size());
		for (Comment comment : commentReplies) {
			String posterFullName = userService.findById(comment.getPosterId()).getFullName();
			CommentDTO dto = new CommentDTO();
			this.mapper.map(comment, dto);
			dto.setPosterFullName(posterFullName);
			dto.setComentedOnId(comment.getCommentedOnId());
			replies.add(dto);
		}
		return replies;
	}
	
	public void replyTo(User user, Integer commentId, CommentContentDTO commentContentDto) 
			throws ElementNotFoundException {
		Comment reply = new Comment(user.getId(), findById(commentId).getPostId(), 
				commentContentDto.getContent(), LocalDateTime.now());
		reply.setCommentedOnId(commentId);
		commentsRepo.save(reply);
	}

}
