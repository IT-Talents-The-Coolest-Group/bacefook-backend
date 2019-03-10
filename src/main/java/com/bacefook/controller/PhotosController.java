package com.bacefook.controller;

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
import com.bacefook.service.PhotoService;

//@CrossOrigin(origins = "http://bacefook.herokuapp.com")
@RestController
public class PhotosController {

	@Autowired
	private PhotoService photoService;

	@PostMapping("photos/uploadphoto")
	public PhotoDTO uploadPhoto(@RequestParam MultipartFile input, HttpServletRequest request,
			HttpServletResponse response)
			throws UnprocessableFileException, UnauthorizedException, ElementNotFoundException {

		Integer userId = SessionManager.getLoggedUser(request);
		PhotoDTO photoDto = photoService.save(input, userId);
		return photoDto;
	}

	@PutMapping("/profilephotos/{photoId}")
	public String updateProfilePhoto(@PathVariable Integer photoId, HttpServletRequest request)
			throws UnauthorizedException, ElementNotFoundException {
		Integer userId = SessionManager.getLoggedUser(request);
		if (!photoService.getIfUserHasPhotoById(userId, photoId)) {
			throw new UnauthorizedException("You do not own a photo with that id!");
		}
		photoService.updateProfilePhoto(photoId, userId);
		return "You changed your profile photo successfully!";
	}

	@PutMapping("/coverphotos/{photoId}")
	public void updateCoverPhoto(@PathVariable Integer photoId, HttpServletRequest request)
			throws UnauthorizedException, ElementNotFoundException {
		Integer userId = SessionManager.getLoggedUser(request);
		if (!photoService.getIfUserHasPhotoById(userId, photoId)) {
			throw new UnauthorizedException("You do not own a photo with that id!");
		}
		photoService.updateCoverPhoto(photoId, userId);
	}

	@GetMapping("/users/{userId}/photos")
	public List<PhotoDTO> getAllPhotosOfUser(@PathVariable Integer userId) {
		return photoService.getAllPhotosOfUser(userId);
	}
}
