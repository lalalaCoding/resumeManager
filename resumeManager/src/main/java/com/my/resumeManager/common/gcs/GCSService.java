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

@Service
public class GCSService {

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;

    public void uploadObject(GCSRequest gcsRequest) throws IOException {

        String keyFileName = "resumemanager-437401-3c8b8af994b7.json"; // 나의 json 파일 이름을 지정한다.
        InputStream keyFile = ResourceUtils.getURL("classpath:" + keyFileName).openStream();

        Storage storage = StorageOptions.newBuilder()
                .setCredentials(GoogleCredentials.fromStream(keyFile))
                .build()
                .getService();

        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, gcsRequest.getName())
                .setContentType(gcsRequest.getFile().getContentType()).build(); // 버킷에 저장할 파일 이름을 지정하는 코드

        Blob blob = storage.create(blobInfo, gcsRequest.getFile().getInputStream());
        

    }

}