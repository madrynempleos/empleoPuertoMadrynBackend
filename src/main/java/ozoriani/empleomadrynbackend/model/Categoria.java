package ozoriani.empleomadrynbackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import jakarta.validation.constraints.*;

import java.util.UUID;

@Entity
@Table(name = "categoria")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull(message = "El nombre de la categoría es requerido")
    @Size(max = 100, message = "El nombre de la categoría no puede tener más de 100 caracteres")
    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

}
