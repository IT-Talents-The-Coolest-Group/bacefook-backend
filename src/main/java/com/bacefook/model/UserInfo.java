package com.bacefook.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "additional_users_info")
public class UserInfo {

	@Id
	private Integer id;
	private Integer cityId;
	private Integer profilePhotoId;
	private Integer coverPhotoId;
	private String phone;
	
}
