package com.example.db;

import java.util.List;
import java.util.Optional;

public interface Database<ID, T> {

	void insert(T t);

	Optional<T> findById(ID id);

	List<T> findAll();
}
