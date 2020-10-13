package com.wellsfargo.sba3.its.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name="users")
public class UserEntity implements Serializable,Comparable<UserEntity> {

	@Id
	@Column(name="uId")
	private Integer userId;
	
	@Column(name="fname")
	private String firstName;
	
	@Column(name="lname")
	private String lastName;
	
	@Column(name="email")
	private String email;
	
	@Column(name="mobile")
	private String mobile;
	
	@ManyToMany(mappedBy = "attendees", fetch=FetchType.LAZY, cascade = CascadeType.ALL)
	private List<InterviewEntity> interviews;
	
	public UserEntity() {
		//left unimplemented
	}

	public UserEntity(Integer userId, String firstName, String lastName, String email, String mobile) {
		super();
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.mobile = mobile;
	}
	public UserEntity(Integer userId, String firstName, String lastName, String email, String mobile, List<InterviewEntity> interviews) {
		super();
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.mobile = mobile;
		this.interviews = interviews;
	}


	public Integer getUserId() {
		return userId;
	}


	public void setUserId(Integer userId) {
		this.userId = userId;
	}


	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getMobile() {
		return mobile;
	}


	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	
	public List<InterviewEntity> getInterviews() {
		return interviews;
	}


	public void setInterviews(List<InterviewEntity> interviews) {
		this.interviews = interviews;
	}


	@Override
	public String toString() {
		return "User [userId=" + userId + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", email=" + email + ", mobile=" + mobile + "]";
	}


	@Override
	public int compareTo(UserEntity o) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
}