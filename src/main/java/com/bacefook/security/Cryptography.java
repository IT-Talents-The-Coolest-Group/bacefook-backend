package com.bacefook.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Cryptography {
	
	public static String cryptSHA256(String password) throws NoSuchAlgorithmException {

		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] byteData = digest.digest(password.getBytes());
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < byteData.length; i++) {
			String hex = Integer.toHexString(0xff & byteData[i]);
			if (hex.length() == 1)
				stringBuilder.append('0');
			stringBuilder.append(hex);
		}
		return stringBuilder.toString();
	}
}
