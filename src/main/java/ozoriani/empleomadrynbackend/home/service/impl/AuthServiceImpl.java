package ozoriani.empleomadrynbackend.home.service.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ozoriani.empleomadrynbackend.config.JwtUtil;
import ozoriani.empleomadrynbackend.errors.exception.ValidationException;
import ozoriani.empleomadrynbackend.home.model.entities.Usuario;
import ozoriani.empleomadrynbackend.home.model.repository.UsuarioRepository;
import ozoriani.empleomadrynbackend.home.service.AuthService;
import ozoriani.empleomadrynbackend.errors.exception.SecurityException;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;

    private final JwtUtil jwtUtil;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    public AuthServiceImpl(UsuarioRepository usuarioRepository, JwtUtil jwtUtil) {
        this.usuarioRepository = usuarioRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Usuario authenticateWithGoogle(String tokenId, String googleId, String email, String name) {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(),
                GsonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(googleClientId))
                .build();

        GoogleIdToken idToken;
        try {
            idToken = verifier.verify(tokenId);
        } catch (GeneralSecurityException | IOException e) {
            throw new SecurityException("Error al verificar el token de Google: " + e.getMessage());
        }
            

        if (idToken == null) {
            throw new ValidationException("Token de Google inválido");
        }

        GoogleIdToken.Payload payload = idToken.getPayload();
        String verifiedEmail = payload.getEmail();
        String verifiedName = (String) payload.get("name");

        Usuario usuario = usuarioRepository.findByGoogleId(googleId)
                .orElse(new Usuario());
        usuario.setEmail(verifiedEmail);
        usuario.setName(verifiedName);
        usuario.setGoogleId(googleId);

        try {
            return usuarioRepository.save(usuario);
        } catch (Exception e) {
            throw new ValidationException("Error al guardar el usuario en la base de datos: " + e.getMessage());
        }
    }

    @Override
    public String generateJwtToken(String email) {
        return jwtUtil.generateToken(email);
    }
}