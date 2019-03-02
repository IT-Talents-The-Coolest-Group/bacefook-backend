package com.bacefook.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@Entity
@Table(name = "genders")
public class Gender {

	@NotNull @Id private Integer id;
	@NotNull private String gender;
}
