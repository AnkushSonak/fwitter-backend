package com.fwitter.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fwitter.dto.FindUsernameDTO;
import com.fwitter.exceptions.EmailAlreadyTakenException;
import com.fwitter.exceptions.EmailFailedToSendException;
import com.fwitter.exceptions.FollowException;
import com.fwitter.exceptions.IncorrectVerificationCodeException;
import com.fwitter.exceptions.UnableToSavePhotoException;
import com.fwitter.exceptions.UserDoesNotExistsException;
import com.fwitter.models.ApplicationUser;
import com.fwitter.models.Image;
import com.fwitter.models.RegistrationObject;
import com.fwitter.models.Role;
import com.fwitter.repositories.RoleRepository;
import com.fwitter.repositories.UserRepository;

@Service
public class UserService implements UserDetailsService{
	 
	private final UserRepository userRepo;
	private final RoleRepository roleRepo;
	private final MailService mailService;
	private final PasswordEncoder passwordEncoder;
	private final ImageService imageService;
	
	public UserService(UserRepository userRepo, RoleRepository roleRepo, MailService mailService, PasswordEncoder passwordEncoder, ImageService imageService) {
		this.userRepo = userRepo;
		this.roleRepo = roleRepo;
		this.mailService =mailService;
		this.passwordEncoder = passwordEncoder;
		this.imageService = imageService;
	}
	
	public ApplicationUser getUserByusername(String username) {
		return userRepo.findByUsername(username).orElseThrow(UserDoesNotExistsException :: new);
	}
	
	public ApplicationUser updateUser(ApplicationUser user) {
		try {
			return userRepo.save(user);
		}catch(Exception e) {
			throw new EmailAlreadyTakenException();
		}
	}
	
	public ApplicationUser registerUser(RegistrationObject ro) {
		ApplicationUser user = new ApplicationUser();
		user.setFirstName(ro.getFirstName());
		user.setLastName(ro.getLastName());
		user.setEmail(ro.getEmail());
		user.setDateOfBirth(ro.getDob());
		
		String name = user.getFirstName() + user.getLastName();
		boolean nameTaken = true;
		
		String tempName = "";
		while(nameTaken) {
			tempName = generateUsername(name);
			
			if(userRepo.findByUsername(tempName).isEmpty()) {
				nameTaken =false;
			}
		}		
		
		user.setUsername(tempName);
		
		Set<Role> roles = user.getAuthorities();
		roles.add(roleRepo.findByAuthority("USER").get());
		user.setAuthorities(roles);
		
		try {
			return userRepo.save(user);
		}catch(Exception e) {
			throw new EmailAlreadyTakenException();
		}
	}
	
	
	public void generateEmailVerification(String username) {
		ApplicationUser user = userRepo.findByUsername(username).orElseThrow(UserDoesNotExistsException :: new);
		
		user.setVerification(generateVerificationNumber());
		
		try {
			mailService.sendEmail(user.getEmail(), "Your Verification code", "Here is your verification code: " + user.getVerification());
			userRepo.save(user);
		} catch (Exception e) {
			throw new EmailFailedToSendException();
		}
		
	}
	
	public ApplicationUser verifyEmail(String username, Long code) {
		ApplicationUser user = userRepo.findByUsername(username).orElseThrow(UserDoesNotExistsException :: new);

		if(code.equals(user.getVerification())) {
			user.setEnabled(true);
			user.setVerification(null);
			return userRepo.save(user);
		}else {
			throw new IncorrectVerificationCodeException();
		}
		
	}
	
	public ApplicationUser setPassword(String username, String password) {
		ApplicationUser user = userRepo.findByUsername(username).orElseThrow(UserDoesNotExistsException :: new);

		String encodedPassword = passwordEncoder.encode(password);
		
		user.setPassword(encodedPassword);
		return userRepo.save(user);
	}
	
	private String generateUsername(String name) {
		long generatedNumber = (long) Math.floor(Math.random()* 1_000_000_000);
		return name+generatedNumber;
	}

	private long generateVerificationNumber() {
		return (long) Math.floor(Math.random() * 100_000_000);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		ApplicationUser u = userRepo.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found.."));
		Set<GrantedAuthority> authorities = u.getAuthorities().stream().map(role -> new SimpleGrantedAuthority(role.getAuthority()))
				.collect(Collectors.toSet());
		
		UserDetails ud = new User(u.getUsername(), u.getPassword(), authorities);
		
		return ud;
	}

	public ApplicationUser setProfileOrBannerPicture(String username, MultipartFile file, String prefix) throws UnableToSavePhotoException{
		
		ApplicationUser user = userRepo.findByUsername(username).orElseThrow(UserDoesNotExistsException::new);
		
		Image photo = imageService.uploadImage(file, prefix);
		
		try {
			if(prefix.equals("pfp")) {
				if(user.getProfilePicture() != null && user.getProfilePicture().getImageName().equals("defaultpfp.png")) {
					Path p = Paths.get(user.getProfilePicture().getImagePath());
					Files.deleteIfExists(p);
				}
				user.setProfilePicture(photo);
			}else {
				if(user.getBannerPicture() != null && user.getBannerPicture().getImageName().equals("defaultbnr.png")) {
					Path p = Paths.get(user.getBannerPicture().getImagePath());
					Files.deleteIfExists(p);
				}
			}
			user.setBannerPicture(photo);
		}catch(IOException e) {
			throw new UnableToSavePhotoException();
		}
		
		return userRepo.save(user);
	}
	
	public Set<ApplicationUser> followUser(String username, String followee) throws FollowException{
		
		if(username.equals(followee)) throw new FollowException();
		
		ApplicationUser loggedInUser = userRepo.findByUsername(username).orElseThrow(UserDoesNotExistsException::new);
		
		Set<ApplicationUser> followingList = loggedInUser.getFollowing();
		
		ApplicationUser followedUser = userRepo.findByUsername(followee).orElseThrow(UserDoesNotExistsException::new);
		
		Set<ApplicationUser> followersList = followedUser.getFollowers();
		
		//Add the followed user to the following list
		followingList.add(followedUser);
		loggedInUser.setFollowing(followingList);
		
		//Add the current user to the follower list of the followee
		followersList.add(loggedInUser);
		followedUser.setFollowers(followersList);
		
		//update both users
		userRepo.save(loggedInUser);
		userRepo.save(followedUser);
		
		return loggedInUser.getFollowing();
	}

	public Set<ApplicationUser> retriveFollowingList(String username) {
		
		ApplicationUser user = userRepo.findByUsername(username).orElseThrow(UserDoesNotExistsException::new);
		return user.getFollowing();
	}

	public Set<ApplicationUser> retriveFollowersList(String username) {
		
		ApplicationUser user = userRepo.findByUsername(username).orElseThrow(UserDoesNotExistsException::new);
		return user.getFollowers();
	}
	
	public String verifyUsername(FindUsernameDTO credentials) {
		ApplicationUser user = userRepo.findByEmailOrPhoneOrUsername(credentials.getEmail(), credentials.getPhone(), credentials.getUsername())
				.orElseThrow(UserDoesNotExistsException:: new);
		return user.getUsername();
	}
	
	public ApplicationUser getUsersEmailAndPhone(FindUsernameDTO credentials) {
		return userRepo.findByEmailOrPhoneOrUsername(credentials.getEmail(), credentials.getPhone(), credentials.getUsername())
				.orElseThrow(UserDoesNotExistsException::new);
	} 
}

