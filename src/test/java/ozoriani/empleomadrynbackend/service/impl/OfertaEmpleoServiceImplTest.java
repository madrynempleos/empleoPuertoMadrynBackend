package ozoriani.empleomadrynbackend.service.impl;

import ozoriani.empleomadrynbackend.dto.OfertaEmpleoResponseDTO;
import ozoriani.empleomadrynbackend.errors.exception.ResourceNotFoundException;
import ozoriani.empleomadrynbackend.errors.exception.ValidationException;
import ozoriani.empleomadrynbackend.model.Categoria;
import ozoriani.empleomadrynbackend.model.FormaPostulacionEnum;
import ozoriani.empleomadrynbackend.model.OfertaEmpleo;
import ozoriani.empleomadrynbackend.model.Usuario;
import ozoriani.empleomadrynbackend.repository.CategoriaRepository;
import ozoriani.empleomadrynbackend.repository.OfertaEmpleoRepository;
import ozoriani.empleomadrynbackend.repository.UsuarioRepository;
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
        MockitoAnnotations.openMocks(this);

        Usuario usuarioPublicador = new Usuario();
        usuarioPublicador.setId(UUID.randomUUID());
        usuarioPublicador.setName("Maximo Ozonas"); // Cambié "Nombre" a "Name"
        usuarioPublicador.setEmail("maxiozonas@gmail.com");

        Categoria categoria = new Categoria();
        categoria.setId(UUID.randomUUID());
        categoria.setNombre("Tecnología");

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
    public void testCreateOferta_Success() {
        when(usuarioRepository.existsById(any(UUID.class))).thenReturn(true);
        when(categoriaRepository.existsById(any(UUID.class))).thenReturn(true);
        when(ofertaEmpleoRepository.save(any(OfertaEmpleo.class))).thenReturn(ofertaEmpleo);

        OfertaEmpleo result = ofertaEmpleoService.createOferta(ofertaEmpleo);
        assertNotNull(result);
        assertEquals(ofertaEmpleo.getTitulo(), result.getTitulo());
        verify(ofertaEmpleoRepository, times(1)).save(ofertaEmpleo);
    }

    @Test
    public void testGetAllOfertas_Success() {
        List<OfertaEmpleo> ofertas = new ArrayList<>();
        ofertas.add(ofertaEmpleo);
        when(ofertaEmpleoRepository.findAll()).thenReturn(ofertas);

        List<OfertaEmpleoResponseDTO> result = ofertaEmpleoService.getAllOfertas();
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(ofertaEmpleoRepository, times(1)).findAll();
    }

    @Test
    public void testGetOfertaById_Success() {
        when(ofertaEmpleoRepository.findById(any(UUID.class))).thenReturn(Optional.of(ofertaEmpleo));

        OfertaEmpleoResponseDTO result = ofertaEmpleoService.getOfertaById(ofertaEmpleo.getId());
        assertNotNull(result);
        assertEquals(ofertaEmpleo.getId(), result.getId());
        verify(ofertaEmpleoRepository, times(1)).findById(ofertaEmpleo.getId());
    }

    @Test
    public void testGetOfertaById_NotFound() {
        when(ofertaEmpleoRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> ofertaEmpleoService.getOfertaById(UUID.randomUUID()));
        verify(ofertaEmpleoRepository, times(1)).findById(any(UUID.class));
    }

    @Test
    public void testUpdateOferta_Success() {
        when(usuarioRepository.existsById(any(UUID.class))).thenReturn(true);
        when(categoriaRepository.existsById(any(UUID.class))).thenReturn(true);
        when(ofertaEmpleoRepository.existsById(any(UUID.class))).thenReturn(true);
        when(ofertaEmpleoRepository.save(any(OfertaEmpleo.class))).thenReturn(ofertaEmpleo);

        OfertaEmpleo result = ofertaEmpleoService.updateOferta(ofertaEmpleo.getId(), ofertaEmpleo);
        assertNotNull(result);
        assertEquals(ofertaEmpleo.getId(), result.getId());
        verify(ofertaEmpleoRepository, times(1)).save(ofertaEmpleo);
    }

    @Test
    public void testUpdateOferta_NotFound() {
        when(ofertaEmpleoRepository.existsById(any(UUID.class))).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> ofertaEmpleoService.updateOferta(UUID.randomUUID(), ofertaEmpleo));
        verify(ofertaEmpleoRepository, times(1)).existsById(any(UUID.class));
    }

    @Test
    public void testDeleteOferta_Success() {
        when(ofertaEmpleoRepository.existsById(any(UUID.class))).thenReturn(true);
        doNothing().when(ofertaEmpleoRepository).deleteById(any(UUID.class));

        ofertaEmpleoService.deleteOferta(ofertaEmpleo.getId());
        verify(ofertaEmpleoRepository, times(1)).deleteById(ofertaEmpleo.getId());
    }

    @Test
    public void testDeleteOferta_NotFound() {
        when(ofertaEmpleoRepository.existsById(any(UUID.class))).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> ofertaEmpleoService.deleteOferta(UUID.randomUUID()));
        verify(ofertaEmpleoRepository, times(1)).existsById(any(UUID.class));
        verify(ofertaEmpleoRepository, never()).deleteById(any(UUID.class));
    }

    @Test
    public void testValidateOfertaEmpleo_MissingUsuario() {
        ofertaEmpleo.setUsuario(null);
        assertThrows(ValidationException.class, () -> ofertaEmpleoService.createOferta(ofertaEmpleo));
    }

    @Test
    public void testValidateOfertaEmpleo_InvalidUsuario() {
        when(usuarioRepository.existsById(ofertaEmpleo.getUsuario().getId())).thenReturn(false);
        assertThrows(ValidationException.class, () -> ofertaEmpleoService.createOferta(ofertaEmpleo));
    }

    @Test
    public void testValidateOfertaEmpleo_MissingCategoria() {
        ofertaEmpleo.setCategoria(null);
        assertThrows(ValidationException.class, () -> ofertaEmpleoService.createOferta(ofertaEmpleo));
    }

    @Test
    public void testValidateOfertaEmpleo_InvalidCategoria() {
        when(categoriaRepository.existsById(ofertaEmpleo.getCategoria().getId())).thenReturn(false);
        assertThrows(ValidationException.class, () -> ofertaEmpleoService.createOferta(ofertaEmpleo));
    }

    @Test
    public void testValidateOfertaEmpleo_InvalidEmail() {
        ofertaEmpleo.setFormaPostulacion(FormaPostulacionEnum.MAIL);
        ofertaEmpleo.setEmailContacto("invalid-email");
        when(usuarioRepository.existsById(any(UUID.class))).thenReturn(true);
        when(categoriaRepository.existsById(any(UUID.class))).thenReturn(true);
        assertThrows(ValidationException.class, () -> ofertaEmpleoService.createOferta(ofertaEmpleo));
    }

    @Test
    public void testValidateOfertaEmpleo_InvalidLink() {
        ofertaEmpleo.setFormaPostulacion(FormaPostulacionEnum.LINK);
        ofertaEmpleo.setLinkPostulacion("invalid-link");
        when(usuarioRepository.existsById(any(UUID.class))).thenReturn(true);
        when(categoriaRepository.existsById(any(UUID.class))).thenReturn(true);
        assertThrows(ValidationException.class, () -> ofertaEmpleoService.createOferta(ofertaEmpleo));
    }

    @Test
    public void testValidateOfertaEmpleo_PastClosingDate() {
        ofertaEmpleo.setFechaCierre(LocalDateTime.now().minusDays(1));
        when(usuarioRepository.existsById(any(UUID.class))).thenReturn(true);
        when(categoriaRepository.existsById(any(UUID.class))).thenReturn(true);
        assertThrows(ValidationException.class, () -> ofertaEmpleoService.createOferta(ofertaEmpleo));
    }

    @Test
    public void testValidateOfertaEmpleo_ClosingDateBeforePublication() {
        ofertaEmpleo.setFechaCierre(LocalDateTime.now().plusDays(1));
        ofertaEmpleo.setFechaPublicacion(LocalDateTime.now().plusDays(2));
        when(usuarioRepository.existsById(any(UUID.class))).thenReturn(true);
        when(categoriaRepository.existsById(any(UUID.class))).thenReturn(true);
        assertThrows(ValidationException.class, () -> ofertaEmpleoService.createOferta(ofertaEmpleo));
    }
}