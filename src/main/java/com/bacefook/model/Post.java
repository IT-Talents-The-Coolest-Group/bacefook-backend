package com.bacefook.model;

import java.time.LocalDate;

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
@Table(name = "posts")
public class Post {
	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id private Integer id;
	
	// TODO figure out how to deal with these FK's
	//private User poster;
	//private Post sharesPost;
	@NonNull private String content;
	@NonNull private LocalDate postingTime;
	
}
