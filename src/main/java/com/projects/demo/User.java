package com.projects.demo;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;


@Document
public class User {

	@Id
	private String email;
	
	private String name;
	
	private String contactNo;
	
	private String gender;
	
	private String type;
	
	private Date date_Of_Birth;
	
	private String password;
	
	public User() {
		// TODO Auto-generated constructor stub
	}
	
	public User(String email, String name, String contactNo, String gender, String type, Date date_Of_Birth,
			String password) {
		super();
		this.email = email;
		this.name = name;
		this.contactNo = contactNo;
		this.gender = gender;
		this.type = type;
		this.date_Of_Birth = date_Of_Birth;
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getContactNo() {
		return contactNo;
	}
	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public Date getDate_Of_Birth() {
		return date_Of_Birth;
	}
	public void setDate_Of_Birth(Date date_Of_Birth) {
		this.date_Of_Birth = date_Of_Birth;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	

}
