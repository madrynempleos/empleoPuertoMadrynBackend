package ozoriani.empleomadrynbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ozoriani.empleomadrynbackend.model.Usuario;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
    Optional<Usuario> findByGoogleId(String googleId);
}
