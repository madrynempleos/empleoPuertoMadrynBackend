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

import ozoriani.empleomadrynbackend.home.controller.CategoriaController;
import ozoriani.empleomadrynbackend.home.model.entities.Categoria;
import ozoriani.empleomadrynbackend.home.service.CategoriaService;

@ExtendWith(MockitoExtension.class)
class CategoriaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CategoriaService categoriaService;

    @InjectMocks
    private CategoriaController categoriaController;

    private ObjectMapper objectMapper;

    private Categoria categoria;
    private UUID categoriaId;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(categoriaController).build();
        categoriaId = UUID.randomUUID();
        categoria = new Categoria(categoriaId, "Categoria Test");
    }

    @Test
    void testCreateCategoria() throws Exception {
        when(categoriaService.createCategoria(any(Categoria.class))).thenReturn(categoria);

        mockMvc.perform(post("/api/categorias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoria)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Categoria Test"));
    }

    @Test
    void testGetAllCategorias() throws Exception {
        List<Categoria> categorias = Collections.singletonList(categoria);
        when(categoriaService.getAllCategorias()).thenReturn(categorias);

        mockMvc.perform(get("/api/categorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Categoria Test"));
    }

    @Test
    void testGetCategoriaById() throws Exception {
        when(categoriaService.getCategoriaById(categoriaId)).thenReturn(categoria);

        mockMvc.perform(get("/api/categorias/{id}", categoriaId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Categoria Test"));
    }

    @Test
    void testUpdateCategoria() throws Exception {
        Categoria updatedCategoria = new Categoria(categoriaId, "Categoria Actualizada");
        when(categoriaService.updateCategoria(eq(categoriaId), any(Categoria.class))).thenReturn(updatedCategoria);

        mockMvc.perform(put("/api/categorias/{id}", categoriaId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedCategoria)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Categoria Actualizada"));
    }

    @Test
    void testDeleteCategoria() throws Exception {
        doNothing().when(categoriaService).deleteCategoria(categoriaId);

        mockMvc.perform(delete("/api/categorias/{id}", categoriaId))
                .andExpect(status().isNoContent());
    }
}
