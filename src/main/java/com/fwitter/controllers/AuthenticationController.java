package com.fwitter.controllers;

import java.util.LinkedHashMap;
import com.google.api.services.gmail.Gmail;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fwitter.dto.FindUsernameDTO;
import com.fwitter.exceptions.EmailAlreadyTakenException;
import com.fwitter.exceptions.EmailFailedToSendException;
import com.fwitter.exceptions.IncorrectVerificationCodeException;
import com.fwitter.exceptions.InvalidCredentialsException;
import com.fwitter.exceptions.UserDoesNotExistsException;
import com.fwitter.models.ApplicationUser;
import com.fwitter.models.LoginResponse;
import com.fwitter.models.RegistrationObject;
import com.fwitter.services.TokenService;
import com.fwitter.services.UserService;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthenticationController {

    private final ImageController imageController;

    private final Gmail getService;

	private final UserService userService;
	private final TokenService tokenService;
	private final AuthenticationManager authenticationManager;
	
	
	public AuthenticationController(UserService userService, TokenService tokenService, AuthenticationManager authenticationManager, Gmail getService, ImageController imageController) {
		this.userService = userService;
		this.tokenService = tokenService;
		this.authenticationManager =authenticationManager;
		this.getService = getService;
		this.imageController = imageController;
	}
	
	@ExceptionHandler({EmailAlreadyTakenException.class})
	public ResponseEntity<String> handleEmailTaken(){
		return new ResponseEntity<String>("The email you have provided is already in use, if it is your please login.", HttpStatus.CONFLICT);
	}
	
	@PostMapping("/register")
	public ApplicationUser registerUser(@RequestBody RegistrationObject ro) {
		return userService.registerUser(ro);
	}
	
	@ExceptionHandler({UserDoesNotExistsException.class})
	public ResponseEntity<String> handleUserDoesNotExist(){
		return new ResponseEntity<String>("The user you are looking doesn't exist...", HttpStatus.NOT_FOUND);
	}
	
	@PutMapping("/update/phone")
	public ApplicationUser updatePhoneNumber(@RequestBody LinkedHashMap<String, String> body) {
		
		String username = body.get("username");
		String phone = body.get("phone");
		
		ApplicationUser user;
		user = userService.getUserByusername(username);
		user.setPhone(phone);
		
		return userService.updateUser(user);
	}
	
	@ExceptionHandler({EmailFailedToSendException.class})
	public ResponseEntity<String> handleFailedEmail(){
		return new ResponseEntity<String>("Email Faild to send, try again in a moment...", HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@PostMapping("/email/code")
	public ResponseEntity<String> createEmailVerification(@RequestBody LinkedHashMap<String, String> body){
		userService.generateEmailVerification(body.get("username"));
		
		return new ResponseEntity<String>("Verification code generated, email sent...", HttpStatus.OK);
	}
	
	@ExceptionHandler({IncorrectVerificationCodeException.class})
	public ResponseEntity<String> incorectCodeHandler(){
		return new ResponseEntity<String>("The code provided does not match with the users verification code...", HttpStatus.CONFLICT);
	}
	
	@PostMapping("/email/verify")
	public ApplicationUser verifyEmail(@RequestBody LinkedHashMap<String, String> body) {
		
		Long code = Long.parseLong(body.get("code"));
		String username = body.get("username");
		
		return userService.verifyEmail(username, code);
	}
	
	@PutMapping("/update/password")
	public ApplicationUser updatePassword(@RequestBody LinkedHashMap<String, String> body) {
		String username = body.get("username");
		String password = body.get("password");
		return userService.setPassword(username, password);
	}
	
	@PostMapping("/login")
	public  LoginResponse login(@RequestBody LinkedHashMap<String, String> body) throws InvalidCredentialsException{
		
		String username = body.get("username");
		String password = body.get("password");
		
		try {
			Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
			
			String token = tokenService.generateToken(auth);
			return new LoginResponse(userService.getUserByusername(username), token);
		}catch(AuthenticationException e) {
			throw new InvalidCredentialsException();
		}
	}
	
	@ExceptionHandler({InvalidCredentialsException.class})
	public ResponseEntity<String> handleInvalidCredentials(){
		return new ResponseEntity<String>("Invalid credentials", HttpStatus.FORBIDDEN);
	}
	
	@PostMapping("/find")
	public ResponseEntity<String> verifyUsername(@RequestBody FindUsernameDTO credentials){
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.TEXT_PLAIN);
		String username = userService.verifyUsername(credentials);
		return new ResponseEntity<String> (username, HttpStatus.OK);
	}
	
}
