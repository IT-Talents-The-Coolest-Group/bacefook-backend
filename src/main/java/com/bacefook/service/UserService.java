package com.bacefook.service;

import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bacefook.dao.ProfilePhotoDAO;
import com.bacefook.dao.UserDAO;
import com.bacefook.dto.SignUpDTO;
import com.bacefook.dto.UserInfoDTO;
import com.bacefook.dto.UserSummaryDTO;
import com.bacefook.exception.ElementNotFoundException;
import com.bacefook.exception.InvalidUserCredentialsException;
import com.bacefook.model.User;
import com.bacefook.model.UserInfo;
import com.bacefook.repository.GenderRepository;
import com.bacefook.repository.UsersInfoRepository;
import com.bacefook.repository.UsersRepository;
import com.bacefook.security.Cryptography;

@Service
public class UserService {

	private static final int MIN_PHONE_LENGTH = 5;
	@Autowired
	private UsersRepository usersRepo;
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private GenderRepository genderService;
	@Autowired
	private UsersInfoRepository usersInfoRepo;
	@Autowired
	private PhotoService photoService;
	@Autowired
	private ProfilePhotoDAO profilePhotoDAO;
	@Autowired
	private RelationService relationService;
	private ModelMapper mapper = new ModelMapper();
	

	public String findProfilePhotoUrl(Integer userId) throws ElementNotFoundException {
		List<String> url = profilePhotoDAO.findProfilePhotoUrl(userId);
		if (url.isEmpty()) {
			throw new ElementNotFoundException("No profile picture for this user!");
		}
		return url.get(0);
	}

	public User findByEmail(String email) throws ElementNotFoundException {
		User user = usersRepo.findByEmail(email);
		if (user == null) {
			throw new ElementNotFoundException("A user with that email does not exist!");
		}
		return user;
	}

	public boolean emailIsTaken(String email) {
		return usersRepo.findByEmail(email) != null;
	}

	public User findById(Integer id) throws ElementNotFoundException {
		try {
			User user = usersRepo.findById(id).get();
			return user;
		} catch (NoSuchElementException e) {
			throw new ElementNotFoundException("A user with that ID does not exist!", e);
		}
	}

	public UserInfo save(UserInfoDTO userInfoDto, Integer userId) {
		UserInfo info = new UserInfo();
		this.mapper.map(userInfoDto, info);
		info.setId(userId);
		usersInfoRepo.save(info);
		return info;
	}

	/**
	 * find search matches show them by Profile picture, Full name and Friends count
	 **/
	public List<UserSummaryDTO> searchByNameOrderedAndLimited(String search, Integer userId) {
		List<Integer> ids = userDAO.getAllSearchingMatchesOrderedByIfFriend(userId, search);
		List<UserSummaryDTO> usersDTO = new LinkedList<UserSummaryDTO>();
		for (Integer integer : ids) {
			Optional<User> user = usersRepo.findById(integer);
			if (user.isPresent()) {
				UserSummaryDTO dto = new UserSummaryDTO();
				this.mapper.map(user.get(), dto);
				dto.setFriendsCount(relationService.getFriendsCountOF(user.get().getId()));
				// TODO maybe set photo URL
				usersDTO.add(dto);
			}
		}
		return usersDTO;
	}

	public String changePassword(int userId, String oldPassword, String newPassword)
			throws ElementNotFoundException, NoSuchAlgorithmException, InvalidUserCredentialsException {
		User user = findById(userId);
		String oldPass = Cryptography.cryptSHA256(oldPassword);
		// TODO implement safer equals
		if (user.getPassword().equals(oldPass)) {
			user.setPassword(Cryptography.cryptSHA256(newPassword));
			usersRepo.save(user);
			return "Password successfylly changed!";
		} else {
			throw new InvalidUserCredentialsException("Incorrect password!");
		}
	}

	public User save(SignUpDTO signUp) throws NoSuchAlgorithmException, ElementNotFoundException {
		User user = new User();
		this.mapper.map(signUp, user);
		user.setPassword(Cryptography.cryptSHA256(signUp.getPassword()));
		user.setGenderId(genderService.findByGenderName(signUp.getGender()).getId());
		usersRepo.save(user);
		return user;
	}

	public UserInfo save(UserInfo info) throws ElementNotFoundException {
		Optional<User> user = usersRepo.findById(info.getId());
		if (!user.isPresent()) {
			throw new ElementNotFoundException("No such user! Register before you can setup your profile.");
		}
		if (info.getProfilePhotoId() != null
				&& !photoService.getIfUserHasPhotoById(info.getId(), info.getProfilePhotoId())) {
			throw new ElementNotFoundException("You are not the owner of this photo!");
		}
		if (info.getCoverPhotoId() != null
				&& !photoService.getIfUserHasPhotoById(info.getId(), info.getCoverPhotoId())) {
			throw new ElementNotFoundException("You are not the owner of this photo!");
		}
		return usersInfoRepo.save(info);
	}

	public UserInfoDTO getInfoByUserId(Integer userId) throws ElementNotFoundException {
		if (userId == null) {
			throw new ElementNotFoundException("User id must not be null!");
		}
		Optional<UserInfo> info = usersInfoRepo.findById(userId);
		if (!info.isPresent()) {
			throw new ElementNotFoundException("No additional info for this user!");
		}
		UserInfo userInfo = info.get();
		UserInfoDTO dto = new UserInfoDTO();
		this.mapper.map(userInfo, dto);
		return dto;
	}
	public UserInfo findUserInfo(Integer userId) throws ElementNotFoundException {
		if (userId == null) {
			throw new ElementNotFoundException("User id must not be null!");
		}
		Optional<UserInfo> info = usersInfoRepo.findById(userId);
		if (!info.isPresent()) {
			throw new ElementNotFoundException("No additional info for this user!");
		}
		return info.get();
		
	}

	public UserInfo getInfoByPhone(String phone) throws InvalidUserCredentialsException {
		if(phone==null || phone.trim().length()<MIN_PHONE_LENGTH) {
			throw new InvalidUserCredentialsException("Invalid phone number");
		}
		return usersInfoRepo.findByPhone(phone);
	}

	

}
