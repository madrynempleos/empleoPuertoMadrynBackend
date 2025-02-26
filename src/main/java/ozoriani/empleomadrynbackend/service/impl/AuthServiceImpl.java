package ozoriani.empleomadrynbackend.service.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ozoriani.empleomadrynbackend.config.JwtUtil;
import ozoriani.empleomadrynbackend.model.Usuario;
import ozoriani.empleomadrynbackend.repository.UsuarioRepository;
import ozoriani.empleomadrynbackend.service.AuthService;
import ozoriani.empleomadrynbackend.errors.exception.ValidationException;

import java.util.Collections;

@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    @Override
    public Usuario authenticateWithGoogle(String tokenId, String googleId, String email, String name) throws ValidationException {
        if (tokenId == null || tokenId.trim().isEmpty()) {
            logger.warn("Google authentication failed: tokenId is null or empty");
            throw new ValidationException("El token de Google no puede ser nulo o vacío");
        }
        if (googleId == null || googleId.trim().isEmpty()) {
            logger.warn("Google authentication failed: googleId is null or empty");
            throw new ValidationException("El Google ID no puede ser nulo o vacío");
        }

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(googleClientId))
                .build();

        GoogleIdToken idToken;
        try {
            idToken = verifier.verify(tokenId);
        } catch (Exception e) {
            logger.error("Error verifying Google token: {}", e.getMessage());
            throw new ValidationException("Error al verificar el token de Google: " + e.getMessage());
        }

        if (idToken == null) {
            logger.warn("Invalid Google token provided");
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
            logger.error("Failed to save user to database: {}", e.getMessage());
            throw new ValidationException("Error al guardar el usuario en la base de datos: " + e.getMessage());
        }
    }

    @Override
    public String generateJwtToken(String email) {
        return jwtUtil.generateToken(email);
    }
}