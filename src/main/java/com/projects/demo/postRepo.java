package com.projects.demo;

import java.util.List;

import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.DeleteMapping;

@Repository
public interface postRepo extends MongoRepository<post, String> {

	@Override
	public List<post> findAll();
	
	@Query(value="{'_id' : $0}")
	post deleteByValue(String id);
	
}
