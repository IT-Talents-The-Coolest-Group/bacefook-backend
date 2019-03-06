package com.bacefook.model;

import org.springframework.boot.context.properties.ConfigurationProperties;

//Adding spring boot configuration dependency to pom.xml
@ConfigurationProperties(prefix = "file")
/**
 * The @ConfigurationProperties(prefix = "file") annotation does its job on
 * application startup and binds all the properties with prefix file to the
 * corresponding fields of the POJO class.
 * 
 * If you define additional file properties in future, you may simply add a
 * corresponding field in the above class, and spring boot will automatically
 * bind the field with the property value.
 **/
public class FileStoragePropertiesModel {
	private String uploadDir;

	public String getUploadDir() {
		return uploadDir;
	}

	public void setUploadDir(String uploadDir) {
		this.uploadDir = uploadDir;
	}
}
