package com.librarymanagementsystem.service.impl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.librarymanagementsystem.service.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class StorageServiceImpl implements StorageService {

    private final AmazonS3 amazonS3;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        log.info("Start uploading file: {}", file.getOriginalFilename());

        if (file.isEmpty()) {
            log.error("File is empty: {}", file.getOriginalFilename());
            throw new IOException("Cannot upload empty file");
        }

        File convertedFile = null;
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        try {
            convertedFile = convertMultiPartToFile(file);
            log.debug("Multipart file converted to File: {}", convertedFile.getName());

            log.info("Uploading file to S3 bucket: {}, as name: {}", bucketName, fileName);
            amazonS3.putObject(new PutObjectRequest(bucketName, fileName, convertedFile));
            log.info("Successfully uploaded file: {}", fileName);

            return fileName;
        } catch (IOException ex) {
            log.error("IOException occurred while uploading file: {}", file.getOriginalFilename(), ex);
            throw ex;
        } catch (com.librarymanagementsystem.exception.AmazonServiceException ase) {
            log.error("AmazonServiceException occurred while uploading file: {}, Error Message: {}",
                    file.getOriginalFilename(), ase.getMessage(), ase);
            throw new IOException("Error uploading file to S3: " + ase.getMessage(), ase);
        } finally {

            if (convertedFile != null && convertedFile.exists()) {
                boolean deleted = convertedFile.delete();
                if (!deleted) {
                    log.warn("Temporary file could not be deleted: {}", convertedFile.getAbsolutePath());
                }
            }
        }
    }

    @Override
    public byte[] downloadFile(String fileName) throws IOException {
        log.info("Start downloading file: {} from bucket: {}", fileName, bucketName);
        try {
            S3Object s3Object = amazonS3.getObject(bucketName, fileName);
            log.debug("S3Object retrieved for file: {}", fileName);

            try (S3ObjectInputStream inputStream = s3Object.getObjectContent()) {
                byte[] content = IOUtils.toByteArray(inputStream);
                log.info("Successfully downloaded file: {} ({} bytes)", fileName, content.length);
                return content;
            }
        } catch (AmazonServiceException ase) {
            log.error("AmazonServiceException occurred while downloading file: {}, Error Message: {}",
                    fileName, ase.getMessage(), ase);
            throw new IOException("Error downloading file from S3: " + ase.getMessage(), ase);
        }
    }

    @Override
    public String deleteFile(String fileName) throws IOException {
        log.info("Start deleting file: {} from bucket: {}", fileName, bucketName);
        try {
            amazonS3.deleteObject(bucketName, fileName);
            log.info("Successfully deleted file: {} from bucket: {}", fileName, bucketName);
            return fileName + " was removed";
        } catch (AmazonServiceException ase) {
            log.error("AmazonServiceException occurred while deleting file: {}, Error Message: {}",
                    fileName, ase.getMessage(), ase);
            throw new IOException("Error deleting file from S3: " + ase.getMessage(), ase);
        }
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        log.debug("Converting MultipartFile to File...");
        File convFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(file.getBytes());
        } catch (IOException ex) {
            log.error("Error converting MultipartFile to File: {}", file.getOriginalFilename(), ex);
            throw ex;
        }
        return convFile;
    }
}
