package com.bacefook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bacefook.model.CommentLike;

@Repository
public interface CommentLikesRepository extends JpaRepository<CommentLike, Integer> {

}
