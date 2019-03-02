package com.bacefook.service;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bacefook.dao.GenderRepository;
import com.bacefook.exception.GenderNotFoundException;

@Service
public class GenderService {

	@Autowired
	private GenderRepository genderRepo;
	
	public String findGenderById(Integer id) throws GenderNotFoundException {
		try {
			return genderRepo.findById(id).get().getGender();
		}
		catch (NoSuchElementException e) {
			throw new GenderNotFoundException("Could not find a gender with that id!", e);
		}
	}
	
}
