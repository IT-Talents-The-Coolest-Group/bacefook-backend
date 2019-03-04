package com.bacefook.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.bacefook.exception.UnauthorizedException;

public class SessionManager {
	private static final String LOGGED_STATUS = "logged";
	private static final int SECONDS = 60 * 60 * 24 * 30;

	public static boolean isLogged(HttpServletRequest request, Integer userId) {
		HttpSession session = request.getSession();
		if (session.isNew()) {
			System.out.println("Session was just created");
			return false;
		}
//		System.out.println("logged " + session.getAttribute(LOGGED_STATUS));
		if (session.getAttribute(LOGGED_STATUS) == null) {
			System.out.println("No session for this user");
			return false;
		}
		return session.getAttribute(LOGGED_STATUS) == userId;
	}

	public static void signInUser(HttpServletRequest request, Integer userId) {
		HttpSession session = request.getSession();
		session.setAttribute(LOGGED_STATUS, userId);
//		System.out.println(session.getAttribute(LOGGED_STATUS));
		session.setMaxInactiveInterval(SECONDS);
	}

	public static String logOutUser(HttpServletRequest request) {
		HttpSession session = request.getSession();
		int userId = (int) session.getAttribute(LOGGED_STATUS);
		session.setAttribute(LOGGED_STATUS, null);
		return "User " + userId + " was logged out!";
		// TODO should we delete cookies
//		Cookie[] cookies = request.getCookies();
//		for (Cookie cookie : cookies) {
//			cookie.
//		}
	}

	// TODO CONFLICT
//<<<<<<< HEAD
//	public static User getLoggedUser(HttpServletRequest request) throws UnauthorizedException {
//		if (!isLogged(request)) {
//			throw new UnauthorizedException("You are not logged in");
//		}

	/**
	 * @return Logged user's ID, or null if no user is logged in
	 * @throws UnauthorizedException
	 */
	public static int getLoggedUser(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Object id = session.getAttribute(LOGGED_STATUS);
		if (id != null) {
			return (int) id;
		}
		return -1;
//		System.out.println(userId);
//		return userId;
	}
}
