package com.bacefook.service;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.management.relation.RelationException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bacefook.dao.UserDAO;
import com.bacefook.dto.SignUpDTO;
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
	private UserDAO userDao;
	private ModelMapper mapper = new ModelMapper();
	
	
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
	
	public void sendFriendRequest(Integer senderId, Integer receiverId) 
			throws RelationException, ElementNotFoundException {
		
		Relation friendRequest = new Relation(senderId, receiverId, 0);
		
		if (!usersRepo.existsById(senderId) || !usersRepo.existsById(receiverId)) {
			throw new ElementNotFoundException("A user with that ID does not exist!");
		}
		
		if (relationsRepo.findBySenderIdAndReceiverId(senderId, receiverId) == null) {
			relationsRepo.save(friendRequest);
		}
		else {
			throw new RelationException("You have already sent a request to that person!"); 
		}
	}

	public void confirmFriendRequest(Integer receiverId, Integer senderId) {
		Relation relation = relationsRepo.findBySenderIdAndReceiverId(senderId, receiverId);
		relation.setIsConfirmed(1);
		relationsRepo.save(relation);
	}
	
	public List<User> findAllFromRequestsTo(Integer receiverId) {
		List<Relation> relations = relationsRepo.findAllByReceiverId(receiverId);
				
		List<User> users = new LinkedList<User>();
		
		for (Relation relation : relations) {
			Optional<User> optionalUser = usersRepo.findById(relation.getSenderId());
			if (optionalUser.isPresent()) {
				users.add(optionalUser.get());
			}
		}
		return users;
	}
	
	public List<UserSummaryDTO> searchByNameOrderedAndLimited(String search,Integer userId) {
		List<Integer> ids = userDao.getAllSearchingMatchesOrderedByIfFriend(userId, search);
		System.out.println(ids);
		List<User> users = usersRepo.findAllById(ids);
		System.out.println(users);
		List<UserSummaryDTO> usersDTO = new ArrayList<UserSummaryDTO>(users.size());
		for (User user : users) {
			UserSummaryDTO dto = new UserSummaryDTO();
			this.mapper.map(user, dto);//TODO check if sets friends count and profile photo
			usersDTO.add(dto);
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
	
	public List<UserInfo> findAllUsersInfo() {
		return usersInfoRepo.findAll();
	}
	
	public void save(UserInfo info) {
		usersInfoRepo.save(info);
	}
	
}
