package com.fwitter.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fwitter.exceptions.UnableToResolvePhotoException;
import com.fwitter.exceptions.UnableToSavePhotoException;
import com.fwitter.services.ImageService;

@RestController
@RequestMapping("/images")
@CrossOrigin("*")
public class ImageController {
	
	@Autowired
	public final ImageService imageService;
	
	public ImageController(ImageService imageService) {
		this.imageService = imageService;
	}
	
	@ExceptionHandler({UnableToResolvePhotoException.class, UnableToSavePhotoException.class})
	public ResponseEntity<String> handlePhotoException(){
		return new ResponseEntity<String>("Unable to process the photo", HttpStatus.NOT_ACCEPTABLE);
	}
	
	@GetMapping("/{filename}")
	public ResponseEntity<byte[]> downloadImage(@PathVariable String filename) throws UnableToResolvePhotoException{
		byte[] imageBytes = imageService.downloadImage(filename);
		return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf(imageService.getImageType(filename)))
				.body(imageBytes);
	}
}
