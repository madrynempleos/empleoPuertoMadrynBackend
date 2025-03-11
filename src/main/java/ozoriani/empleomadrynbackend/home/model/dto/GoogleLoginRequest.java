package ozoriani.empleomadrynbackend.home.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class GoogleLoginRequest {
    @NotBlank(message = "El token de Google no puede estar vacío")
    private String tokenId;

    @NotBlank(message = "El Google ID no puede estar vacío")
    private String googleId;

    @NotBlank(message = "El email no puede estar vacío")
    private String email;

    @NotBlank(message = "El nombre no puede estar vacío")
    private String name;

}
