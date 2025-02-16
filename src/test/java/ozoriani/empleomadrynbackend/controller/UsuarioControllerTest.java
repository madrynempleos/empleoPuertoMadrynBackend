package ozoriani.empleomadrynbackend.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.*;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import ozoriani.empleomadrynbackend.model.Usuario;
import ozoriani.empleomadrynbackend.service.UsuarioService;

@ExtendWith(MockitoExtension.class)
class UsuarioControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioController usuarioController;

    private ObjectMapper objectMapper;

    private Usuario usuario;
    private UUID usuarioId;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(usuarioController).build();
        usuarioId = UUID.randomUUID();
        usuario = new Usuario(usuarioId, "Juan", "juan@example.com", "password123", new ArrayList<>(), new ArrayList<>());
    }

    @Test
    void testCreateUsuario() throws Exception {
        when(usuarioService.createUsuario(any(Usuario.class))).thenReturn(usuario);

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("juan@example.com"));
    }

    @Test
    void testGetAllUsuarios() throws Exception {
        List<Usuario> usuarios = Collections.singletonList(usuario);
        when(usuarioService.getAllUsuarios()).thenReturn(usuarios);

        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("juan@example.com"));
    }

    @Test
    void testGetUsuarioById() throws Exception {
        when(usuarioService.getUsuarioById(usuarioId)).thenReturn(usuario);

        mockMvc.perform(get("/api/usuarios/{id}", usuarioId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("juan@example.com"));
    }

    @Test
    void testUpdateUsuario() throws Exception {
        Usuario updatedUsuario = new Usuario(usuarioId, "Juan Perez", "juan@example.com", "newpassword123", new ArrayList<>(), new ArrayList<>());
        when(usuarioService.updateUsuario(eq(usuarioId), any(Usuario.class))).thenReturn(updatedUsuario);

        mockMvc.perform(put("/api/usuarios/{id}", usuarioId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUsuario)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan Perez"));
    }

    @Test
    void testDeleteUsuario() throws Exception {
        doNothing().when(usuarioService).deleteUsuario(usuarioId);

        mockMvc.perform(delete("/api/usuarios/{id}", usuarioId))
                .andExpect(status().isNoContent());
    }
}
