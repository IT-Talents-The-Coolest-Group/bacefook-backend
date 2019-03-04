package com.bacefook.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionManager {
	private static final String LOGGED_STATUS = "logged";
	private static final int SECONDS = 60 * 60 * 24 * 30;

	public static boolean isLogged(HttpServletRequest request,Integer id) {
		HttpSession session = request.getSession();
		if (session.isNew()) {
			System.out.println("Session was just created");
			return false;
		}
//		System.out.println("logged " + session.getAttribute(LOGGED_STATUS));
		if(session.getAttribute(LOGGED_STATUS)==null) {
			System.out.println("No session for this user");
			return false;
		}
		return session.getAttribute(LOGGED_STATUS)==id;
	}

	public static void signInUser(HttpServletRequest request, Integer id) {
		HttpSession session = request.getSession();
		session.setAttribute(LOGGED_STATUS, id);
//		System.out.println(session.getAttribute(LOGGED_STATUS));
		session.setMaxInactiveInterval(SECONDS);
	}

	public static String logOutUser(HttpServletRequest request) {
		HttpSession session = request.getSession();
		int userId =  (int) session.getAttribute(LOGGED_STATUS);
		session.setAttribute(LOGGED_STATUS, null);
		return "User " + userId + " was logged out!";
		// TODO should we delete cookies
//		Cookie[] cookies = request.getCookies();
//		for (Cookie cookie : cookies) {
//			cookie.
//		}
	}

	/**
	 * @return Logged user's ID, or null if no user is logged
	 */
	public static Object getLoggedUser(HttpServletRequest request) {
		HttpSession session = request.getSession();
		return session.getAttribute(LOGGED_STATUS);
//		System.out.println(userId);
//		return userId;
	}
}
