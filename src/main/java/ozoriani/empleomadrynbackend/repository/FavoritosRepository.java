package ozoriani.empleomadrynbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ozoriani.empleomadrynbackend.model.Favoritos;

@Repository
public interface FavoritosRepository extends JpaRepository<Favoritos, Long> {}
