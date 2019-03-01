package com.bacefook.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "posts")
public class Post {
	
	@Id private Integer id;
	
	// TODO figure out how to deal with these FK's
	//private User poster;
	//private Post sharesPost;
	private String content;
	private LocalDate postingTime;
	
}
