package com.bacefook.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NonNull;

@Data
@Entity
@Table(name = "genders")
public class Gender {

	@NonNull @Id private Integer id;
	@NonNull private String genderName;
}
