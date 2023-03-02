package com.projects.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class postSer {

	@Autowired
	postRepo repo;
	
	public post savePost(post p) {
		return repo.save(p);
	}
	
	public List<post> postList(){
		return repo.findAll();
	}
	
	public post postById(String id) {
		return repo.findAll().stream().filter(db->db.getId().equals(id)).findFirst().orElse(null);
	}
	
	public post deleteBypId(String id) {
		post p=postById(id);
		repo.deleteById(id);
		return  p;
	}
}
