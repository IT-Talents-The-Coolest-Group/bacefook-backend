
package com.bacefook.model;

import java.time.LocalDate;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.WhereJoinTable;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@NonNull
	private Integer genderId;
	@NonNull @Column(unique=true, nullable = false)
	private String email;
	@NonNull
	private String firstName;
	@NonNull
	private String lastName;
	@NonNull
	private String password;
	@NonNull
	private LocalDate birthday;

//	@NonNull // TODO implement
//	private String profilePhotoUrl;
	
	/* Makes sure that UserRepository gets all friends
	 * of this user from the 'relations' table */
	@ManyToMany(
		fetch = FetchType.LAZY,
		cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
    	name = "relations",
    	joinColumns = { @JoinColumn(name = "sender_id") },
    	inverseJoinColumns = { @JoinColumn(name = "receiver_id") })
	@WhereJoinTable( 
		clause = "is_confirmed = 1")
	private Set<User> friends;
	
	public String getFullName() {
		return getFirstName() + " " + getLastName();
	}
	
	public Integer getFriendsCount() {
		return friends.size();
	}
}
