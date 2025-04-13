package com.fwitter.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fwitter.models.Image;

public interface ImageRepository extends JpaRepository<Image, Long>{
	
	Optional<Image> findByImageName(String ImageName);

}
