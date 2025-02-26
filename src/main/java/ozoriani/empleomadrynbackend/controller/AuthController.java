package ozoriani.empleomadrynbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import ozoriani.empleomadrynbackend.dto.AuthResponse;
import ozoriani.empleomadrynbackend.dto.GoogleLoginRequest;
import ozoriani.empleomadrynbackend.model.Usuario;
import ozoriani.empleomadrynbackend.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/google")
    public ResponseEntity<AuthResponse> googleLogin(@Valid @RequestBody GoogleLoginRequest request) {
        try {
            Usuario usuario = authService.authenticateWithGoogle(
                    request.getTokenId(),
                    request.getGoogleId(),
                    request.getEmail(),
                    request.getName()
            );
            String token = authService.generateJwtToken(usuario.getEmail());
            return ResponseEntity.ok(new AuthResponse(token));
        } catch (Exception e) {
            AuthResponse errorResponse = new AuthResponse("Error en autenticación: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}

