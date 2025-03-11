package ozoriani.empleomadrynbackend.controller;

import ozoriani.empleomadrynbackend.errors.exception.ResourceNotFoundException;
import ozoriani.empleomadrynbackend.home.controller.OfertaEmpleoController;
import ozoriani.empleomadrynbackend.home.model.dto.OfertaEmpleoResponseDTO;
import ozoriani.empleomadrynbackend.home.model.entities.Categoria;
import ozoriani.empleomadrynbackend.home.model.entities.FormaPostulacionEnum;
import ozoriani.empleomadrynbackend.home.model.entities.OfertaEmpleo;
import ozoriani.empleomadrynbackend.home.model.entities.Usuario;
import ozoriani.empleomadrynbackend.home.service.OfertaEmpleoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
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
        MockitoAnnotations.openMocks(this);

        Usuario usuarioPublicador = new Usuario();
        usuarioPublicador.setId(UUID.randomUUID());
        usuarioPublicador.setName("Maximo Ozonas"); // Cambié "Nombre" a "Name" para coincidir con tu modelo
        usuarioPublicador.setEmail("maxiozonas@gmail.com");
        // "Password" no está en tu modelo actual, así que lo eliminé

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
        when(ofertaEmpleoService.createOferta(any(OfertaEmpleo.class))).thenReturn(ofertaEmpleo);
        ResponseEntity<OfertaEmpleo> response = ofertaEmpleoController.createOferta(ofertaEmpleo);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ofertaEmpleo, response.getBody());
        verify(ofertaEmpleoService, times(1)).createOferta(ofertaEmpleo);
    }

    @Test
    public void testGetAllOfertas_Success() {
        List<OfertaEmpleoResponseDTO> ofertas = Collections.singletonList(new OfertaEmpleoResponseDTO());
        when(ofertaEmpleoService.getAllOfertas()).thenReturn(ofertas);
        ResponseEntity<List<OfertaEmpleoResponseDTO>> response = ofertaEmpleoController.getAllOfertas();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(ofertaEmpleoService, times(1)).getAllOfertas();
    }

    @Test
    public void testGetOfertaById_Success() {
        OfertaEmpleoResponseDTO dto = new OfertaEmpleoResponseDTO();
        when(ofertaEmpleoService.getOfertaById(ofertaEmpleo.getId())).thenReturn(dto);
        ResponseEntity<OfertaEmpleoResponseDTO> response = ofertaEmpleoController.getOfertaById(ofertaEmpleo.getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
        verify(ofertaEmpleoService, times(1)).getOfertaById(ofertaEmpleo.getId());
    }

    @Test
    public void testGetOfertaById_NotFound() {
        when(ofertaEmpleoService.getOfertaById(any(UUID.class)))
                .thenThrow(new ResourceNotFoundException("Oferta no encontrada"));
        assertThrows(ResourceNotFoundException.class, () -> ofertaEmpleoController.getOfertaById(UUID.randomUUID()));
        verify(ofertaEmpleoService, times(1)).getOfertaById(any(UUID.class));
    }

    @Test
    public void testUpdateOferta_Success() {
        when(ofertaEmpleoService.updateOferta(any(UUID.class), any(OfertaEmpleo.class))).thenReturn(ofertaEmpleo);
        ResponseEntity<OfertaEmpleo> response = ofertaEmpleoController.updateOferta(ofertaEmpleo.getId(), ofertaEmpleo);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ofertaEmpleo, response.getBody());
        verify(ofertaEmpleoService, times(1)).updateOferta(ofertaEmpleo.getId(), ofertaEmpleo);
    }

    @Test
    public void testUpdateOferta_NotFound() {
        when(ofertaEmpleoService.updateOferta(any(UUID.class), any(OfertaEmpleo.class)))
                .thenThrow(new ResourceNotFoundException("Oferta no encontrada"));
        assertThrows(ResourceNotFoundException.class,
                () -> ofertaEmpleoController.updateOferta(UUID.randomUUID(), ofertaEmpleo));
        verify(ofertaEmpleoService, times(1)).updateOferta(any(UUID.class), eq(ofertaEmpleo));
    }

    @Test
    public void testDeleteOferta_Success() {
        doNothing().when(ofertaEmpleoService).deleteOferta(ofertaEmpleo.getId());
        ResponseEntity<Void> response = ofertaEmpleoController.deleteOferta(ofertaEmpleo.getId());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(ofertaEmpleoService, times(1)).deleteOferta(ofertaEmpleo.getId());
    }

    @Test
    public void testDeleteOferta_NotFound() {
        doThrow(new ResourceNotFoundException("Oferta no encontrada")).when(ofertaEmpleoService)
                .deleteOferta(any(UUID.class));
        assertThrows(ResourceNotFoundException.class, () -> ofertaEmpleoController.deleteOferta(UUID.randomUUID()));
        verify(ofertaEmpleoService, times(1)).deleteOferta(any(UUID.class));
    }
}