package com.bacefook.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bacefook.dto.PhotoDTO;
import com.bacefook.exception.ElementNotFoundException;
import com.bacefook.exception.UnauthorizedException;
import com.bacefook.exception.UnprocessableFileException;
import com.bacefook.model.Photo;
import com.bacefook.service.PhotoService;

@RestController
public class PhotosController {

	@Autowired
	private PhotoService photoService;

	@PostMapping("uploadphoto")
	public PhotoDTO uploadPhoto(@RequestParam MultipartFile input, HttpServletRequest request, HttpServletResponse response)
					throws UnprocessableFileException, UnauthorizedException {

		Integer userId = SessionManager.getLoggedUser(request);

		PhotoDTO photoDto = photoService.save(input, userId);
		
		try {
			response.sendRedirect(photoDto.getUrl());
		} 
		catch (IOException e) {
			throw new UnprocessableFileException("Could not open the photo, invalid url was generated..", e);
		}
		return photoDto;
	}
	
	@PutMapping("profilephoto/{photoId}")
	public void updateProfilePhoto(@PathVariable Integer photoId, HttpServletRequest request) 
			throws UnauthorizedException, ElementNotFoundException {
		
		Integer userId = SessionManager.getLoggedUser(request);
		
		photoService.updateProfilePhoto(photoId, userId);
	}
	
	@PutMapping("coverphoto/{photoId}")
	public void updateCoverPhoto(@PathVariable Integer photoId, HttpServletRequest request) 
			throws UnauthorizedException, ElementNotFoundException {
		
		Integer userId = SessionManager.getLoggedUser(request);
		
		photoService.updateCoverPhoto(photoId, userId);
	}
	
	@GetMapping("user/{userId}/photos")
	public List<Photo> getAllPhotosOfUser(@PathVariable Integer userId) {
		return photoService.getAllPhotosOfUser(userId);
	}
	
	
}
