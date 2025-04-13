package com.fwitter.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Images")
public class Image {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "image_id")
	private Long imageId;
	
	@Column(name = "image_name", unique = true)
	private String imageName;
	
	@Column(name = "image_type")
	private String imageType;
	
	@Column(name = "image_path")
	@JsonIgnore
	private String imagePath;
	
	@Column(name = "image_url")
	private String imageUrl;

	public Image() {
		super();
	}

	public Image(String imageName, String imageType, String imagePath, String imageUrl) {
		super();
		this.imageName = imageName;
		this.imageType = imageType;
		this.imagePath = imagePath;
		this.imageUrl = imageUrl;
	}

	public Image(Long imageId, String imageName, String imageType, String imagePath, String imageUrl) {
		super();
		this.imageId = imageId;
		this.imageName = imageName;
		this.imageType = imageType;
		this.imagePath = imagePath;
		this.imageUrl = imageUrl;
	}

	public Long getImageId() {
		return imageId;
	}

	public void setImageId(Long imageId) {
		this.imageId = imageId;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public String getImageType() {
		return imageType;
	}

	public void setImageType(String imageType) {
		this.imageType = imageType;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	@Override
	public String toString() {
		return "Image [imageId=" + imageId + ", imageName=" + imageName + ", imageType=" + imageType + ", imagePath="
				+ imagePath + ", imageUrl=" + imageUrl + "]";
	}
	
	
	
}
