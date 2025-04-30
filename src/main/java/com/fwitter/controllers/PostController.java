package com.fwitter.controllers;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fwitter.dto.CreatePostDTO;
import com.fwitter.models.ApplicationUser;
import com.fwitter.models.Post;
import com.fwitter.services.PostService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/posts")
public class PostController {
	
	private final PostService postService;
	
	public PostController(PostService postService) {
		this.postService = postService;
	}
	
	@GetMapping("/")
	public List<Post> getAllPosts(){
		return postService.getAllPosts();
	}
	
	@PostMapping("/")
	public Post createPost(@RequestBody CreatePostDTO postDTO) {
		return postService.createPost(postDTO);
	}
	
	@GetMapping("/id/{id}")
	public Post getPostById(@PathVariable("id") int id) {
		return postService.getPostById(id);
	}
	
	@GetMapping("/author/{userId}")
	public Set<Post> getPostByAuthor(@PathVariable("userId") int userId){
		ApplicationUser author = new ApplicationUser();
		author.setUserId(userId);
		return postService.getAllPostsByAuthor(author);
	}
	
	@DeleteMapping("/")
	public ResponseEntity<String> deletePost(@RequestBody Post p){
		postService.deletePost(p);
		return new ResponseEntity<String>("Post has been deleted", HttpStatus.OK);
	}
	
}
