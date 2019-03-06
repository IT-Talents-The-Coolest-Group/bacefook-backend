package com.bacefook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bacefook.model.Photo;

@Repository
public interface PhotosRepository extends JpaRepository<Photo, Integer> {

}
