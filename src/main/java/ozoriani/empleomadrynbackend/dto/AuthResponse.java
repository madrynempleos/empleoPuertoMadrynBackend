package ozoriani.empleomadrynbackend.dto;

public class AuthResponse {
    private String token;
    private String usuarioId;

    public AuthResponse(String id, String token) {
        this.usuarioId = id;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public String getId() {
        return usuarioId;
    }
}
