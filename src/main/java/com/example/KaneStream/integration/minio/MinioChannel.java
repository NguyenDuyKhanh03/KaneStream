package com.example.KaneStream.integration.minio;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.SetBucketPolicyArgs;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MinioChannel {
    private final MinioClient minioClient;

    @PostConstruct
    public void init() {
        createBucket("kane-stream");
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
}
