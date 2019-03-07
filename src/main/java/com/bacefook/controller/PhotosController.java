package com.bacefook.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bacefook.dto.PhotoDTO;
import com.bacefook.exception.UnauthorizedException;
import com.bacefook.exception.UnprocessableFileException;
import com.bacefook.service.PhotoHostingService;

@RestController
public class PhotosController {

	@Autowired
	private PhotoHostingService photoService;

	@PostMapping("uploadphoto")
	public PhotoDTO uploadPhoto(@RequestParam MultipartFile input, HttpServletRequest request,HttpServletResponse response, @RequestParam String postContent)
		throws UnprocessableFileException, UnauthorizedException {

		Integer userId = SessionManager.getLoggedUser(request);
		
		try {
			// TODO fix file name (remove extension and "temp")
			File file = Files.createTempFile("temp", input.getOriginalFilename()).toFile();
			input.transferTo(file);
			PhotoDTO photoDto = photoService.save(file, userId, postContent);
			response.sendRedirect("https://res.cloudinary.com/bacefook/image/upload/v1551888321/8603_baaaaby.jpg.jpg");
			return photoDto;
		}catch (IOException e) {
			throw new UnprocessableFileException("Could not your process image, sorry!");
		}
	}
	
	// TODO update profile photo
	
	// TODO update cover photo
	
	// TODO get all photos by album name
}
