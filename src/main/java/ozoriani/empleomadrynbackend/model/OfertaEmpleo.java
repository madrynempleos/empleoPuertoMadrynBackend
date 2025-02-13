package ozoriani.empleomadrynbackend.model;

import java.time.LocalDateTime;
import java.util.UUID;

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
    @JoinColumn(name = "usuario_publicador_id", nullable = false)
    private Usuario usuarioPublicador;

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
}

