package com.bacefook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bacefook.model.Comment;

@Repository
public interface CommentsRepository extends JpaRepository<Comment, Integer>{

}
