package com.example.entity;

public record User(
	String id,
	String password,
	String name,
	String email
) {
}
