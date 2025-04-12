package com.fwitter;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.fwitter.models.ApplicationUser;
import com.fwitter.models.RegistrationObject;
import com.fwitter.models.Role;
import com.fwitter.repositories.RoleRepository;
import com.fwitter.services.UserService;

@SpringBootApplication
@ComponentScan(basePackages = "com.fwitter")
public class FwitterBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(FwitterBackendApplication.class, args);
	}
 
	@Bean
	CommandLineRunner run(RoleRepository roleRepo, UserService userService) {
		return args -> { 
			roleRepo.save( new Role(null, "USER"));
//			ApplicationUser u = new ApplicationUser();
			RegistrationObject ro = new RegistrationObject();
			ro.setFirstName("Ankush");
			ro.setLastName("Sharma");
			userService.registerUser(ro);
		};
	}
}
