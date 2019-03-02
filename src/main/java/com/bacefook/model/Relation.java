package com.bacefook.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "relations")
public class Relation {

	@NotNull @Id private Integer id;
	@NotNull private Integer senderId;
	@NotNull private Integer receiverId;
}
