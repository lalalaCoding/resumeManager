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
    
    public void deleteObject(String objectName) {
        // The ID of your GCP project
        // String projectId = "your-project-id";

        // The ID of your GCS bucket
        // String bucketName = "your-unique-bucket-name";

        // The ID of your GCS object
        // String objectName = "your-object-name";

        Storage storage = StorageOptions.newBuilder().setProjectId(this.projectId).build().getService(); //스토리지에 접근
        Blob blob = storage.get(this.bucketName, objectName); //버킷과 버킷 내 오브젝트에 접근
        if (blob == null) { //삭제 대상이되는 오브젝트가 버킷 내부에 존재하지 않는 경우
          System.out.println("The object " + objectName + " wasn't found in " + bucketName);
          return;
        }
        BlobId idWithGeneration = blob.getBlobId(); //오브젝트에 대한 id값 조회
        // Deletes the blob specified by its id. When the generation is present and non-null it will be
        // specified in the request.
        // If versioning is enabled on the bucket and the generation is present in the delete request,
        // only the version of the object with the matching generation will be deleted.
        // If instead you want to delete the current version, the generation should be dropped by
        // performing the following.
        // BlobId idWithoutGeneration =
        //    BlobId.of(idWithGeneration.getBucket(), idWithGeneration.getName());
        // storage.delete(idWithoutGeneration);
        storage.delete(idWithGeneration); 

        System.out.println("Object " + objectName + " was permanently deleted from " + bucketName);
      }
    

}