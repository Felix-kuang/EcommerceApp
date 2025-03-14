package com.example.ecommerce.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/v1/")
public class FileUploadController {
    private final Path productUploadDir = Paths.get("uploads/products");
    private final Path userUploadDir = Paths.get("uploads/userProfile");

    public FileUploadController() throws IOException {
        Files.createDirectories(productUploadDir);
        Files.createDirectories(userUploadDir);
    }

    @PostMapping("/products/{id}/upload")
    public ResponseEntity<String> uploadProductImage(@PathVariable Long id, @RequestParam MultipartFile file) {
        return uploadFile(file, productUploadDir);
    }

    @PostMapping("/users/{id}/upload")
    public ResponseEntity<String> uploadUserProfileImage(@PathVariable Long id,
            @RequestParam MultipartFile file) {
        return uploadFile(file, userUploadDir);
    }

    private ResponseEntity<String> uploadFile(MultipartFile file, Path uploadDir) {
        try {
            String fileName = UUID.randomUUID().toString() + "-" + StringUtils.cleanPath(file.getOriginalFilename());
            Path targetLocation = uploadDir.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            String filePath = "/" + uploadDir.getFileName() + "/" + fileName;
            return ResponseEntity.ok("Image uploaded successfully: " + filePath);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image");
        }
    }

    @GetMapping("/images/{folder}/{filename}")
    public ResponseEntity<Resource> getImage(@PathVariable String folder, @PathVariable String filename) {
        try {
            Path filePath = Paths.get("uploads", folder, filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(filePath))
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
