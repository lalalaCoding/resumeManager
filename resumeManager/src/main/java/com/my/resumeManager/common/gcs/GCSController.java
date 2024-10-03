package com.my.resumeManager.common.gcs;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.RequiredArgsConstructor;
@Controller
@RequiredArgsConstructor
public class GCSController {
	private final GCSService gcsService;

    @PostMapping("/api/gcs/upload")
    public ResponseEntity<Void> objectUpload(GCSRequest gcsRequest) throws IOException {

        gcsService.uploadObject(gcsRequest);

        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    public void objectDownload() {
    	// 프로젝트 아이디, 버킷 이름, 객체 이름, 저장할 경로가 필요함
    	
    	
    }
    
    
    
}
