package com.bacefook.model;

import java.time.LocalDate;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.WhereJoinTable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
	
	@Id private Integer id;
	@NotNull private int genderId;
	@NotNull private String email;
	@NotNull private String firstName;
	@NotNull private String lastName;
	@NotNull private String password;
	@NotNull private LocalDate birthday;
	
	/* Makes sure that UserRepository gets all friends
	 * of this user from the 'relations' table
	 */
	@ManyToMany(
		fetch = FetchType.LAZY,
		cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
    	name = "relations",
    	joinColumns = { @JoinColumn(name = "user1_id") },
    	inverseJoinColumns = { @JoinColumn(name = "user2_id") })
	@WhereJoinTable( 
		clause = "is_confirmed = 1")
	private Set<User> friends;
	
	
}
