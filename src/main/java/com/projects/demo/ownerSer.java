package com.projects.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ownerSer {
	
	@Autowired
	ownerRepo repo;
	
	public User saveO(User owner) {
		return repo.save(owner);
	}
	
	public User findUser(String username,String password) {
		return repo.findAll().stream().filter(db->db.getEmail().equals(username)&&db.getPassword().equals(password)).findFirst().orElse(null);
	}

}
