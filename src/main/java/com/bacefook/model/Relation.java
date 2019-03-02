package com.bacefook.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@RequiredArgsConstructor
@NoArgsConstructor
@Table(name = "relations")
public class Relation {
	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id private Integer id;
	@NonNull private Integer senderId;
	@NonNull private Integer receiverId;
}
