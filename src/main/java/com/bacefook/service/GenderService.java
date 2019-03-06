package com.bacefook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bacefook.exception.ElementNotFoundException;
import com.bacefook.model.Gender;
import com.bacefook.repository.GenderRepository;

@Service
public class GenderService {

	@Autowired
	private GenderRepository genderRepo;
	
	public String findById(Integer id) throws ElementNotFoundException {
		Gender gender = genderRepo.findById(id).get();
		if (gender == null) {
			throw new ElementNotFoundException("A gender with that id does not exist!");
		}
		return gender.getGenderName();
	}
	
	public Gender findByName(String genderName) throws ElementNotFoundException {
		Gender gender = genderRepo.findByGenderName(genderName);
		if (gender == null) {
			throw new ElementNotFoundException("A gender with that name does not exist!");
		}
		return gender;
	}
	
}
