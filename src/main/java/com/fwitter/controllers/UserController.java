package com.fwitter.controllers;

import java.util.LinkedHashMap;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fwitter.exceptions.FollowException;
import com.fwitter.exceptions.UnableToSavePhotoException;
import com.fwitter.models.ApplicationUser;
import com.fwitter.services.TokenService;
import com.fwitter.services.UserService;
import com.google.common.net.HttpHeaders;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {
	private final UserService userService;
	private final TokenService tokenService;
	
	public UserController(UserService userService, TokenService tokenService) {
		this.tokenService = tokenService;
		this.userService = userService;
	}
	
	@GetMapping("/verify")
	public ApplicationUser verifyIdentity(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		String username = tokenService.getUsernameFromToken(token);
		
		return userService.getUserByusername(username);
	}
	
	@PostMapping("/pfp")
	public ApplicationUser uploadProfilePicture(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestParam("image") MultipartFile file) throws UnableToSavePhotoException{
		
		String username = tokenService.getUsernameFromToken(token);
		return userService.setProfileOrBannerPicture(username, file, "pfp");
	}
	
	@PostMapping("/banner")
	public ApplicationUser uploadBannerPicture(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestParam("image") MultipartFile file) throws UnableToSavePhotoException{
		
		String username = tokenService.getUsernameFromToken(token);
		return userService.setProfileOrBannerPicture(username, file, "bnr");
	}
	
	@PutMapping("/")
	public ApplicationUser updateUser(@RequestBody ApplicationUser u) {
		return userService.updateUser(u);
	}
	
	@ExceptionHandler({FollowException.class})
	public ResponseEntity<String> handleFollowException(){
		return new ResponseEntity<String>("Users cannot follow themselves", HttpStatus.FORBIDDEN);
	}
	
	@PutMapping("/follow")
	public Set<ApplicationUser> followUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody LinkedHashMap<String, String> body) throws FollowException{
		String loggedInUser = tokenService.getUsernameFromToken(token);
		String followedUser = body.get("followedUser");
		
		return userService.followUser(loggedInUser, followedUser);
	}
	
	@GetMapping("/following/{username}")
	public Set<ApplicationUser> getFollowingList(@PathVariable("username") String username){
		return userService.retriveFollowingList(username);
	}
	
	@GetMapping("/followers/{username}")
	public Set<ApplicationUser> getFollowersList(@PathVariable("username") String username){
		return userService.retriveFollowersList(username);
	}
}
