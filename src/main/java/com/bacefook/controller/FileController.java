package com.bacefook.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.bacefook.model.UploadFileResponse;
import com.bacefook.service.FileStorageService;


@RestController
public class FileController {
	
//	private static final Logger logger = LoggerFactory.getLogger(FileController.class);
//	
//	 @Autowired
//	 private FileStorageService fileStorageService;
//	 
//	 @PostMapping("/uploadfile")
//	    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file) {
//	        String fileName = fileStorageService.storeFile(file);
//
//	        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
//	                .path("/downloadfile/")
//	                .path(fileName)
//	                .toUriString();
//
//	        return new UploadFileResponse(fileName, fileDownloadUri,
//	                file.getContentType(), file.getSize());
//	    }
//
//	public static Logger getLogger() {
//		return logger;
//	}
//	
}
