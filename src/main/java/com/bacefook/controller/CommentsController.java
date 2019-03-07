package com.bacefook.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bacefook.dao.CommentDAO;
import com.bacefook.dto.CommentContentDTO;
import com.bacefook.dto.CommentDTO;
import com.bacefook.exception.ElementNotFoundException;
import com.bacefook.exception.UnauthorizedException;
import com.bacefook.model.Comment;
import com.bacefook.model.User;
import com.bacefook.service.CommentService;
import com.bacefook.service.PostService;
import com.bacefook.service.UserService;
import com.bacefook.utility.TimeConverter;

@CrossOrigin
@RestController
public class CommentsController {

	@Autowired
	private CommentService commentsService;
	@Autowired
	private UserService userService;
	@Autowired
	private PostService postService;
	@Autowired
	private CommentDAO dao;

	private ModelMapper mapper = new ModelMapper();

	@PostMapping("/commentlikes")
	public String addLikeToComment(@RequestParam("commentId") Integer commentId, HttpServletRequest request)
			throws UnauthorizedException, ElementNotFoundException {
		int userId = SessionManager.getLoggedUser(request);
		commentsService.findById(commentId);
//		commentsService.likeCommentById(userId, commentId);
		dao.addLikeToComment(commentId, userId);
		return "Comment " + commentId + " was liked";
	}

	@PostMapping("/commentreply")
	public CommentDTO addReplyToComment(@RequestParam("commentId") Integer commentId,
			@RequestBody CommentContentDTO commentContentDto, HttpServletRequest request)
			throws UnauthorizedException, ElementNotFoundException {

		// TODO move to service layer
		User user = userService.findById(SessionManager.getLoggedUser(request));
		Comment comment = commentsService.findById(commentId);
		Comment reply = new Comment(null, user.getId(), comment.getPostId(), commentId, commentContentDto.getContent(),
				LocalDateTime.now());
		commentsService.save(reply);
		CommentDTO dto = new CommentDTO(reply.getCommentedOnId(), reply.getId(), user.getFullName(), reply.getContent(),
				TimeConverter.convertTimeToString(reply.getPostingTime()));
		return dto;
	}

	@PostMapping("/comments")
	public Integer addCommentToPost(@RequestParam("postId") Integer postId,
			@RequestBody CommentContentDTO commentContentDto, HttpServletRequest request)
			throws UnauthorizedException, ElementNotFoundException {
		postService.findById(postId);
		int posterId = SessionManager.getLoggedUser(request);
		if (commentContentDto.getContent().isEmpty()) {
			throw new ElementNotFoundException("Cannot add comment with empty content!");
		}
		Comment comment = new Comment(posterId, postId, commentContentDto.getContent(), LocalDateTime.now());
		commentsService.save(comment);
		return comment.getId();
	}

	@PutMapping("/comments")
	public void updateComment(@RequestParam("commentId") Integer commentId, @RequestBody CommentContentDTO content,
			HttpServletRequest request) throws UnauthorizedException, ElementNotFoundException {
		if (SessionManager.isLogged(request)) {
			Comment comment = commentsService.findById(commentId);
			if (content.getContent().isEmpty()) {
				throw new ElementNotFoundException("Cannot update comment with empty content!");
			}
			comment.setContent(content.getContent());
			commentsService.save(comment);
		} else {
			throw new UnauthorizedException("You are not logged in! Please log in before trying to update your posts.");
		}
	}

	@GetMapping("/comments")
	public List<CommentDTO> getAllCommentsByPost(@RequestParam("postId") Integer postId)
			throws ElementNotFoundException {
		// checks if post exists
		postService.findById(postId);

		List<Comment> comments = commentsService.findAllByPostId(postId);
		List<CommentDTO> commentsOnPost = new ArrayList<>();

		for (Comment comment : comments) {
			String posterFullName = userService.findById(comment.getPosterId()).getFullName();
			CommentDTO commentDto = new CommentDTO();
			this.mapper.map(comment, commentDto);
			commentDto.setPosterFullName(posterFullName);
			commentsOnPost.add(commentDto);
		}
		return commentsOnPost;

	}

	@GetMapping("/commentreplies")
	public List<CommentDTO> getAllCommentReplies(@RequestParam("commentId") Integer commentId)
			throws ElementNotFoundException {
		commentsService.findById(commentId);

		List<Comment> commentReplies = commentsService.findAllRepliesTo(commentId);
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

}
