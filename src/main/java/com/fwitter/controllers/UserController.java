package com.fwitter.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fwitter.exceptions.UnableToSavePhotoException;
import com.fwitter.models.ApplicationUser;
import com.fwitter.services.ImageService;
import com.fwitter.services.TokenService;
import com.fwitter.services.UserService;
import com.google.common.net.HttpHeaders;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {
	private final UserService userService;
	private final TokenService tokenService;
	private final ImageService imageService;
	
	@Autowired
	public UserController(UserService userService, TokenService tokenService, ImageService imageService) {
		this.tokenService = tokenService;
		this.userService = userService;
		this.imageService = imageService;
	}
	
	@GetMapping("/verify")
	public ApplicationUser verifyIdentity(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		String username = "";
		ApplicationUser user;
		
		if(token.substring(0, 6).equals("Bearer")) {
			String strippedToken = token.substring(7);
			username = tokenService.getUsernameFromToken(strippedToken);
		}
		try {
			user = userService.getUserByusername(username);
		}catch(Exception e) {
			user = null;
		}
		
		return user;
	}
	
	@PostMapping("/pfp")
	public ResponseEntity<String> uploadProfilePicture(@RequestParam("image") MultipartFile file) throws UnableToSavePhotoException{
		String uploadImage = imageService.uploadImage(file, "pfp");
		
		return ResponseEntity.status(HttpStatus.OK).body(uploadImage);
	}
	
}
