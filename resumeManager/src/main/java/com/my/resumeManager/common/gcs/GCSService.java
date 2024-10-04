package com.my.resumeManager.common.gcs;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import com.google.cloud.storage.BlobId; // 다운로드
import java.nio.file.Paths; // 다운로드

@Service
public class GCSService {

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;

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
    
    
    public static void downloadObject(
    	      String projectId, String bucketName, String objectName, String destFilePath) {
	    // The ID of your GCP project
	    // String projectId = "your-project-id";
	
	    // The ID of your GCS bucket
	    // String bucketName = "your-unique-bucket-name";
	
	    // The ID of your GCS object
	    // String objectName = "your-object-name";
	
	    // The path to which the file should be downloaded
	    // String destFilePath = "/local/path/to/file.txt";
	
	    Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
	
	    Blob blob = storage.get(BlobId.of(bucketName, objectName));
	    blob.downloadTo(Paths.get(destFilePath));
	
	    System.out.println(
	        "Downloaded object "
	            + objectName
	            + " from bucket name "
	            + bucketName
	            + " to "
	            + destFilePath);
	  }
    
    
    

}