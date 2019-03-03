package com.bacefook.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.bacefook.model.User;

public class SessionManager {
	private static final String LOGGED_STATUS = "logged";
	private static final int SECONDS = 60*60*24*30;
	
	public static boolean isLogged(HttpServletRequest request) {
		HttpSession session = request.getSession();
		
		return (!session.isNew()) && (session.getAttribute(LOGGED_STATUS) != null);
	}
	public static void signInUser(HttpServletRequest request, User user) {
		HttpSession session = request.getSession();
		session.setAttribute(LOGGED_STATUS, user);
		session.setMaxInactiveInterval(SECONDS);
	}
	
	public static int getLoggedUser(HttpServletRequest request) {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute(LOGGED_STATUS);
		System.out.println(user);
		return user.getId();
	}
}
