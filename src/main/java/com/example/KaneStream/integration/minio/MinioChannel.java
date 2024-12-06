package com.example.KaneStream.integration.minio;

import io.minio.*;
import io.minio.http.Method;
import jakarta.annotation.PostConstruct;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class MinioChannel {
    private final MinioClient minioClient;
    private final String BUCKET= "kane-stream";

    @PostConstruct
    public void init() {
        createBucket(BUCKET);
    }

    @SneakyThrows
    private void createBucket(final String name){
        // check if bucket exists
        final boolean found= minioClient.bucketExists(
                BucketExistsArgs.builder()
                        .bucket(name)
                        .build()
        );

        // if not found, create it
        if(!found) {
            minioClient.makeBucket(
                    MakeBucketArgs.builder()
                            .bucket(name)
                            .build()
            );

            final String policy= """
                {
                    "Version": "2012-10-17",
                    "Statement": [
                        {
                            "Effect": "Allow",
                            "Principal": "*",
                            "Action": "s3:GetObject",
                            "Resource": [
                                "arn:aws:s3:::%s/*"
                            ]
                        }
                    ]
                }
                """.formatted(name, name);

            minioClient.setBucketPolicy(
                    SetBucketPolicyArgs.builder()
                            .bucket(name)
                            .config(policy)
                            .build()
            );

        }
        else {
            log.info("Bucket {} already exists: ", name);
        }


    }

    @SneakyThrows
    public String upload(@NonNull final MultipartFile file) {
        log.info("Bucket: {}, file size: {}",BUCKET, file.getSize());
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(BUCKET)
                            .object(file.getOriginalFilename())
                            .contentType(Objects.isNull(file.getContentType()) ? "image/png; image/jpg;" : file.getContentType())
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .build()
            );
        }catch (Exception e) {
            log.error("Error uploading file: ", e);
        }
        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket(BUCKET)
                        .object(file.getOriginalFilename())
                        .build()
        );

    }

}
