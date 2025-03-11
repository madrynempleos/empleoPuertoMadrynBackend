package ozoriani.empleomadrynbackend.home.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ozoriani.empleomadrynbackend.home.model.entities.Categoria;
import ozoriani.empleomadrynbackend.home.service.CategoriaService;
import jakarta.validation.Valid;

import java.util.UUID;

@RestController
@RequestMapping("/api/categorias")
@Validated
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @PostMapping
    public ResponseEntity<Categoria> createCategoria(@Valid @RequestBody Categoria categoria) {
        Categoria createdCategoria = categoriaService.createCategoria(categoria);
        return new ResponseEntity<>(createdCategoria, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Iterable<Categoria>> getAllCategorias() {
        return ResponseEntity.ok(categoriaService.getAllCategorias());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categoria> getCategoriaById(@PathVariable UUID id) {
        return ResponseEntity.ok(categoriaService.getCategoriaById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Categoria> updateCategoria(@PathVariable UUID id, @Valid @RequestBody Categoria categoria) {
        return ResponseEntity.ok(categoriaService.updateCategoria(id, categoria));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategoria(@PathVariable UUID id) {
        categoriaService.deleteCategoria(id);
        return ResponseEntity.noContent().build();
    }

}
