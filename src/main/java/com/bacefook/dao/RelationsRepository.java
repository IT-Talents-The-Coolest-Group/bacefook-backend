package com.bacefook.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bacefook.model.Relation;

@Repository
public interface RelationsRepository extends JpaRepository<Relation, Integer> {

}
