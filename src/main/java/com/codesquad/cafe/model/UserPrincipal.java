package com.codesquad.cafe.model;

import java.io.Serializable;

public class UserPrincipal implements Serializable {
	private final Long id;
	private final String username;

	public UserPrincipal(Long id, String username) {
		this.id = id;
		this.username = username;
	}

	public Long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}
}
