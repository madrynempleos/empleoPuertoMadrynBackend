package ozoriani.empleomadrynbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ozoriani.empleomadrynbackend.model.Categoria;

import java.util.UUID;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, UUID> {
}
