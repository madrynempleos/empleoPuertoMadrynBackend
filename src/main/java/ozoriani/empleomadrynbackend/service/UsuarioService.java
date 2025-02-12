package ozoriani.empleomadrynbackend.service;

import ozoriani.empleomadrynbackend.model.Usuario;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioService {
    Usuario createUsuario(Usuario usuario);
    Usuario getUsuarioById(UUID id);
    List<Usuario> getAllUsuarios();
    Usuario updateUsuario(UUID id, Usuario usuario);
    void deleteUsuario(UUID id);
    Optional<Usuario> findByEmail(String email);
}
