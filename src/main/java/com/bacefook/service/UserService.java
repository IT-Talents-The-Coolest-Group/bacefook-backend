package com.bacefook.service;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.management.relation.RelationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bacefook.exception.ElementNotFoundException;
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

	public User findUserByEmail(String email) throws ElementNotFoundException {
		User user = usersRepo.findByEmail(email);
		
		if (user == null) {
			throw new ElementNotFoundException("A user with that email does not exist!");
		}
		return user;
	}
	
	public boolean emailIsTaken(String email) {
		return usersRepo.findByEmail(email) != null;
	}
	
	public User findUserById(Integer id) throws ElementNotFoundException {
		try {
			User user = usersRepo.findById(id).get();
			return user;
		}
		catch (NoSuchElementException e) {
			throw new ElementNotFoundException("A user with that ID does not exist!", e);
		}
	}
	
	public Integer saveUser(User user) {
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
	
	public List<User> findAllUsersFromRequestsTo(Integer receiverId) {
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
	
	
	
}
