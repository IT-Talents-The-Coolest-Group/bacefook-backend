package com.bacefook.service;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bacefook.dao.UsersRepository;
import com.bacefook.exceptions.UserNotFoundException;
import com.bacefook.model.User;

@Service
public class UserService {

	@Autowired
	private UsersRepository usersRepo;
	
	public User findUserById(Integer id) throws UserNotFoundException {
		try {
			return usersRepo.findById(id).get();
		}
		catch (NoSuchElementException e) {
			throw new UserNotFoundException("A user with that ID does not exist!", e);
		}
	}
	
}
