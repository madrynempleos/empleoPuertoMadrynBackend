package ozoriani.empleomadrynbackend.service.impl;

import org.springframework.stereotype.Service;
import ozoriani.empleomadrynbackend.errors.exception.InvalidOperationException;
import ozoriani.empleomadrynbackend.errors.exception.ResourceNotFoundException;
import ozoriani.empleomadrynbackend.model.Categoria;
import ozoriani.empleomadrynbackend.repository.CategoriaRepository;
import ozoriani.empleomadrynbackend.service.CategoriaService;

import java.util.List;
import java.util.UUID;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaServiceImpl(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public Categoria createCategoria(Categoria categoria) {
        if (categoria == null)
            throw new InvalidOperationException("Categoria cannot be null");
        if (categoria.getNombre() == null || categoria.getNombre().isEmpty())
            throw new InvalidOperationException("Categoria name cannot be null or empty");

        return categoriaRepository.save(categoria);
    }

    @Override
    public Categoria getCategoriaById(UUID id) {
        return categoriaRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Categoria not found"));
    }

    @Override
    public List<Categoria> getAllCategorias() {
        return categoriaRepository.findAll();
    }

    @Override
    public Categoria updateCategoria(UUID id, Categoria categoria) {
        if (categoria == null)
            throw new InvalidOperationException("Categoria cannot be null");

        Categoria categoriaToUpdate = categoriaRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Categoria not found"));

        updateValues(categoriaToUpdate, categoria);

        return categoriaRepository.save(categoriaToUpdate);
    }

    @Override
    public void deleteCategoria(UUID id) {
        categoriaRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Categoria not found"));

        categoriaRepository.deleteById(id);
    }

    private void updateValues(Categoria categoriaToUpdate, Categoria categoria) {
        categoriaToUpdate.setNombre(categoria.getNombre());
    }

}
