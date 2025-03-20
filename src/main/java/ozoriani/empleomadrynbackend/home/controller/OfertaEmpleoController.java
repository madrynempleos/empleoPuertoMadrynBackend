package ozoriani.empleomadrynbackend.home.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import ozoriani.empleomadrynbackend.home.model.dto.OfertaEmpleoResponseDTO;
import ozoriani.empleomadrynbackend.home.model.entities.OfertaEmpleo;
import ozoriani.empleomadrynbackend.home.service.OfertaEmpleoService;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/ofertas")
public class OfertaEmpleoController {

    private final OfertaEmpleoService ofertaEmpleoService;

    @Autowired
    public OfertaEmpleoController(OfertaEmpleoService ofertaEmpleoService) {
        this.ofertaEmpleoService = ofertaEmpleoService;
    }

    @PostMapping
    public ResponseEntity<OfertaEmpleo> createOferta(@Valid @RequestBody OfertaEmpleo ofertaEmpleo) {
        OfertaEmpleo nuevaOferta = ofertaEmpleoService.createOferta(ofertaEmpleo);
        return ResponseEntity.ok(nuevaOferta);
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

    @PutMapping("/{id}")
    public ResponseEntity<OfertaEmpleo> updateOferta(@PathVariable UUID id,
            @Valid @RequestBody OfertaEmpleo ofertaEmpleo) {
        OfertaEmpleo ofertaActualizada = ofertaEmpleoService.updateOferta(id, ofertaEmpleo);
        return ResponseEntity.ok(ofertaActualizada);
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
}