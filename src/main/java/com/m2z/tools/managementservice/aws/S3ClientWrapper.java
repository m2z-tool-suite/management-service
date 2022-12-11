package com.m2z.tools.managementservice.aws;

import java.net.URL;
import java.util.Optional;

public interface S3ClientWrapper {
    boolean upload(String key, byte[] bytes);

    boolean upload(String bucket, String key, byte[] bytes);

    boolean delete(String key);

    boolean delete(String bucket, String key);

    Optional<URL> generateDownloadUrl(String key);

    Optional<URL> generateDownloadUrl(String bucket, String key);
}
