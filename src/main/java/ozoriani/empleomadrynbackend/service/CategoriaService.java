package ozoriani.empleomadrynbackend.service;

import ozoriani.empleomadrynbackend.model.Categoria;

import java.util.List;
import java.util.UUID;

public interface CategoriaService {
    Categoria createCategoria(Categoria categoria);
    Categoria getCategoriaById(UUID id);
    List<Categoria> getAllCategorias();
    Categoria updateCategoria(UUID id, Categoria categoria);
    void deleteCategoria(UUID id);
}
