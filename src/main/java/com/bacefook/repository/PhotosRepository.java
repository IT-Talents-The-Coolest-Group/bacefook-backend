package com.bacefook.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bacefook.model.Photo;

@Repository
public interface PhotosRepository extends JpaRepository<Photo, Integer> {

	@Query(value = "SELECT ph.id FROM photos ph left join posts po on (ph.post_id = po.id) left join users u on (po.poster_id = u.id) where u.id = ?1",
			nativeQuery = true)
	List<Integer> findAllPhotosOfUser(Integer userId);

}
