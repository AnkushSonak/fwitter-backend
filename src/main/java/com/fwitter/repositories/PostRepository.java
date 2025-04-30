package com.fwitter.repositories;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fwitter.models.ApplicationUser;
import com.fwitter.models.Post;

public interface PostRepository extends JpaRepository<Post, Integer>{
	
	Optional<Set<Post>> findByAuthor(ApplicationUser author);
}
