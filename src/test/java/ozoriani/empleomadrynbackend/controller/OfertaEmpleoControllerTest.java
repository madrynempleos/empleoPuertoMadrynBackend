package ozoriani.empleomadrynbackend.controller;

import ozoriani.empleomadrynbackend.dto.OfertaEmpleoResponseDTO;
import ozoriani.empleomadrynbackend.errors.exception.ResourceNotFoundException;
import ozoriani.empleomadrynbackend.model.Categoria;
import ozoriani.empleomadrynbackend.model.FormaPostulacionEnum;
import ozoriani.empleomadrynbackend.model.OfertaEmpleo;
import ozoriani.empleomadrynbackend.model.Usuario;
import ozoriani.empleomadrynbackend.service.OfertaEmpleoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OfertaEmpleoControllerTest {

    @InjectMocks
    private OfertaEmpleoController ofertaEmpleoController;

    @Mock
    private OfertaEmpleoService ofertaEmpleoService;

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
        ofertaEmpleo.setUsuarioPublicador(usuarioPublicador);
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
        when(ofertaEmpleoService.createOferta(any(OfertaEmpleo.class))).thenReturn(ofertaEmpleo);
        ResponseEntity<OfertaEmpleo> response = ofertaEmpleoController.createOferta(ofertaEmpleo);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(ofertaEmpleo, response.getBody());
        verify(ofertaEmpleoService, times(1)).createOferta(ofertaEmpleo);
    }

    @Test
    public void testGetAllOfertas() {
        List<OfertaEmpleoResponseDTO> ofertas = Collections.singletonList(new OfertaEmpleoResponseDTO());
        when(ofertaEmpleoService.getAllOfertas()).thenReturn(ofertas);
        ResponseEntity<List<OfertaEmpleoResponseDTO>> response = ofertaEmpleoController.getAllOfertas();
        
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(ofertaEmpleoService, times(1)).getAllOfertas();
    }

    @Test
    public void testGetOfertaById() {
        when(ofertaEmpleoService.getOfertaById(any(UUID.class))).thenReturn(new OfertaEmpleoResponseDTO());
        ResponseEntity<OfertaEmpleoResponseDTO> response = ofertaEmpleoController.getOfertaById(ofertaEmpleo.getId());
        assertEquals(200, response.getStatusCode().value());
        verify(ofertaEmpleoService, times(1)).getOfertaById(ofertaEmpleo.getId());
    }

    @Test
    public void testGetOfertaByIdNotFound() {
        when(ofertaEmpleoService.getOfertaById(any(UUID.class))).thenThrow(new ResourceNotFoundException("Oferta no encontrada"));
        ResponseEntity<OfertaEmpleoResponseDTO> response = ofertaEmpleoController.getOfertaById(UUID.randomUUID());
        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    public void testUpdateOferta() {
        when(ofertaEmpleoService.updateOferta(any(UUID.class), any(OfertaEmpleo.class))).thenReturn(ofertaEmpleo);
        ResponseEntity<OfertaEmpleo> response = ofertaEmpleoController.updateOferta(ofertaEmpleo.getId(), ofertaEmpleo);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(ofertaEmpleo, response.getBody());
        verify(ofertaEmpleoService, times(1)).updateOferta(ofertaEmpleo.getId(), ofertaEmpleo);
    }

    @Test
    public void testUpdateOfertaNotFound() {
        when(ofertaEmpleoService.updateOferta(any(UUID.class), any(OfertaEmpleo.class))).thenThrow(new ResourceNotFoundException("Oferta no encontrada"));
        ResponseEntity<OfertaEmpleo> response = ofertaEmpleoController.updateOferta(UUID.randomUUID(), ofertaEmpleo);
        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    public void testDeleteOferta() {
        when(ofertaEmpleoService.deleteOferta(any(UUID.class))).thenReturn(true);
        ResponseEntity<Void> response = ofertaEmpleoController.deleteOferta(ofertaEmpleo.getId());
        assertEquals(204, response.getStatusCode().value());
        verify(ofertaEmpleoService, times(1)).deleteOferta(ofertaEmpleo.getId());
    }

    @Test
    public void testDeleteOfertaNotFound() {
        when(ofertaEmpleoService.deleteOferta(any(UUID.class))).thenReturn(false);
        ResponseEntity<Void> response = ofertaEmpleoController.deleteOferta(UUID.randomUUID());
        assertEquals(404, response.getStatusCode().value());
    }
} 