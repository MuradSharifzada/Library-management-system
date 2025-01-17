package com.librarymanagementsystem.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StorageService {

    String uploadFile(MultipartFile file) throws IOException;

    byte[] downloadFile(String fileName) throws IOException;

    String deleteFile(String fileName) throws IOException;
}
