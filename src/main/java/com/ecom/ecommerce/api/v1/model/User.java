package com.ecom.ecommerce.api.v1.model;

import java.util.Map;

public class User {
	
	private int id;
    private String name;
    private String password;
    private String email;
    private String role;
    
	 public User() {
		 super();
	 }
	
	public User(Map<String, Object> user) {
		super();
		this.name = (String) user.getOrDefault("name", null);
		this.password = (String) user.getOrDefault("password", null);
		this.email = (String) user.getOrDefault("email", null);
		this.role = (String) user.getOrDefault("role", null);
	}

	public User(int id, String name, String password, String email, String role) {
		 super();
		 this.id = id;
		 this.name = name;
		 this.password = password;
		 this.email = email;
		 this.role = role;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	} 
}