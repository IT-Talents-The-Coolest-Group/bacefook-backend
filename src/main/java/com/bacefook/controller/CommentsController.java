package com.bacefook.controller;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bacefook.dto.CommentContentDTO;
import com.bacefook.exception.UnauthorizedException;
import com.bacefook.model.Comment;
import com.bacefook.service.CommentService;

@RestController
public class CommentsController {

	CommentService commentsService;

	@PostMapping("/comments")
	public ResponseEntity<Object> addCommentToPost(@RequestParam("postId") Integer postId,
			@RequestBody CommentContentDTO commentContentDto, HttpServletRequest request) throws UnauthorizedException {
		int posterId = SessionManager.getLoggedUser(request).getId();//throw exception if no user in session
		// TODO validate if properties are not empty
		Comment comment = new Comment(posterId, postId, commentContentDto.getContent(), LocalDateTime.now());

		commentsService.saveComment(comment);// TODO validate with status code
		return new ResponseEntity<>(comment.getId(), HttpStatus.OK);
	}
}
