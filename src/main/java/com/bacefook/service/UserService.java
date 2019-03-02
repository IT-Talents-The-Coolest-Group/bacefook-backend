package com.bacefook.service;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bacefook.exception.UserNotFoundException;
import com.bacefook.model.Relation;
import com.bacefook.model.User;
import com.bacefook.repository.RelationsRepository;
import com.bacefook.repository.UsersRepository;

@Service
public class UserService {

	@Autowired private UsersRepository usersRepo;
	@Autowired private RelationsRepository relationsRepo;
	
	public User findUserById(Integer id) throws UserNotFoundException {
		try {
			return usersRepo.findById(id).get();
		}
		catch (NoSuchElementException e) {
			throw new UserNotFoundException("A user with that ID does not exist!", e);
		}
	}
	
	public Integer saveUser(User user) {
		return usersRepo.save(user).getId();
	}
	
	public void makeRelation(Integer senderId, Integer receiverId) {
		usersRepo.save(new User());
		relationsRepo.save(new Relation(senderId, receiverId)); // TODO fix this
	}
	
	public User findUserByEmail(String email) {
		return usersRepo.findByEmail(email);
	}
	
}
