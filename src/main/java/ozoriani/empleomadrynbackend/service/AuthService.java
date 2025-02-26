package ozoriani.empleomadrynbackend.service;

import ozoriani.empleomadrynbackend.model.Usuario;

public interface AuthService {
    Usuario authenticateWithGoogle(String accessToken, String googleId, String email, String name) throws Exception;
    String generateJwtToken(String email);
}
