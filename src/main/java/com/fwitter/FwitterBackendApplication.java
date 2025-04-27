package com.fwitter;


import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fwitter.config.RSAKeyProperties;
import com.fwitter.models.ApplicationUser;
import com.fwitter.models.Role;
import com.fwitter.repositories.RoleRepository;
import com.fwitter.repositories.UserRepository;


@EnableConfigurationProperties(RSAKeyProperties.class)
@SpringBootApplication
@ComponentScan(basePackages = "com.fwitter")
public class FwitterBackendApplication {



	public static void main(String[] args) {
		SpringApplication.run(FwitterBackendApplication.class, args);
	}
 
	@Bean
	CommandLineRunner run(RoleRepository roleRepo, UserRepository userRepo, PasswordEncoder encoder) {
		return args -> { 
			Role r = roleRepo.save( new Role(null, "USER"));
			
			Set<Role> roles = new HashSet<>();
			
			roles.add(r);
			
			ApplicationUser u = new ApplicationUser();
			u.setAuthorities(roles);
			u.setFirstName("System");
			u.setLastName("User");
			u.setEmail("xeyec20564@astimei.com");
			u.setUsername("system_user");
			u.setPhone("9999999999");
			u.setPassword(encoder.encode("password"));
			u.setEnabled(true);
			
			userRepo.save(u);
		};
	}
}
