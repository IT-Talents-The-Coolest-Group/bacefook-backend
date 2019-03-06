package com.bacefook.service;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bacefook.model.Photo;
import com.bacefook.repository.PhotosRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Service
public class PhotoHostingService {

	@Autowired
	private PhotosRepository photosRepo;
	private static final Cloudinary cloudinary = new Cloudinary("cloudinary://763529519438114:rCTrP8RNpMEiCVzYZNnZlVx5sxw@bacefook");
	
	public String save(Integer postId, File file) throws IOException {
		@SuppressWarnings("rawtypes")
		Map response = cloudinary.uploader().upload(file, ObjectUtils.asMap("public_id", file.getName()));
		String url = (String) response.get("url");
		
		Photo photo = new Photo(postId, url);
		photosRepo.save(photo);
		return url;
	}
	
}
