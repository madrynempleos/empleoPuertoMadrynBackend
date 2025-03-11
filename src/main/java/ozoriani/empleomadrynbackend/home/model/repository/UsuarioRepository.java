package ozoriani.empleomadrynbackend.home.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ozoriani.empleomadrynbackend.home.model.entities.Usuario;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
    Optional<Usuario> findByGoogleId(String googleId);

    Optional<Usuario> findByEmail(String email);
}
