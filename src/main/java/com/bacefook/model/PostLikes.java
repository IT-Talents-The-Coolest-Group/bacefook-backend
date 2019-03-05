package com.bacefook.model;

import javax.persistence.Entity;

import org.springframework.lang.NonNull;

import lombok.Data;

@Data
@Entity
public class PostLikes {

	@NonNull
//	@NonNullFields
	private Integer posterId;
	private Integer postId;
}
