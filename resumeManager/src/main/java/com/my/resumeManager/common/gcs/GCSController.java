package com.my.resumeManager.common.gcs;

import java.io.IOException;
import java.util.HashMap;

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
    
    public void objectDownload(HashMap<String, String> profileMap) {
    	// 프로젝트 아이디, 버킷 이름 : application.properties에 존재 -> 서비스단에서 해결
    	// 객체 이름, 저장할 경로가 필요함 : db에 존재 -> MemberController에서 전달 받아야함
    	gcsService.downloadObject(profileMap.get("objectName"), profileMap.get("destFilePath"));
    }
    
    
    
}
