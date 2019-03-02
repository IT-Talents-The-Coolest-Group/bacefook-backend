package com.bacefook.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.bacefook.model.User;

public class SessionManager {
	private static final int SECONDS = 60*60*24*30;
	
	public static boolean validateLogin(HttpServletRequest request) {
		HttpSession session = request.getSession();
		if(session.isNew()) return false;
		if(session.getAttribute("logged")==null) return false;
			//session.getAttribute("logged")==false ??
		return true;
	}
	public static void signInUser(HttpServletRequest request, User user) {
		HttpSession session = request.getSession();
		session.setAttribute("logged", user);
		session.setMaxInactiveInterval(SECONDS);
	}
}
