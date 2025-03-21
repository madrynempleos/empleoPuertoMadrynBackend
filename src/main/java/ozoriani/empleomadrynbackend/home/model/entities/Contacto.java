package ozoriani.empleomadrynbackend.home.model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contacto {
    private String nombre;
    private String apellido;
    private String email;
    private String mensaje;
}