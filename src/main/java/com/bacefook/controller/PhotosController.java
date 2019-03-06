package com.bacefook.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bacefook.service.PhotoHostingService;

@RestController
public class PhotosController {

	@Autowired
	private PhotoHostingService photoService;

	@PostMapping("uploadphoto")
	public ResponseEntity<String> uploadPhoto(@RequestParam MultipartFile file) {

		try {
			File f = Files.createTempFile("temp", file.getOriginalFilename()).toFile();
			file.transferTo(f);

			@SuppressWarnings("rawtypes")
			Map response = photoService.upload(f);
			return new ResponseEntity<String>((String) response.get("url"), HttpStatus.OK);
		} 
		catch (IOException e) {
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
	}
}
