package com.bacefook.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class FileController {
	
	private static final Logger logger = LoggerFactory.getLogger(FileController.class);

	public static Logger getLogger() {
		return logger;
	}
	
}
