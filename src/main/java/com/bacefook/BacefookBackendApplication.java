package com.bacefook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.bacefook.model.FileStoragePropertiesModel;

@SpringBootApplication
/**
 * To enable the ConfigurationProperties feature, we
 * add @EnableConfigurationProperties annotation to any configuration class.
 **/
@EnableConfigurationProperties({ FileStoragePropertiesModel.class })
public class BacefookBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BacefookBackendApplication.class, args);
	}

}
