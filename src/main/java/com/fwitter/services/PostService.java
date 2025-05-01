package com.fwitter.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fwitter.dto.CreatePostDTO;
import com.fwitter.exceptions.PostDoesNotExistException;
import com.fwitter.exceptions.UnableToCreatePostException;
import com.fwitter.models.ApplicationUser;
import com.fwitter.models.Image;
import com.fwitter.models.Post;
import com.fwitter.repositories.PostRepository;

@Service
@Transactional
public class PostService {

	private final PostRepository postRepo;
	
	private final ImageService imageService;
	
	public PostService(PostRepository postRepo, ImageService imageService) {
		this.postRepo = postRepo;
		this.imageService = imageService;
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
			 throw new UnableToCreatePostException();
		}	
	}
	
	public Post createMediaPost(String post, List<MultipartFile> files) {
		CreatePostDTO postDto = new CreatePostDTO();
		
		try {
			
			ObjectMapper objectMapper = new ObjectMapper();
			postDto = objectMapper.readValue(post, CreatePostDTO.class);
			
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
			
			//Upload the images that got to passed
			List<Image> postImages = new ArrayList<>();
			
			for(int i = 0; i <files.size(); i++) {
				Image postImage = imageService.uploadImage(files.get(i), "post");
				postImages.add(postImage);
			}
			
			p.setImages(postImages);
			return postRepo.save(p);
			
		} catch (Exception e) {
			throw new UnableToCreatePostException();
		}
	}
	
	public List<Post> getAllPosts(){
		return postRepo.findAll();
	}
	
	public Post getPostById(Integer Id) {
		return postRepo.findById(Id).orElseThrow(PostDoesNotExistException::new);
	}
	
	public Set<Post> getAllPostsByAuthor(ApplicationUser author){
		Set<Post> userPosts = postRepo.findByAuthor(author).orElse(new HashSet<>());
		
		return userPosts;
	}
	
	public void deletePost(Post p) {
		postRepo.delete(p);
	}
}
