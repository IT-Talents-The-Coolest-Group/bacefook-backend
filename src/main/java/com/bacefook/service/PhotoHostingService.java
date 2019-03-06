package com.bacefook.service;

import java.io.File;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bacefook.repository.PhotosRepository;
import com.cloudinary.Cloudinary;

@Service
public class PhotoHostingService {

	@Autowired
	private PhotosRepository photosRepo;
	
	private static final Cloudinary cloudinary = new Cloudinary("cloudinary://763529519438114:rCTrP8RNpMEiCVzYZNnZlVx5sxw@bacefook");
	
	public Map upload(File file) {
		return null;
	}
	
}
