package ozoriani.empleomadrynbackend.home.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ozoriani.empleomadrynbackend.home.model.entities.OfertaEmpleo;
import ozoriani.empleomadrynbackend.home.model.repository.OfertaEmpleoRepository;
import ozoriani.empleomadrynbackend.home.service.EmailService;

import java.util.UUID;

@RestController
@RequestMapping("/api/applications")
public class PostulacionOfertaController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private OfertaEmpleoRepository ofertaRepository;

    @PostMapping("/apply/{jobId}")
    public ResponseEntity<String> applyForJob(
            @PathVariable String jobId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("applicantEmail") String applicantEmail) {
        try {
            UUID ofertaId = UUID.fromString(jobId);
            OfertaEmpleo oferta = ofertaRepository.findById(ofertaId)
                    .orElseThrow(() -> new IllegalArgumentException("Oferta no encontrada"));

            String fileName = file.getOriginalFilename();
            if (fileName == null || (!fileName.endsWith(".pdf") && !fileName.endsWith(".doc"))) {
                return ResponseEntity.badRequest().body("Solo se permiten archivos .pdf o .doc");
            }
            if (file.getSize() > 5 * 1024 * 1024) { 
                return ResponseEntity.badRequest().body("El archivo no debe superar los 5MB");
            }

            emailService.enviarCorreoPostulacion(oferta, applicantEmail, file.getBytes(), fileName);

            emailService.enviarConfirmacionPostulante(applicantEmail, oferta);

            return ResponseEntity.ok("Postulación enviada con éxito");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al procesar la postulación: " + e.getMessage());
        }
    }
}