package com.bacefook.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comments")
public class Comment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@NonNull
	@Column(nullable = false)
	private Integer posterId;
	@NonNull
	@Column(nullable = false)
	private Integer postId;
	@Column
	private Integer commentedOnId;
	@NonNull
	@Column(nullable = false)
	private String content;
	@NonNull
	@Column(nullable = false)
	private LocalDateTime postingTime;

}
