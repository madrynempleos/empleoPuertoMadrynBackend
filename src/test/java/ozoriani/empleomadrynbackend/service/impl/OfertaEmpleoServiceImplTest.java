package ozoriani.empleomadrynbackend.service.impl;

import ozoriani.empleomadrynbackend.dto.OfertaEmpleoResponseDTO;
import ozoriani.empleomadrynbackend.errors.exception.ResourceNotFoundException;
import ozoriani.empleomadrynbackend.errors.exception.ValidationException;
import ozoriani.empleomadrynbackend.model.OfertaEmpleo;
import ozoriani.empleomadrynbackend.model.FormaPostulacionEnum;
import ozoriani.empleomadrynbackend.model.Usuario;
import ozoriani.empleomadrynbackend.model.Categoria;
import ozoriani.empleomadrynbackend.repository.OfertaEmpleoRepository;
import ozoriani.empleomadrynbackend.repository.UsuarioRepository;
import ozoriani.empleomadrynbackend.repository.CategoriaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OfertaEmpleoServiceImplTest {

    @InjectMocks
    private OfertaEmpleoServiceImpl ofertaEmpleoService;

    @Mock
    private OfertaEmpleoRepository ofertaEmpleoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    private OfertaEmpleo ofertaEmpleo;

    @BeforeEach
    public void setUp() {

        Usuario usuarioPublicador = new Usuario();
        usuarioPublicador.setId(UUID.randomUUID());
        usuarioPublicador.setNombre("Maximo Ozonas");
        usuarioPublicador.setEmail("maxiozonas@gmail.com");
        usuarioPublicador.setPassword("12345678");

        Categoria categoria = new Categoria();
        categoria.setId(UUID.randomUUID());
        categoria.setNombre("Tecnología");

        MockitoAnnotations.openMocks(this);
        ofertaEmpleo = new OfertaEmpleo();
        ofertaEmpleo.setId(UUID.randomUUID());
        ofertaEmpleo.setUsuario(usuarioPublicador);
        ofertaEmpleo.setCategoria(categoria);
        ofertaEmpleo.setTitulo("Desarrollador Java");
        ofertaEmpleo.setDescripcion("Se busca desarrollador Java con experiencia.");
        ofertaEmpleo.setFechaPublicacion(LocalDateTime.now());
        ofertaEmpleo.setFechaCierre(LocalDateTime.now().plusDays(30));
        ofertaEmpleo.setEmpresaConsultora("Consultora XYZ");
        ofertaEmpleo.setEmailContacto("contacto@consultoraxyz.com");
        ofertaEmpleo.setLinkPostulacion("http://consultoraxyz.com/postular");
        ofertaEmpleo.setFormaPostulacion(FormaPostulacionEnum.MAIL);
    }

    @Test
    public void testCreateOferta() {
        when(usuarioRepository.existsById(any(UUID.class))).thenReturn(true);
        when(categoriaRepository.existsById(any(UUID.class))).thenReturn(true);
        
        when(ofertaEmpleoRepository.save(any(OfertaEmpleo.class))).thenReturn(ofertaEmpleo);
        OfertaEmpleo result = ofertaEmpleoService.createOferta(ofertaEmpleo);
        assertNotNull(result);
        assertEquals(ofertaEmpleo.getTitulo(), result.getTitulo());
        verify(ofertaEmpleoRepository, times(1)).save(ofertaEmpleo);
    }

    @Test
    public void testGetAllOfertas() {
        List<OfertaEmpleo> ofertas = new ArrayList<>();
        ofertas.add(ofertaEmpleo);
        when(ofertaEmpleoRepository.findAll()).thenReturn(ofertas);
        List<OfertaEmpleoResponseDTO> result = ofertaEmpleoService.getAllOfertas();
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(ofertaEmpleoRepository, times(1)).findAll();
    }

    @Test
    public void testGetOfertaById() {
        when(ofertaEmpleoRepository.findById(any(UUID.class))).thenReturn(Optional.of(ofertaEmpleo));
        OfertaEmpleoResponseDTO result = ofertaEmpleoService.getOfertaById(ofertaEmpleo.getId());
        assertNotNull(result);
        assertEquals(ofertaEmpleo.getId(), result.getId());
        verify(ofertaEmpleoRepository, times(1)).findById(ofertaEmpleo.getId());
    }

    @Test
    public void testGetOfertaByIdNotFound() {
        when(ofertaEmpleoRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> {
            ofertaEmpleoService.getOfertaById(UUID.randomUUID());
        });
    }

    @Test
    public void testUpdateOferta() {
        when(usuarioRepository.existsById(any(UUID.class))).thenReturn(true);
        when(categoriaRepository.existsById(any(UUID.class))).thenReturn(true);
        
        when(ofertaEmpleoRepository.existsById(any(UUID.class))).thenReturn(true);
        when(ofertaEmpleoRepository.save(any(OfertaEmpleo.class))).thenReturn(ofertaEmpleo);
        OfertaEmpleo result = ofertaEmpleoService.updateOferta(ofertaEmpleo.getId(), ofertaEmpleo);
        assertNotNull(result);
        verify(ofertaEmpleoRepository, times(1)).save(ofertaEmpleo);
    }

    @Test
    public void testUpdateOfertaNotFound() {
        when(ofertaEmpleoRepository.existsById(any(UUID.class))).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> {
            ofertaEmpleoService.updateOferta(UUID.randomUUID(), ofertaEmpleo);
        });
    }

    @Test
    public void testDeleteOferta() {
        when(ofertaEmpleoRepository.existsById(any(UUID.class))).thenReturn(true);
        boolean result = ofertaEmpleoService.deleteOferta(ofertaEmpleo.getId());
        assertTrue(result);
        verify(ofertaEmpleoRepository, times(1)).deleteById(ofertaEmpleo.getId());
    }

    @Test
    public void testDeleteOfertaNotFound() {
        when(ofertaEmpleoRepository.existsById(any(UUID.class))).thenReturn(false);
        boolean result = ofertaEmpleoService.deleteOferta(UUID.randomUUID());
        assertFalse(result);
        verify(ofertaEmpleoRepository, times(0)).deleteById(any(UUID.class));
    }

    @Test
    public void testValidateOfertaEmpleoWithMissingTitle() {
        ofertaEmpleo.setTitulo(null);
        assertThrows(ValidationException.class, () -> {
            ofertaEmpleoService.createOferta(ofertaEmpleo);
        });
    }

    @Test
    public void testValidateOfertaEmpleoWithMissingDescription() {
        ofertaEmpleo.setDescripcion(null);
        assertThrows(ValidationException.class, () -> {
            ofertaEmpleoService.createOferta(ofertaEmpleo);
        });
    }

    @Test
    public void testValidateOfertaEmpleoWithMissingUsuarioPublicador() {
        ofertaEmpleo.setUsuario(null);
        assertThrows(ValidationException.class, () -> {
            ofertaEmpleoService.createOferta(ofertaEmpleo);
        });
    }

    @Test
    public void testValidateOfertaEmpleoWithInvalidEmail() {
        ofertaEmpleo.setFormaPostulacion(FormaPostulacionEnum.MAIL);
        ofertaEmpleo.setEmailContacto("invalid-email");
        assertThrows(ValidationException.class, () -> {
            ofertaEmpleoService.createOferta(ofertaEmpleo);
        });
    }

    @Test
    public void testValidateOfertaEmpleoWithInvalidLink() {
        ofertaEmpleo.setFormaPostulacion(FormaPostulacionEnum.LINK);
        ofertaEmpleo.setLinkPostulacion("invalid-link");
        assertThrows(ValidationException.class, () -> {
            ofertaEmpleoService.createOferta(ofertaEmpleo);
        });
    }

    @Test
    public void testValidateOfertaEmpleoWithPastClosingDate() {
        ofertaEmpleo.setFechaCierre(LocalDateTime.now().minusDays(1));
        assertThrows(ValidationException.class, () -> {
            ofertaEmpleoService.createOferta(ofertaEmpleo);
        });
    }

    @Test
    public void testValidateOfertaEmpleoWithClosingDateBeforePublication() {
        ofertaEmpleo.setFechaCierre(LocalDateTime.now().plusDays(1));
        ofertaEmpleo.setFechaPublicacion(LocalDateTime.now().plusDays(2));
        assertThrows(ValidationException.class, () -> {
            ofertaEmpleoService.createOferta(ofertaEmpleo);
        });
    }
} 