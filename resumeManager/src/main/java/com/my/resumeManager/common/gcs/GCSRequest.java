package com.my.resumeManager.common.gcs;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data //
public class GCSRequest {
	private String name;
	private MultipartFile file;
}
