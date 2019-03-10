package com.bacefook.service;

import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.management.relation.RelationException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bacefook.dao.ProfilePhotoDAO;
import com.bacefook.dao.RelationsDAO;
import com.bacefook.dao.UserDAO;
import com.bacefook.dto.SignUpDTO;
import com.bacefook.dto.UserInfoDTO;
import com.bacefook.dto.UserSummaryDTO;
import com.bacefook.exception.ElementNotFoundException;
import com.bacefook.exception.InvalidUserCredentialsException;
import com.bacefook.model.Relation;
import com.bacefook.model.User;
import com.bacefook.model.UserInfo;
import com.bacefook.repository.GenderRepository;
import com.bacefook.repository.RelationsRepository;
import com.bacefook.repository.UsersInfoRepository;
import com.bacefook.repository.UsersRepository;
import com.bacefook.security.Cryptography;

@Service
public class UserService {

	@Autowired 
	private UsersRepository usersRepo;
	@Autowired 
	private RelationsRepository relationsRepo;
	@Autowired
	private GenderRepository genderService;
	@Autowired
	private UsersInfoRepository usersInfoRepo;
	@Autowired
	private PhotoService photoService;
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private RelationsDAO relationsDAO;
	@Autowired
	private ProfilePhotoDAO profilePhotoDAO;
	private ModelMapper mapper = new ModelMapper();
	
	public String removeFromFriends(Integer loggedId,Integer friendId) {
		int rows = relationsDAO.removeFromFriends(loggedId, friendId);
		if(rows > 0) {
			return "You are no longer friends with " + friendId;
		}else {
			return "Sorry, but you are not friends with such person.";
		}
	}
	
	public String cancelFriendRequest(Integer loggedId,Integer receiverId) {
		int rows = relationsDAO.cancelFriendRequest(loggedId, receiverId);
		if(rows > 0) {
			return "You canceled a friend request to " + receiverId;
		}
		else {
			return "Sorry, but you have not sent a friend request to this user.";
		}
	}
	
	public String deleteFriendRequest(Integer loggedId,Integer senderId) {
		int rows = relationsDAO.cancelFriendRequest(loggedId, senderId);
		if(rows > 0) {
			return "You deleted a friend request from " + senderId;
		}
		else {
			return "Sorry, but you have not received a friend request from this user.";
		}
	}
	
	public String findProfilePhotoUrl(Integer userId) throws ElementNotFoundException {
		List<String> url = profilePhotoDAO.findProfilePhotoUrl(userId);
		if(url.isEmpty()) {
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
		}
		catch (NoSuchElementException e) {
			throw new ElementNotFoundException("A user with that ID does not exist!", e);
		}
	}
	
	public Integer save(User user) {
		return usersRepo.save(user).getId();
	}
	
	public Integer sendFriendRequest(Integer senderId, Integer receiverId) 
			throws RelationException, ElementNotFoundException {
		Relation friendRequest = new Relation(senderId, receiverId, 0);
		if (senderId.equals(receiverId)) {
			throw new RelationException("You cannot send a request to yourself!");
		}
		if (!usersRepo.existsById(senderId) || !usersRepo.existsById(receiverId)) {
			throw new ElementNotFoundException("A user with that ID does not exist!");
		}
		if (relationsRepo.findBySenderIdAndReceiverId(senderId, receiverId) != null) {
			throw new RelationException("You have already sent a request to that person!"); 
		}
		if (userDAO.findAllFriendsOf(senderId).contains(receiverId)) {
			throw new RelationException("You are already friends!");
		}
		return relationsRepo.save(friendRequest).getId();
	}

	public Integer confirmFriendRequest(Integer receiverId, Integer senderId) throws RelationException {
		Relation relation = relationsRepo.findBySenderIdAndReceiverId(senderId, receiverId);
		if (relation == null) {
			throw new RelationException("You do not have a request from that user!");
		}
		relation.setIsConfirmed(1);
		return relationsRepo.save(relation).getId();
	}

	/**
	 * find all friend requests to user
	 * **/
	public List<UserSummaryDTO> findAllFromRequestsTo(Integer userId) {
		List<Integer> userIds = userDAO.findAllRequestsTo(userId);	
		List<UserSummaryDTO> users = new LinkedList<UserSummaryDTO>();
		for (Integer id : userIds) {
			Optional<User> user = usersRepo.findById(id);
			if (user.isPresent()) {
				UserSummaryDTO summary = new UserSummaryDTO();
				mapper.map(user.get(), summary);
				summary.setFriendsCount(getFriendsCountOF(user.get().getId()));
				users.add(summary);
			}
		}
		return users;
	}
	
	/**
	 * find all friends by user
	 * **/
	public List<UserSummaryDTO> findAllFriendOf(Integer userId){
		List<Integer> friendsIds = userDAO.findAllFriendsOf(userId);
		List<UserSummaryDTO> users = new LinkedList<UserSummaryDTO>();
		for (Integer id : friendsIds) {
			Optional<User> user = usersRepo.findById(id);
			if (user.isPresent()) {
				UserSummaryDTO summary = new UserSummaryDTO();
				mapper.map(user.get(), summary);
				summary.setFriendsCount(getFriendsCountOF(user.get().getId()));
				users.add(summary);
			}
		}
		return users;
	}
	
	public int getFriendsCountOF(Integer userId) {
		return userDAO.findAllFriendsOf(userId).size();
	}

	/**
	 * find search matches 
	 * show them by Profile picture, Full name and Friends count
	 * **/
	public List<UserSummaryDTO> searchByNameOrderedAndLimited(String search,Integer userId) {
		List<Integer> ids = userDAO.getAllSearchingMatchesOrderedByIfFriend(userId, search);
		List<UserSummaryDTO> usersDTO = new LinkedList<UserSummaryDTO>();
		for (Integer integer : ids) {
			Optional<User> user = usersRepo.findById(integer);
			if(user.isPresent()) {
			UserSummaryDTO dto = new UserSummaryDTO();
			this.mapper.map(user.get(), dto);
			dto.setFriendsCount(getFriendsCountOF(user.get().getId()));
			//TODO maybe set photo URL
			usersDTO.add(dto);
			}
		}
		return usersDTO;
	}

	public void changePassword(int userId, String oldPassword, String newPassword) 
			throws ElementNotFoundException, NoSuchAlgorithmException, InvalidUserCredentialsException {
		User user = findById(userId);
		String oldPass = Cryptography.cryptSHA256(oldPassword);
		// TODO implement safer equals
		if (user.getPassword().equals(oldPass)) {
			user.setPassword(Cryptography.cryptSHA256(newPassword));
			save(user);
		} 
		else {
			throw new InvalidUserCredentialsException("Incorrect password!");
		}
	}

	public User save(SignUpDTO signUp) 
			throws NoSuchAlgorithmException, ElementNotFoundException {
		User user = new User();
		this.mapper.map(signUp, user);
		user.setPassword(Cryptography.cryptSHA256(signUp.getPassword()));
		user.setGenderId(genderService.findByGenderName(signUp.getGender()).getId());
		save(user);
		return user;
	}

	public UserInfo save(UserInfo info) throws ElementNotFoundException {
		Optional<User> user = usersRepo.findById(info.getId());
		if(!user.isPresent()) {
			throw new ElementNotFoundException("No such user! Register before you can setup your profile.");
		}
		if(info.getProfilePhotoId()!=null && !photoService.getIfUserHasPhotoById(info.getId(), info.getProfilePhotoId())) {
			throw new ElementNotFoundException("You are not the owner of this photo!");
		}
		if(info.getCoverPhotoId()!=null && !photoService.getIfUserHasPhotoById(info.getId(), info.getCoverPhotoId())) {
			throw new ElementNotFoundException("You are not the owner of this photo!");
		}
		return usersInfoRepo.save(info);
	}
	
	public UserInfoDTO getInfoByUserId(Integer userId) throws ElementNotFoundException {
		if(userId==null) {
			throw new ElementNotFoundException("User id must not be null!");
		}
		Optional<UserInfo> info =usersInfoRepo.findById(userId);
		if(!info.isPresent()) {
			throw new ElementNotFoundException("No additional info for this user!");
		}
		UserInfo userInfo = info.get();
		UserInfoDTO dto = new UserInfoDTO();
		this.mapper.map(userInfo, dto);
		return dto;
	}
	//TODO concat 
	public UserInfo findUserInfo(Integer userId) {
		UserInfo info = null;
		Optional<UserInfo> optionalInfo = usersInfoRepo.findById(userId);
		if (optionalInfo.isPresent()) {
			info = optionalInfo.get();
		}
		else {
			info = new UserInfo();
			info.setId(userId);
		}
		return info;
	}

}
