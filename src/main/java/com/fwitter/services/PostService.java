package com.fwitter.services;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fwitter.dto.CreatePostDTO;
import com.fwitter.models.ApplicationUser;
import com.fwitter.models.Post;
import com.fwitter.repositories.PostRepository;

@Service
@Transactional
public class PostService {

	private final PostRepository postRepo;
	
	public PostService(PostRepository postRepo) {
		this.postRepo = postRepo;
	}
	
	public Post createPost(CreatePostDTO postDto) {
		Post p = new Post();
		p.setContent(postDto.getContent());
		if(postDto.getScheduled()) {
			p.setPostDate(postDto.getScheduledDate());
		}else {
			p.setPostDate(new Date());
		}
		p.setAuthor(postDto.getAuthor());
		p.setReplies(postDto.getReplies());
		p.setScheduled(postDto.getScheduled());
		p.setScheduled(postDto.getScheduled());
		p.setAudience(postDto.getAudience());
		p.setReplyRestriction(postDto.getReplyRestriction());
		
		try {
			Post posted = postRepo.save(p);
			return posted;
		} catch (Exception e) {
			return null;
		}	
	}
	
	public List<Post> getAllPosts(){
		return postRepo.findAll();
	}
	
	public Post getPostById(Integer Id) {
		return postRepo.findById(Id).get();
	}
	
	public Set<Post> getAllPostsByAuthor(ApplicationUser author){
		Set<Post> userPosts = postRepo.findByAuthor(author).orElse(new HashSet<>());
		
		return userPosts;
	}
	
	public void deletePost(Post p) {
		postRepo.delete(p);
	}
}
