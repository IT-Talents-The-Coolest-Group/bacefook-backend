package com.bacefook.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bacefook.model.Gender;

@Repository
public interface GenderRepository extends JpaRepository<Gender, Integer> {

}
