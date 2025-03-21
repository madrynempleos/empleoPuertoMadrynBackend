package ozoriani.empleomadrynbackend.home.model.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private String usuarioId;

    public AuthResponse(String usuarioId, String token) {
        this.usuarioId = usuarioId;
        this.token = token;
    }
}
