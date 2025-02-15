package ozoriani.empleomadrynbackend.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfertaEmpleoResponseDTO {
    private UUID id;
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