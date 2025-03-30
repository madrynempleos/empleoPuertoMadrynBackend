package ozoriani.empleomadrynbackend.home.model.dto;

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
    
    private UsuarioPublicadorDTO usuarioPublicador;
    
    private String empresaConsultora;
    private LocalDateTime fechaPublicacion;
    private LocalDateTime fechaCierre;
    private String formaPostulacion;
    private String contactoPostulacion; 
    private CategoriaDTO categoria;
    private String logoUrl;
    
    @Data
    public static class UsuarioPublicadorDTO {
        private String email;
    }
    
    @Data
    public static class CategoriaDTO {
        private String id;
        private String nombre;
    }
} 