package com.lucaslearning.wsreader.domain;

import java.util.List;

public class UserResponse {

	private List<User> data;
	
	public UserResponse() {
		
	}

	public UserResponse(List<User> data) {
		this.data = data;
	}

	public List<User> getData() {
		return data;
	}

	public void setData(List<User> data) {
		this.data = data;
	}
	
}
