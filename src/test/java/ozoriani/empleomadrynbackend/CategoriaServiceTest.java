package ozoriani.empleomadrynbackend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ozoriani.empleomadrynbackend.errors.exception.ResourceNotFoundException;
import ozoriani.empleomadrynbackend.model.Categoria;
import ozoriani.empleomadrynbackend.repository.CategoriaRepository;
import ozoriani.empleomadrynbackend.service.impl.CategoriaServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaServiceImpl categoriaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createCategoria() {
        UUID id = UUID.randomUUID();
        Categoria categoria = new Categoria(id, "Tecnología");
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoria);

        Categoria resultado = categoriaService.createCategoria(categoria);

        verify(categoriaRepository).save(categoria);
        assertNotNull(resultado);
        assertEquals("Tecnología", resultado.getNombre());
    }

    @Test
    public void getCategoriaById() {
        UUID id = UUID.randomUUID();
        Categoria categoria = new Categoria(id, "Salud");
        when(categoriaRepository.findById(id)).thenReturn(Optional.of(categoria));

        Categoria resultado = categoriaService.getCategoriaById(id);

        verify(categoriaRepository).findById(id);
        assertNotNull(resultado);
        assertEquals("Salud", resultado.getNombre());
    }

    @Test
    public void getCategoriaById_NotFound() {
        UUID id = UUID.randomUUID();
        when(categoriaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> categoriaService.getCategoriaById(id));
    }

    @Test
    public void getAllCategorias() {
        List<Categoria> categorias = Arrays.asList(
                new Categoria(UUID.randomUUID(), "Arte"),
                new Categoria(UUID.randomUUID(), "Ciencia")
        );
        when(categoriaRepository.findAll()).thenReturn(categorias);

        List<Categoria> resultado = categoriaService.getAllCategorias();

        verify(categoriaRepository).findAll();
        assertEquals(2, resultado.size());
    }

    @Test
    public void updateCategoria() {
        UUID id = UUID.randomUUID();
        Categoria categoriaExistente = new Categoria(id, "Deportes");
        Categoria categoriaActualizada = new Categoria(id, "Fútbol");

        when(categoriaRepository.findById(id)).thenReturn(Optional.of(categoriaExistente));
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoriaActualizada);

        Categoria resultado = categoriaService.updateCategoria(id, categoriaActualizada);

        verify(categoriaRepository).findById(id);
        verify(categoriaRepository).save(categoriaActualizada);
        assertEquals("Fútbol", resultado.getNombre());
    }

    @Test
    public void updateCategoria_NotFound() {
        UUID id = UUID.randomUUID();
        Categoria categoriaActualizada = new Categoria(id, "Música");

        when(categoriaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> categoriaService.updateCategoria(id, categoriaActualizada));
    }

    @Test
    public void deleteCategoria() {
        UUID id = UUID.randomUUID();
        Categoria categoria = new Categoria();

        when(categoriaRepository.findById(id)).thenReturn(Optional.of(categoria));
        doNothing().when(categoriaRepository).deleteById(id);

        categoriaService.deleteCategoria(id);

        verify(categoriaRepository).deleteById(id);
    }

    @Test
    public void deleteCategoria_NotFound() {
        UUID id = UUID.randomUUID();
        when(categoriaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> categoriaService.deleteCategoria(id));
    }
}
