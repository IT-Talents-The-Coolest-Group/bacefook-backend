package com.bacefook.controller;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bacefook.dao.UserDAO;
import com.bacefook.model.User;

@RestController
public class UserController {
	@Autowired
	private UserDAO userRepository;
	
	@GetMapping("/users")
	public List<User> getAllUsers() {
		try {
			return userRepository.getAllUsers();
		} catch (SQLException e) {
			e.printStackTrace();
			return new LinkedList<User>();
		}
	}
	
	
}
