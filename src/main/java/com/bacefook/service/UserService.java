package com.bacefook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bacefook.exception.UserNotFoundException;
import com.bacefook.model.User;
import com.bacefook.repository.UsersRepository;


@Service
public class UserService {

	@Autowired 
	private UsersRepository usersRepo;
	@Autowired 
	//private RelationsRepository relationsRepo;
	
	public User findUserByEmail(String email) throws UserNotFoundException {
		User user = usersRepo.findByEmail(email);
		
		if (user == null) {
			throw new UserNotFoundException("A user with that email does not exist!");
		}
		return user;
	}
	
	public User findUserById(Integer id) throws UserNotFoundException {
		User user = usersRepo.findById(id).get();
		if (user == null) {
			throw new UserNotFoundException("A user with that ID does not exist!");
		}
		return user;
	}
	
	public Integer saveUser(User user) {
		return usersRepo.save(user).getId();
	}
	
	public void makeRelation(Integer senderId, Integer receiverId) {
//		usersRepo.save(new User());
//		relationsRepo.save(new Relation(senderId, receiverId)); // TODO fix this
	}
	
	
}
