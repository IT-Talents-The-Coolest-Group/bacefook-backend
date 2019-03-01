package com.bacefook.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bacefook.dao.UsersRepository;
import com.bacefook.model.User;

@Service
public class UserService {

	@Autowired
	private UsersRepository usersRepo;
	
	public List<User> getAllUsers() {
		return usersRepo.findAll();
	}
	
}
