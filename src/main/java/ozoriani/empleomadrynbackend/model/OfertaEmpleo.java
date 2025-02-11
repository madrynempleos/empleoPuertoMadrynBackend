package ozoriani.empleomadrynbackend.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "oferta_empleo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfertaEmpleo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "usuario_publicador_id", nullable = false)
    private Usuario usuarioPublicador;

    @Column(nullable = false, length = 150)
    private String empresaConsultora;

    @Column(nullable = false)
    private LocalDateTime fechaPublicacion = LocalDateTime.now();

    private LocalDateTime fechaCierre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private FormaPostulacionEum formaPostulacion;

    private String emailContacto; // Si aplica por email
    private String linkPostulacion; // Si aplica por link externo

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false) // Relación 1:N
    private Categoria categoria;
}

