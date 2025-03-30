package ozoriani.empleomadrynbackend.home.controller;

import java.util.List;
import java.util.UUID;

import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ozoriani.empleomadrynbackend.home.service.AdminService;

@RestController
@RequestMapping("/api/admin")
@Validated
public class AdminController {
    private final AdminService adminService;

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/{entityName}")
    public ResponseEntity<List<?>> getAllFromEntity(@PathVariable String entityName) {
        return ResponseEntity.ok(adminService.getAllFromEntity(entityName));
    }

    @GetMapping("/{entityName}/{id}")
    public ResponseEntity<Object> getByIdFromEntity(@PathVariable String entityName, @PathVariable UUID id) {
        return ResponseEntity.ok(adminService.getByIdFromEntity(entityName, id));
    }

    @PostMapping("/{entityName}/{id}")
    public ResponseEntity<Object> updateEntity(
            @PathVariable String entityName,
            @PathVariable UUID id,
            @RequestBody Object updatedEntity) {
        return ResponseEntity.ok(adminService.updateEntity(entityName, id, updatedEntity));
    }

    @DeleteMapping("/{entityName}/{id}")
    public ResponseEntity<String> deleteEntity(@PathVariable String entityName, @PathVariable UUID id) {
        adminService.deleteEntity(entityName, id);
        return ResponseEntity.ok(entityName + " con ID " + id + " eliminada");
    }

    @PostMapping("/ofertas/habilitar/{id}")
    public ResponseEntity<String> enableJobOffer(@PathVariable UUID id) {
        logger.info("Iniciando habilitación de oferta con ID: {}", id);
        System.out.println("Habilitando oferta de empleo con ID: " + id);
        try {
            adminService.enableJobOffer(id);
            logger.info("Oferta habilitada con éxito para ID: {}", id);
            return ResponseEntity.ok("Oferta de empleo habilitada con ID: " + id);
        } catch (RuntimeException e) {
            logger.error("Excepción manejada: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.SC_NOT_FOUND)
                    .body("Oferta no encontrada: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Error inesperado al habilitar oferta con ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                    .body("Error al habilitar la oferta: " + e.getMessage());
        }
    }
    
}