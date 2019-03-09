package com.bacefook.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bacefook.model.UserInfo;

public interface UsersInfoRepository extends JpaRepository<UserInfo, Integer> {

}
