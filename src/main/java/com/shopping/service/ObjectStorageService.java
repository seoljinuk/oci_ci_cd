package com.shopping.service;

import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.requests.GetBucketRequest;
import com.oracle.bmc.objectstorage.requests.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ObjectStorageService {

    private final ObjectStorageClient objectStorageClient;

    @Value("${oci.object-storage.namespace}")
    private String namespaceName;

    @Value("${oci.object-storage.bucket-name}")
    private String bucketName;

    @Value("${oci.object-storage.region}")
    private String region;

    public ObjectStorageService(ObjectStorageClient objectStorageClient) {
        this.objectStorageClient = objectStorageClient;
    }

    public void checkBucket() {

        GetBucketRequest request =
                GetBucketRequest.builder()
                        .namespaceName(namespaceName)
                        .bucketName(bucketName)
                        .build();

        objectStorageClient.getBucket(request);

        System.out.println("Bucket 확인 성공!");
    }

    public String uploadImage(MultipartFile file, String objectName)
            throws IOException {

        PutObjectRequest request = PutObjectRequest.builder()
                .namespaceName(namespaceName)
                .bucketName(bucketName)
                .objectName(objectName)
                .contentType(file.getContentType())
                .putObjectBody(file.getInputStream())
                .build();

        System.out.println("[" + "namespaceName" + "]");
        System.out.println("[" + namespaceName + "]");

        System.out.println("[" + "bucketName" + "]");
        System.out.println("[" + bucketName + "]");

        System.out.println("[" + "region" + "]");
        System.out.println("[" + region + "]");

        objectStorageClient.putObject(request);

        // OCI Object Storage 이미지 URL
        return String.format(
                "https://objectstorage.%s.oraclecloud.com/n/%s/b/%s/o/%s",
                region,
                namespaceName,
                bucketName,
                objectName
        );
    }
}