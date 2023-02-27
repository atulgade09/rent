package com.projects.demo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ownerRepo extends MongoRepository<Owner, String> {

	@Override
	public List<Owner> findAll();
}
