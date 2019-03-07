package com.bacefook.service;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bacefook.dto.PhotoDTO;
import com.bacefook.model.Photo;
import com.bacefook.model.Post;
import com.bacefook.repository.PhotosRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Service
public class PhotoHostingService {

	@Autowired
	private PhotosRepository photosRepo;
	private static final Cloudinary cloudinary = new Cloudinary("cloudinary://763529519438114:rCTrP8RNpMEiCVzYZNnZlVx5sxw@bacefook");
	
	@Transactional
	public PhotoDTO save(File file, Integer userId, String postContent) throws IOException {
		@SuppressWarnings("rawtypes")
		Map response = cloudinary.uploader().upload(file, ObjectUtils.asMap("public_id", file.getName()));
		String url = (String) response.get("url");
		
		Post post = new Post();
		
		//Photo photo = photosRepo.save(new Photo(postId, url));
		// TODO convert to dto
		return null;
	}
	
}
