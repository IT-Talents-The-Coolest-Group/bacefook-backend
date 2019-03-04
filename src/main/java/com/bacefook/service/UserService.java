package com.bacefook.service;

import java.util.NoSuchElementException;

import javax.management.relation.RelationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bacefook.exception.UserNotFoundException;
import com.bacefook.model.Relation;
import com.bacefook.model.User;
import com.bacefook.repository.RelationsRepository;
import com.bacefook.repository.UsersRepository;

@Service
public class UserService {

	@Autowired 
	private UsersRepository usersRepo;
	@Autowired 
	private RelationsRepository relationsRepo;
	
	public User findUserByEmail(String email) throws UserNotFoundException {
		User user = usersRepo.findByEmail(email);
		
		if (user == null) {
			throw new UserNotFoundException("A user with that email does not exist!");
		}
		return user;
	}
	
	public boolean emailIsTaken(String email) {
		return usersRepo.findByEmail(email) != null;
	}
	
	public User findUserById(Integer id) throws UserNotFoundException {
		try {
			User user = usersRepo.findById(id).get();
			return user;
		}
		catch (NoSuchElementException e) {
			throw new UserNotFoundException("A user with that ID does not exist!", e);
		}
	}
	
	public Integer saveUser(User user) {
		return usersRepo.save(user).getId();
	}
	
	public void makeRelation(Integer senderId, Integer receiverId) throws RelationException {
		Relation friendRequest = new Relation(senderId, receiverId);
		
		if (relationsRepo.findBySenderIdAndReceiverId(senderId, receiverId) == null) {
			relationsRepo.save(friendRequest);
		}
		else {
			throw new RelationException("You have already sent a request to that person!"); 
		}
	}
	
}
