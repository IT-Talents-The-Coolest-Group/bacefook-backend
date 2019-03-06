package com.bacefook.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.bacefook.exception.UnauthorizedException;
import com.bacefook.model.User;

public class SessionManager {
	private static final String LOGGED_STATUS = "logged";
	private static final int SECONDS = 60 * 60 * 24 * 30;

	public static boolean isLogged(HttpServletRequest request) {
		HttpSession session = request.getSession();
//		if (session.isNew()) {
//			System.out.println("Session was just created");//TODO
//			return false;
//		}
		return session.getAttribute(LOGGED_STATUS) != null;
	}

	public static void signInUser(HttpServletRequest request, User user) {
		HttpSession session = request.getSession();
		session.setAttribute(LOGGED_STATUS, user);
		session.setMaxInactiveInterval(SECONDS);
	}

	public static String logOutUser(HttpServletRequest request) {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute(LOGGED_STATUS);
		session.setAttribute(LOGGED_STATUS, null);
		return "User " + user.getId() + " was logged out!";
	}

	/**
	 * @return Logged user's ID, or null if no user is logged in
	 * @throws UnauthorizedException
	 */
	public static User getLoggedUser(HttpServletRequest request) throws UnauthorizedException {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute(LOGGED_STATUS);
		if (user != null) {
			return user;
		}
		throw new UnauthorizedException("You are not logged in!");
	}
}
