package com.bacefook.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bacefook.dto.PhotoDTO;
import com.bacefook.exception.ElementNotFoundException;
import com.bacefook.exception.UnprocessableFileException;
import com.bacefook.model.Photo;
import com.bacefook.model.Post;
import com.bacefook.model.UserInfo;
import com.bacefook.repository.PhotosRepository;
import com.bacefook.repository.UsersInfoRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Service
public class PhotoService {

	@Autowired
	private PhotosRepository photosRepo;
	@Autowired
	private UsersInfoRepository usersInfoRepo;
	@Autowired
	private PostService postsService;
	@Autowired
	private UserService userService;

	private ModelMapper mapper = new ModelMapper();

	private static final Cloudinary cloudinary = new Cloudinary(
			"cloudinary://763529519438114:rCTrP8RNpMEiCVzYZNnZlVx5sxw@bacefook");

	@Transactional
	public PhotoDTO save(MultipartFile input, Integer userId)
			throws UnprocessableFileException, ElementNotFoundException {
		try {
			File file = Files.createTempFile("", input.getOriginalFilename()).toFile();
			input.transferTo(file);
			@SuppressWarnings("rawtypes")
			Map response = cloudinary.uploader().upload(file, ObjectUtils.asMap("public_id", file.getName()));
			String url = (String) response.get("url");

			int postId = postsService.save(userId, "Photo");
			Post post = postsService.findById(postId);
			Photo photo = photosRepo.save(new Photo(post.getId(), url));

			return new PhotoDTO(photo.getId(), photo.getUrl(), post.getId());
		} catch (IOException e) {
			throw new UnprocessableFileException("Could not your process image, sorry!");
		}
	}

	public void updateProfilePhoto(Integer photoId, Integer userId) throws ElementNotFoundException {
		if (!photosRepo.existsById(photoId)) {
			throw new ElementNotFoundException("Could not update photo, photo not found!");
		}
		UserInfo info = userService.findUserInfo(userId);
		info.setProfilePhotoId(photoId);
		usersInfoRepo.save(info);
	}

	public void updateCoverPhoto(Integer photoId, Integer userId) throws ElementNotFoundException {
		if (!photosRepo.existsById(photoId)) {
			throw new ElementNotFoundException("Could not update photo, photo not found!");
		}

		UserInfo info = userService.findUserInfo(userId);
		info.setCoverPhotoId(photoId);
		usersInfoRepo.save(info);
	}

	public List<PhotoDTO> getAllPhotosOfUser(Integer userId) {
		List<Integer> photoIds = photosRepo.findAllPhotosOfUser(userId);
		List<PhotoDTO> photos = new LinkedList<PhotoDTO>();
		for (Integer integer : photoIds) {
			Optional<Photo> optionalPhoto = photosRepo.findById(integer);
			if (optionalPhoto.isPresent()) {
				Photo photo = optionalPhoto.get();
				PhotoDTO dto = new PhotoDTO();
				this.mapper.map(photo, dto);
				photos.add(dto);
			}
		}

		return photos;
	}

	/**
	 * checks if photo id is posted by user
	 **/
	public boolean getIfUserHasPhotoById(Integer userId, Integer photoId) {
		List<Integer> photoIds = photosRepo.findAllPhotosOfUser(userId);
		return photoIds.contains(photoId);
	}
}
