package ozoriani.empleomadrynbackend.home.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    public FileStorageService() {
        this.fileStorageLocation = Paths.get("uploads/logos").toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear el directorio para almacenar archivos.", e);
        }
    }

    public String storeFile(MultipartFile file) {
        if (file.isEmpty()) {
            return null;
        }

        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        try {
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation);
            return "/uploads/logos/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Error al almacenar el archivo: " + fileName, e);
        }
    }
}