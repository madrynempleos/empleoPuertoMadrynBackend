package ozoriani.empleomadrynbackend.home.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ozoriani.empleomadrynbackend.home.model.entities.Favoritos;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FavoritosRepository extends JpaRepository<Favoritos, UUID> {
    Optional<Favoritos> findByUsuarioIdAndOfertaEmpleoId(UUID usuarioId, UUID ofertaId);

    List<Favoritos> findByUsuarioId(UUID usuarioId);

    void deleteByUsuarioIdAndOfertaEmpleoId(UUID usuarioId, UUID ofertaId);
}
