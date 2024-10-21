package org.nhathuy.notification_gas24h7.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.firebase.cloud.StorageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class ImageUploadService {
    private final StorageClient storageClient;

    @Autowired
    public ImageUploadService(StorageClient storageClient) {
        this.storageClient = storageClient;
    }
    public String uploadFile(MultipartFile file) throws IOException{
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        BlobId blobId = BlobId.of(storageClient.bucket().getName(), fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(file.getContentType()).build();
        Blob blob = storageClient.bucket().create(fileName, file.getBytes(),file.getContentType());

        String downloadUrl = String.format("https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media",
                storageClient.bucket().getName(),
                fileName.replace("/","%2F"));

        return downloadUrl;
    }
}
