package com.bacefook.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

	@PostMapping("/commentlikes")
	public ResponseEntity<String> addLikeToComment(@RequestParam("commentId") Integer commentId,
			HttpServletRequest request) throws UnauthorizedException, ElementNotFoundException {
		int userId = SessionManager.getLoggedUser(request).getId();
		commentsService.findById(commentId);
//		commentsService.likeCommentById(userId, commentId);
		dao.addLikeToComment(commentId, userId);
		return new ResponseEntity<String>("Comment " + commentId + " was liked", HttpStatus.OK);
	}

	@PostMapping("/commentreply")
	public ResponseEntity<CommentDTO> addReplyToComment(@RequestParam("commentId") Integer commentId,
			@RequestBody CommentContentDTO commentContentDto, HttpServletRequest request)
			throws UnauthorizedException, ElementNotFoundException {
		User user = SessionManager.getLoggedUser(request);
		Comment comment = commentsService.findById(commentId);
		Comment reply = new Comment(null, user.getId(), comment.getPostId(), commentId, commentContentDto.getContent(),
				LocalDateTime.now());
		commentsService.save(reply);
		CommentDTO dto = new CommentDTO(reply.getId(), user.getFullName(), reply.getCommentedOnId(), reply.getContent(),
				TimeConverter.convertTimeToString(reply.getPostingTime()));
		return new ResponseEntity<CommentDTO>(dto, HttpStatus.OK);
	}

	@PostMapping("/comments")
	public ResponseEntity<Integer> addCommentToPost(@RequestParam("postId") Integer postId,
			@RequestBody CommentContentDTO commentContentDto, HttpServletRequest request) throws UnauthorizedException {
		// throw exception if no user in session
		// TODO check if comment is a reply on another comment
		// TODO validate if properties are not empty

		int posterId = SessionManager.getLoggedUser(request).getId();
		Comment comment = new Comment(posterId, postId, commentContentDto.getContent(), LocalDateTime.now());

		// TODO validate with status code
		commentsService.save(comment);
		return new ResponseEntity<>(comment.getId(), HttpStatus.OK);
	}

	@PutMapping("/comments")
	public ResponseEntity<Object> updateComment(@RequestParam("commentId") Integer commentId, @RequestBody CommentContentDTO content,
			HttpServletRequest request) throws UnauthorizedException, ElementNotFoundException {
		
		if (SessionManager.isLogged(request)) {
			System.out.println(content);
			Comment comment = commentsService.findById(commentId);

			if (content.getContent().isEmpty()) {
				throw new ElementNotFoundException("Cannot update comment with empty content!");
			}
			comment.setContent(content.getContent());
			commentsService.save(comment);
			
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			throw new UnauthorizedException("You are not logged in! Please log in before trying to update your posts.");
		}
	}

	@GetMapping("/comments")
	public ResponseEntity<List<CommentDTO>> getAllCommentsByPost(@RequestParam("postId") Integer postId,
			HttpServletRequest request) throws ElementNotFoundException {

		// TODO check if user is friend with poster
//		SessionManager.getLoggedUser(request);
		postService.findById(postId);

		List<Comment> comments = commentsService.findAllByPostId(postId);
		List<CommentDTO> commentsOnPost = new ArrayList<>();

		for (Comment comment : comments) {
			String posterFullName = userService.findById(comment.getPosterId()).getFullName();

			String timeOfPosting = TimeConverter.convertTimeToString(comment.getPostingTime());

			CommentDTO commentDto = new CommentDTO(comment.getId(), posterFullName, comment.getCommentedOnId(),
					comment.getContent(), timeOfPosting);

			commentsOnPost.add(commentDto);
		}
		return new ResponseEntity<>(commentsOnPost, HttpStatus.OK);

	}

}
