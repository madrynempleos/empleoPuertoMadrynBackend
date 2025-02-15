package ozoriani.empleomadrynbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ozoriani.empleomadrynbackend.model.OfertaEmpleo;
import ozoriani.empleomadrynbackend.service.OfertaEmpleoService;
import ozoriani.empleomadrynbackend.dto.OfertaEmpleoResponseDTO;
import ozoriani.empleomadrynbackend.errors.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/ofertas")
public class OfertaEmpleoController {

    @Autowired
    private OfertaEmpleoService ofertaEmpleoService;

    // Crear una nueva oferta de empleo
    @PostMapping
    public ResponseEntity<OfertaEmpleo> createOferta(@Valid @RequestBody OfertaEmpleo ofertaEmpleo) {
        OfertaEmpleo nuevaOferta = ofertaEmpleoService.createOferta(ofertaEmpleo);
        return ResponseEntity.ok(nuevaOferta);
    }

    // Obtener todas las ofertas de empleo
    @GetMapping
    public ResponseEntity<List<OfertaEmpleoResponseDTO>> getAllOfertas() {
        List<OfertaEmpleoResponseDTO> ofertas = ofertaEmpleoService.getAllOfertas();
        return ResponseEntity.ok(ofertas);
    }

    // Obtener una oferta de empleo por ID
    @GetMapping("/{id}")
    public ResponseEntity<OfertaEmpleoResponseDTO> getOfertaById(@PathVariable UUID id) {
        try {
            OfertaEmpleoResponseDTO oferta = ofertaEmpleoService.getOfertaById(id);
            return ResponseEntity.ok(oferta);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Actualizar una oferta de empleo
    @PutMapping("/{id}")
    public ResponseEntity<OfertaEmpleo> updateOferta(@PathVariable UUID id, @Valid @RequestBody OfertaEmpleo ofertaEmpleo) {
        try {
            OfertaEmpleo ofertaActualizada = ofertaEmpleoService.updateOferta(id, ofertaEmpleo);
            return ResponseEntity.ok(ofertaActualizada);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Eliminar una oferta de empleo
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOferta(@PathVariable UUID id) {
        boolean eliminado = ofertaEmpleoService.deleteOferta(id);
        if (eliminado) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Void> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
}
