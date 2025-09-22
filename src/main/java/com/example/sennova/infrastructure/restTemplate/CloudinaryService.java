package com.example.sennova.infrastructure.restTemplate;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String uploadImage(MultipartFile image) {
        try {
            Map uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
            return uploadResult.get("secure_url").toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Map deleteFileByUrl(String url) throws IOException {
        String publicId = extractPublicIdFromUrl(url);
        return cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }

    private String extractPublicIdFromUrl(String url) {

        String[] parts = url.split("/upload/");
        if (parts.length < 2) {
            throw new IllegalArgumentException("URL inválida de Cloudinary: " + url);
        }

        String path = parts[1];


        if (path.startsWith("v")) {
            path = path.substring(path.indexOf("/") + 1);
        }


        int dotIndex = path.lastIndexOf(".");
        if (dotIndex > 0) {
            path = path.substring(0, dotIndex);
        }

        return path;
    }

}
