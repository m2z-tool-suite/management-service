package com.m2z.tools.managementservice.employee.service;

import com.m2z.tools.managementservice.aws.S3ClientWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfilePictureServiceS3 implements ProfilePictureStorage {
    
    private final S3ClientWrapper s3ClientWrapper;
    
    private final static String picturePrefix = "profile/pictures/";

    private String prependPath(String userId) {
        return String.format("%s%s%s", picturePrefix, userId, ".jpeg");
    }
    
    @Override
    public boolean put(String userId, byte[] bytes) {
        return this.s3ClientWrapper.upload(prependPath(userId), bytes);
    }

    @Override
    public boolean delete(String userId) {
        return this.s3ClientWrapper.delete(prependPath(userId));
    }

    @Override
    public byte[] get(String path) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<URL> generateUrl(String userId) {
        return this.s3ClientWrapper.generateDownloadUrl(prependPath(userId));
    }

    @Override
    public List<Optional<URL>> generateUrl(List<String> userId) {
        throw new UnsupportedOperationException();
    }
}
