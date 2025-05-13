package com.fwitter.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fwitter.exceptions.UnableToResolvePhotoException;
import com.fwitter.exceptions.UnableToSavePhotoException;
import com.fwitter.models.Image;
import com.fwitter.repositories.ImageRepository;

@Service
@Transactional
public class ImageService {
	
	@Autowired
	private final ImageRepository imageRepository;
	
	private static final File DIRECTORY = new File("C:\\PROJECTS\\fwitter\\backend\\fwitter-backend\\img");
	private static final String URL = "http://localhost:8000/images/";
	
	public ImageService(ImageRepository imageRepository) {
		this.imageRepository = imageRepository;
	}
	
	public Image saveGifFromPost(Image image) {
		return imageRepository.save(image);
	}
	
	public Image uploadImage(MultipartFile file, String perfix) throws UnableToSavePhotoException {
		try {
			//The content type from the request looks something like this img/jpeg
			String extension = "." + file.getContentType().split("/")[1];
			File img = File.createTempFile(perfix, extension, DIRECTORY);
			
			file.transferTo(img);
			String imageURL = URL + img.getName();
			
			Image i = new Image(img.getName(), file.getContentType(), img.getPath(), imageURL);
			
			Image saved = imageRepository.save(i);
			
			return saved;
			
		}catch(IOException e) {
			throw new UnableToSavePhotoException();
		}
	}
	
	public byte[] downloadImage(String filename) throws UnableToResolvePhotoException{
		try {
			Image image = imageRepository.findByImageName(filename).get();
			
			String filePath = image.getImagePath();
			
			byte[] imageBytes = Files.readAllBytes(new File(filePath).toPath());
			
			return imageBytes;
		}catch(IOException e) {
			throw new UnableToResolvePhotoException();
		}
	}
	
	public String getImageType(String filename) {
		Image image = imageRepository.findByImageName(filename).get();
		
		return image.getImageType();
	}
 }
