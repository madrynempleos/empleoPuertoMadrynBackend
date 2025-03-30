package ozoriani.empleomadrynbackend.home.model.entities;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonSetter;

import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "oferta_empleo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfertaEmpleo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull(message = "El título es requerido")
    @Size(max = 150, message = "El título no puede tener más de 150 caracteres")
    @Column(nullable = false, length = 150)
    private String titulo;

    @NotNull(message = "La descripción es requerida")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @NotNull(message = "El usuario publicador es requerido")
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonBackReference
    private Usuario usuario;

    @NotNull(message = "La empresa consultora es requerida")
    @Size(max = 150, message = "La empresa consultora no puede tener más de 150 caracteres")
    @Column(nullable = false, length = 150)
    private String empresaConsultora;

    @Column(nullable = false)
    private LocalDateTime fechaPublicacion = LocalDateTime.now();

    private LocalDateTime fechaCierre;

    @NotNull(message = "La forma de postulación es requerida")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private FormaPostulacionEnum formaPostulacion;

    @Email(message = "El formato del email de contacto no es válido")
    private String emailContacto;

    @Pattern(regexp = "^(http|https)://.*$", message = "El formato del link de postulación no es válido")
    private String linkPostulacion;

    @NotNull(message = "La categoría es requerida")
    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @Column(nullable = true)
    private String logoUrl;

    // Setter personalizado para usuario
    @JsonSetter("usuario")
    public void setUsuario(Map<String, String> usuarioMap) {
        if (usuarioMap != null && usuarioMap.containsKey("id")) {
            this.usuario = new Usuario();
            this.usuario.setId(UUID.fromString(usuarioMap.get("id")));
        }
    }

    // Setter personalizado para categoria
    @JsonSetter("categoria")
    public void setCategoria(Map<String, String> categoriaMap) {
        if (categoriaMap != null && categoriaMap.containsKey("id")) {
            this.categoria = new Categoria();
            this.categoria.setId(UUID.fromString(categoriaMap.get("id")));
        }
    }
}
