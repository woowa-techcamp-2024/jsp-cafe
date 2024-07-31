package com.example.db;

import java.util.List;
import java.util.Optional;

public interface Database<ID, T> {

	ID insert(T t);

	Optional<T> findById(ID id);

	List<T> findAll();

	void update(ID id, T t);
}
