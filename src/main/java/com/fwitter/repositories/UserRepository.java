package com.fwitter.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fwitter.models.ApplicationUser;

@Repository
public interface UserRepository extends JpaRepository<ApplicationUser, Integer>{

	Optional<ApplicationUser> findByUsername(String username);
	Optional<ApplicationUser> findByEmailOrPhoneOrUsername(String email, String phone, String username);
}
