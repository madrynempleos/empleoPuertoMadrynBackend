package ozoriani.empleomadrynbackend.home.service;

import ozoriani.empleomadrynbackend.home.model.entities.Usuario;

public interface AuthService {
    Usuario authenticateWithGoogle(String accessToken, String googleId, String email, String name) throws Exception;

    String generateJwtToken(String email);
}
