package com.dzhatdoev.vk.util.GoogleCloudStorage;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;

@Component
public class CloudStorageService {
    private final Storage storage;

    private static final String bucketName = "vk_task_bucket";

    public CloudStorageService() throws IOException {
        // Инициализация объекта Storage
        String credentialsPath = "src/main/resources/static/key.json";
        Storage storage = null;
        try (FileInputStream credentialsStream = new FileInputStream(credentialsPath)) {
            GoogleCredentials credentials = GoogleCredentials.fromStream(credentialsStream);
            storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.storage = storage;
    }

    //=============================================================================================================

    public String uploadImage(MultipartFile image, String imageName) throws IOException {
        // Получение данных изображения из объекта ImageData
//        byte[] imageDataBytes = Base64.getDecoder().decode(convertMultipartFileToBase64(image));
        byte[] imageDataBytes = image.getBytes();

        BlobId blobId = BlobId.of(bucketName, imageName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        storage.create(blobInfo, imageDataBytes);
        return imageName;
    }

    //=============================================================================================================

    public String downloadImage(String imageName) {
        // Извлечение имени и ведра (bucket) из пути к изображению
        Blob blob = storage.get(bucketName, imageName);
        // Чтение данных изображения в массив байтов
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        if (blob == null) return null;
        blob.downloadTo(outputStream);
        return Base64.getEncoder().encodeToString(outputStream.toByteArray());
    }

    //=============================================================================================================

}