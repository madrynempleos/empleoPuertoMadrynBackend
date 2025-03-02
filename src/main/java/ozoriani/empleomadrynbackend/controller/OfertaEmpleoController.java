package ozoriani.empleomadrynbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ozoriani.empleomadrynbackend.model.OfertaEmpleo;
import ozoriani.empleomadrynbackend.service.OfertaEmpleoService;
import ozoriani.empleomadrynbackend.dto.OfertaEmpleoResponseDTO;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/ofertas")
public class OfertaEmpleoController {

    @Autowired
    private OfertaEmpleoService ofertaEmpleoService;

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
    public ResponseEntity<OfertaEmpleo> updateOferta(@PathVariable UUID id, @Valid @RequestBody OfertaEmpleo ofertaEmpleo) {
        OfertaEmpleo ofertaActualizada = ofertaEmpleoService.updateOferta(id, ofertaEmpleo);
        return ResponseEntity.ok(ofertaActualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOferta(@PathVariable UUID id) {
        ofertaEmpleoService.deleteOferta(id); // Esto lanzará ResourceNotFoundException si no existe
        return ResponseEntity.noContent().build();
    }
}