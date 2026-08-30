package com.example.demo.domain.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "login_user")
@Data
public class UserList implements Serializable{
	@Id
	@Column(name = "user_id")
	private String userId;
	
	@Column(name = "user_name")
	private String userName;
	
	@Column(name = "user_pass")
	private String userPass;
	
	@Column(name = "user_role")
	private String userRole;
}
