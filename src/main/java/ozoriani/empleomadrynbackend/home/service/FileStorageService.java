package ozoriani.empleomadrynbackend.home.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;
    private static final List<String> ALLOWED_CONTENT_TYPES = Arrays.asList("image/png", "image/jpeg", "image/jpg");
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    public FileStorageService(@Value("${file.upload-dir:/uploads/logos}") String uploadDir) {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear el directorio para almacenar archivos.", e);
        }
    }

    public String storeFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        String contentType = file.getContentType();
        if (!ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new IllegalArgumentException("Solo se permiten imágenes en formato PNG, JPG o JPEG.");
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("El tamaño del archivo no puede exceder los 5MB.");
        }
        String fileName = UUID.randomUUID().toString() + "_" + sanitizeFileName(file.getOriginalFilename());
        try {
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation);
            return "/uploads/logos/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Error al almacenar el archivo: " + fileName, e);
        }
    }
    
    public void deleteFile(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return; 
        }
        
        String fileName = fileUrl.replace("/uploads/logos/", "");
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName);
            Files.deleteIfExists(filePath); 
        } catch (IOException e) {
            throw new RuntimeException("Error al eliminar el archivo: " + fileName, e);
        }
    }

    private String sanitizeFileName(String fileName) {
        return fileName != null ? fileName.replaceAll("[^a-zA-Z0-9._-]", "-") : "";
    }
}