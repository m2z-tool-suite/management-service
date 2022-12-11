package com.m2z.tools.managementservice.employee.service;

import java.net.URL;
import java.util.List;
import java.util.Optional;

public interface ProfilePictureStorage {

    boolean put(String userId, byte[] bytes);

    boolean delete(String userId);
    
    byte[] get(String path) throws Exception;

    Optional<URL> generateUrl(String userId);
    
    /*
    Returns all url in the equivalent index position in the new list null if file not found
     */
    List<Optional<URL>> generateUrl(List<String> userId);
}
