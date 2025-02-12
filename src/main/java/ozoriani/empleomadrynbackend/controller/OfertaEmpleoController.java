package ozoriani.empleomadrynbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ozoriani.empleomadrynbackend.model.OfertaEmpleo;
import ozoriani.empleomadrynbackend.service.OfertaEmpleoService;
import ozoriani.empleomadrynbackend.dto.OfertaEmpleoResponseDTO;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/ofertas")
public class OfertaEmpleoController {

    @Autowired
    private OfertaEmpleoService ofertaEmpleoService;

    // Crear una nueva oferta de empleo
    @PostMapping
    public ResponseEntity<OfertaEmpleo> createOferta(@RequestBody OfertaEmpleo ofertaEmpleo) {
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
        OfertaEmpleoResponseDTO oferta = ofertaEmpleoService.getOfertaById(id);
        return ResponseEntity.ok(oferta);
    }

    // Actualizar una oferta de empleo
    @PutMapping("/{id}")
    public ResponseEntity<OfertaEmpleo> updateOferta(@PathVariable UUID id, @RequestBody OfertaEmpleo ofertaEmpleo) {
        OfertaEmpleo ofertaActualizada = ofertaEmpleoService.updateOferta(id, ofertaEmpleo);
        if (ofertaActualizada != null) {
            return ResponseEntity.ok(ofertaActualizada);
        } else {
            return ResponseEntity.notFound().build();
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
