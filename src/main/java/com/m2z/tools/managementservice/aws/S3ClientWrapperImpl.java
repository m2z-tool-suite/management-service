package com.m2z.tools.managementservice.aws;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.net.URL;
import java.time.Duration;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class S3ClientWrapperImpl implements S3ClientWrapper {

    private final S3Client s3Client;

    private final S3Presigner s3Presigner;

    @Value("${ms.aws.s3.bucket}")
    private String defaultBucket;

    @Value("${ms.aws.cognito.aws.s3.multiPartEnabled:false}")
    private boolean multiPartEnabled;

    // PUT
    @Override
    public boolean upload(String key, byte[] bytes) {
        return this.upload(this.defaultBucket, key, bytes);
    }

    @Override
    public boolean upload(String bucket, String key, byte[] bytes) {
        return (multiPartEnabled && bytes.length >= 1024 ? uploadMultipart(bucket, key, bytes) : uploadBasic(bucket, key, bytes));
    }

    protected boolean uploadBasic(String bucket, String key, byte[] bytes) {
        log.info("S3:PUT OBJECT Started bucket: {} key: {}", bucket, key);

        PutObjectRequest objectRequest = PutObjectRequest.builder()
            .bucket(bucket)
            .contentType("image/jpeg")
            .key(key)
            .build();
        s3Client.putObject(objectRequest, RequestBody.fromBytes(bytes));
        return true;
    }

    protected boolean uploadMultipart(String bucket, String key, byte[] bytes) {
        log.info("S3:Multipart:PUT Started bucket: {} key: {}", bucket, key);
        throw new UnsupportedOperationException();
    }

    // DELETE
    @Override
    public boolean delete(String key) {
        return this.delete(defaultBucket, key);
    }

    @Override
    public boolean delete(String bucket, String key) {
        try {
            log.info("S3:DELETE Started bucket: {} key: {}", bucket, key);
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
            return true;
        } catch (NoSuchKeyException e) {
            log.debug("S3:DELETE Caught at NoSuchKeyException");
            return false;
        } catch (S3Exception e) {
            log.debug("S3:DELETE Caught at S3Exception equivalent to NoSuchKey");
            if (e.awsErrorDetails().errorCode().equalsIgnoreCase("NoSuchKey")) {
                return false;
            }
            log.error("S3:DELETE Failed bucket: {} key: {} msg: {}", bucket, key, e.getMessage());
            throw e;
        }
    }

    // GET
    @Override
    public Optional<URL> generateDownloadUrl(String key) {
        return generateDownloadUrl(this.defaultBucket, key);
    }

    /*
    Require object exist check since we can generate pre signed urls for non-existing objects
    This is a purely client side operation
     */
    @Override
    public Optional<URL> generateDownloadUrl(String bucket, String key) {
        if (/*!objectExists(bucket,key)*/false) return Optional.empty();

        return Optional.of(generateDownloadUrlNoCheck(bucket, key));
    }

    @Override
    public URL generateDownloadUrlNoCheck(String key) {
        return generateDownloadUrlNoCheck(this.defaultBucket, key);
    }

    @Override
    public URL generateDownloadUrlNoCheck(String bucket, String key) {
        log.info("S3:GENERATE PRE SIGNED Started bucket: {} key: {}", bucket, key);
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofHours(2))
                .getObjectRequest(getObjectRequest)
                .build();

        PresignedGetObjectRequest presignedGetObjectRequest = s3Presigner.presignGetObject(getObjectPresignRequest);
        return presignedGetObjectRequest.url();
    }

    private boolean objectExists(String bucket, String key) {
        try {
            log.info("S3:OBJECT EXISTS Started bucket: {} key: {}", bucket, key);
            s3Client.headObject(HeadObjectRequest.builder().bucket(bucket).key(key).build());
            return true;
        } catch (NoSuchKeyException e) {
            return false;
        }
    }
}