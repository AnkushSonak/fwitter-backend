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
import com.fwitter.models.Poll;
import com.fwitter.models.PollChoice;
import com.fwitter.models.Post;
import com.fwitter.repositories.PostRepository;

@Service
@Transactional
public class PostService {

	@Autowired
	private final PostRepository postRepo;
	
	@Autowired
	private final ImageService imageService;
	
	@Autowired
	private final PollService pollService;
	
	public PostService(PostRepository postRepo, ImageService imageService, PollService pollService) {
		this.postRepo = postRepo;
		this.imageService = imageService;
		this.pollService = pollService;
	}
	
	public Post createPost(CreatePostDTO postDto) {
		
		Image savedGif;
		
		//if true there is a single gif from tenor
		if(postDto.getImages()  != null && postDto.getImages().size() > 0) {
			List<Image> gifList = postDto.getImages();
			Image gif = gifList.get(0);
			gif.setImageId(null); //TODO: Need to be worked here as i am adding null to avoid the error.
			System.out.println("Gif: --> " + gif.toString());
			gif.setImagePath(gif.getImageUrl());
			
			savedGif = imageService.saveGifFromPost(gif);
			gifList.remove(0);
			gifList.add(savedGif);
			postDto.setImages(gifList);
		}
		
		//if true there is a Poll that need to be created
		Poll savedPoll =  null;
		if(postDto.getPoll() != null) {
			Poll p = new Poll();
			p.setEndTime(postDto.getPoll().getEndTime());
			p.setChoices(new ArrayList<>());
			savedPoll = pollService.generatePoll(p);
			List<PollChoice> pollChoices = new ArrayList<PollChoice>();
			List<PollChoice> choices = postDto.getPoll().getChoices();
			for(int i = 0; i< choices.size(); i++) {
				PollChoice choice = choices.get(i);
				choice.setPollChoiceId(null);  //TODO: the id is set null because it will be taken care by spring it self as the Generation strategy is auto
				choice.setPoll(savedPoll);
				pollService.generateChoice(choice);
				pollChoices.add(choice);
			}
			savedPoll.setChoices(pollChoices);
			savedPoll = pollService.generatePoll(savedPoll);
			
			System.out.println(savedPoll);
		}
		
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
		p.setImages(postDto.getImages());
		p.setPoll(savedPoll);
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
