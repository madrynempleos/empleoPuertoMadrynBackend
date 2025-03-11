package ozoriani.empleomadrynbackend.home.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ozoriani.empleomadrynbackend.home.model.entities.Categoria;

import java.util.UUID;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, UUID> {
}
