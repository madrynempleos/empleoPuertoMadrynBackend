package ozoriani.empleomadrynbackend.home.service;

import java.util.List;
import java.util.UUID;

import ozoriani.empleomadrynbackend.home.model.entities.Categoria;

public interface CategoriaService {
    Categoria createCategoria(Categoria categoria);

    Categoria getCategoriaById(UUID id);

    List<Categoria> getAllCategorias();

    Categoria updateCategoria(UUID id, Categoria categoria);

    void deleteCategoria(UUID id);
}
