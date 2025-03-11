package ozoriani.empleomadrynbackend.home.model.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private String usuarioId;

    public AuthResponse(String id, String token) {
        this.usuarioId = id;
        this.token = token;
    }
}
