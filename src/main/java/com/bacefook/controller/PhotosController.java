package com.bacefook.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bacefook.service.PhotoHostingService;

@RestController
public class PhotosController {

	@Autowired
	private PhotoHostingService photoService;

	@PostMapping("{postId}/attachphoto")
	public ResponseEntity<String> uploadPhoto(@PathVariable Integer postId, @RequestParam MultipartFile input) {

		try {
			File file = Files.createTempFile("temp", input.getOriginalFilename()).toFile();
			input.transferTo(file);

			String imageUrl = photoService.savePhoto(postId, file);
			return new ResponseEntity<String>((String) imageUrl, HttpStatus.OK);
		} 
		catch (IOException e) {
			return new ResponseEntity<String>("Could not your process image, sorry!", HttpStatus.BAD_REQUEST);
		}
	}
}
