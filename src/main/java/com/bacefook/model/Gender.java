package com.bacefook.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Entity
@NoArgsConstructor
@Table(name = "genders")
public class Gender {
	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id private Integer id;
	@NonNull private String genderName;
}
