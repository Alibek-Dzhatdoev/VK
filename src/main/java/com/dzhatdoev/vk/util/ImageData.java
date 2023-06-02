package com.dzhatdoev.vk.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@NoArgsConstructor
@Getter
@AllArgsConstructor
public class ImageData {

    private String imageName;
    private String imageBase64;


    public void setImageBase64(String imageName,String imageBase64) {
        this.imageName = imageName;
        this.imageBase64 = imageBase64;
    }

    public static String convertMultipartFileToBase64(MultipartFile multipartFile) {
        try {
            byte[] fileContent = multipartFile.getBytes();
            byte[] encodedBytes = Base64.encodeBase64(fileContent);
            return new String(encodedBytes);
        } catch (IOException e) {
            // Обработка ошибки чтения содержимого файла
            return null;
        }
    }

    public static byte[] convertBase64ToByteArray(String base64String) {
        return Base64.decodeBase64(base64String.getBytes());
    }
}