package ozoriani.empleomadrynbackend.home.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import ozoriani.empleomadrynbackend.home.model.dto.OfertaEmpleoResponseDTO;
import ozoriani.empleomadrynbackend.home.model.entities.OfertaEmpleo;
import ozoriani.empleomadrynbackend.home.service.FileStorageService;
import ozoriani.empleomadrynbackend.home.service.OfertaEmpleoService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/ofertas")
public class OfertaEmpleoController {

    private final OfertaEmpleoService ofertaEmpleoService;
    private final FileStorageService fileStorageService;
    private final ObjectMapper objectMapper;

    public OfertaEmpleoController(OfertaEmpleoService ofertaEmpleoService, FileStorageService fileStorageService,
            ObjectMapper objectMapper) {
        this.ofertaEmpleoService = ofertaEmpleoService;
        this.fileStorageService = fileStorageService;
        this.objectMapper = objectMapper;
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @PostMapping(consumes = { "multipart/form-data" })
    public ResponseEntity<?> createOferta(
            @RequestPart("oferta") String ofertaJson,
            @RequestPart(value = "logo", required = false) MultipartFile logo) {
        try {
            OfertaEmpleo ofertaEmpleo = objectMapper.readValue(ofertaJson, OfertaEmpleo.class);

            if (logo != null && !logo.isEmpty()) {
                String logoUrl = fileStorageService.storeFile(logo);
                ofertaEmpleo.setLogoUrl(logoUrl);
            }
            OfertaEmpleo nuevaOferta = ofertaEmpleoService.createOferta(ofertaEmpleo);
            return ResponseEntity.ok(nuevaOferta);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorResponse.put("message", "Error al crear la oferta: " + e.getMessage());
            errorResponse.put("path", "Ocurrió un error inesperado");
            errorResponse.put("error", e.getClass().getSimpleName());
            errorResponse.put("timestamp", LocalDateTime.now().toString());
            e.printStackTrace();
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<OfertaEmpleoResponseDTO>> getAllOfertas() {
        List<OfertaEmpleoResponseDTO> ofertas = ofertaEmpleoService.getAllOfertas();
        return ResponseEntity.ok(ofertas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OfertaEmpleoResponseDTO> getOfertaById(@PathVariable UUID id) {
        OfertaEmpleoResponseDTO oferta = ofertaEmpleoService.getOfertaById(id);
        return ResponseEntity.ok(oferta);
    }

    @PutMapping(value = "/{id}", consumes = { "multipart/form-data" })
    public ResponseEntity<?> updateOferta(
            @PathVariable UUID id,
            @RequestPart("oferta") String ofertaJson,
            @RequestPart(value = "logo", required = false) MultipartFile logo,
            @RequestPart(value = "logoUrl", required = false) String logoUrl) {
        try {
            OfertaEmpleo ofertaEmpleo = objectMapper.readValue(ofertaJson, OfertaEmpleo.class);

            if (logo != null && !logo.isEmpty()) {
                String newLogoUrl = fileStorageService.storeFile(logo);
                ofertaEmpleo.setLogoUrl(newLogoUrl);
            } else if (logoUrl != null && logoUrl.equals("null")) {
                ofertaEmpleo.setLogoUrl(null);
            } else {
                ofertaEmpleo.setLogoUrl(logoUrl);
            }

            OfertaEmpleo ofertaActualizada = ofertaEmpleoService.updateOferta(id, ofertaEmpleo);
            return ResponseEntity.ok(ofertaActualizada);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorResponse.put("message", "Error al actualizar la oferta: " + e.getMessage());
            errorResponse.put("path", "Ocurrió un error inesperado");
            errorResponse.put("error", e.getClass().getSimpleName());
            errorResponse.put("timestamp", LocalDateTime.now().toString());
            e.printStackTrace();
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOferta(@PathVariable UUID id) {
        ofertaEmpleoService.deleteOferta(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/mis-avisos")
    public ResponseEntity<List<OfertaEmpleoResponseDTO>> getUserJobPosts(Authentication authentication) {
        String userEmail = authentication.getName();
        List<OfertaEmpleoResponseDTO> userPosts = ofertaEmpleoService.getUserJobPosts(userEmail);
        return ResponseEntity.ok(userPosts);
    }

    @GetMapping("/no-habilitadas")
    public ResponseEntity<List<OfertaEmpleoResponseDTO>> getOfertasNoHabilitadas() {
        List<OfertaEmpleoResponseDTO> ofertas = ofertaEmpleoService.getOfertasNoHabilitadas();
        return ResponseEntity.ok(ofertas);
    }

    @PatchMapping("/{id}/habilitar")
    public ResponseEntity<OfertaEmpleoResponseDTO> habilitarOferta(@PathVariable UUID id) {
        OfertaEmpleo oferta = ofertaEmpleoService.habilitarOferta(id);
        return ResponseEntity.ok(ofertaEmpleoService.convertToDTO(oferta)); 
    }
}