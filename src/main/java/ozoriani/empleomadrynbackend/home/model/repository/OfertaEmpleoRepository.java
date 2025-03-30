package ozoriani.empleomadrynbackend.home.model.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ozoriani.empleomadrynbackend.home.model.entities.OfertaEmpleo;

import java.util.List;

@Repository
public interface OfertaEmpleoRepository extends JpaRepository<OfertaEmpleo, UUID> {
    List<OfertaEmpleo> findByUsuarioEmail(String email);
    List<OfertaEmpleo> findByHabilitadoFalse();
}
