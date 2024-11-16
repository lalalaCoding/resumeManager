package com.my.resumeManager.common.gcs;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class GCSService {
	
	@Value("${spring.cloud.gcp.storage.project-id}")
	private String projectId;
    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;
    
//    @Autowired
//    private Environment env; 
    
    
    
    public void uploadObject(GCSRequest gcsRequest) throws IOException {

        String keyFileName = "resumemanager-437401-375b06ed51be.json"; // 나의 json 파일 이름을 지정한다.
        InputStream keyFile = ResourceUtils.getURL("classpath:" + keyFileName).openStream();

        Storage storage = StorageOptions.newBuilder()
                .setCredentials(GoogleCredentials.fromStream(keyFile))
                .build()
                .getService();

        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, gcsRequest.getName())
                .setContentType(gcsRequest.getFile().getContentType()).build(); // 버킷에 저장할 파일 이름을 지정하는 코드

        Blob blob = storage.create(blobInfo, gcsRequest.getFile().getInputStream());
    }
    
    
    public void downloadObject(String objectName, String destFilePath) {
    	log.info("projectId={}",this.projectId);
    	log.info("bucketName={}",this.bucketName);
    	
	    // The ID of your GCP project
	    // String projectId = "your-project-id";
	
	    // The ID of your GCS bucket
	    // String bucketName = "your-unique-bucket-name";
	
	    // The ID of your GCS object
	    // String objectName = "your-object-name";
	
	    // The path to which the file should be downloaded
	    // String destFilePath = "/local/path/to/file.txt";
	
	    Storage storage = StorageOptions.newBuilder().setProjectId(this.projectId).build().getService();
	
	    Blob blob = storage.get(BlobId.of(this.bucketName, objectName));
	    
	    //디렉토리 생성하기
	    File file = new File(destFilePath);
	    file.mkdirs();
	    
	    blob.downloadTo(Paths.get(destFilePath + "/" + objectName));
	    
	    System.out.println(
	        "Downloaded object "
	            + objectName
	            + " from bucket name "
	            + bucketName
	            + " to "
	            + destFilePath);
	  }
    
    
    

}