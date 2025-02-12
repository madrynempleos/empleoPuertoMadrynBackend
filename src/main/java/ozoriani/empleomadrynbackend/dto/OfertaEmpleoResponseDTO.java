package ozoriani.empleomadrynbackend.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class OfertaEmpleoResponseDTO {
    private String titulo;
    private String descripcion;
    
    // Datos del usuario publicador
    private UsuarioPublicadorDTO usuarioPublicador;
    
    private String empresaConsultora;
    private LocalDateTime fechaPublicacion;
    private LocalDateTime fechaCierre;
    private String formaPostulacion;
    private String contactoPostulacion; // email o link según corresponda
    private CategoriaDTO categoria;
    
    @Data
    public static class UsuarioPublicadorDTO {
        private String nombre;
        private String email;
    }
    
    @Data
    public static class CategoriaDTO {
        private String nombre;
    }
} 