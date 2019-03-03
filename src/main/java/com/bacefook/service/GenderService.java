package com.bacefook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bacefook.exception.GenderNotFoundException;
import com.bacefook.model.Gender;
import com.bacefook.repository.GenderRepository;

@Service
public class GenderService {

	@Autowired
	private GenderRepository genderRepo;
	
	public String findGenderById(Integer id) throws GenderNotFoundException {
		Gender gender = genderRepo.findById(id).get();
		if (gender == null) {
			throw new GenderNotFoundException("A gender with that id does not exist!");
		}
		return gender.getGenderName();
	}
	
	public Gender findByGenderName(String genderName) throws GenderNotFoundException {
		Gender gender = genderRepo.findByGenderName(genderName);
		if (gender == null) {
			throw new GenderNotFoundException("A gender with that name does not exist!");
		}
		return gender;
	}
	
}
