package ozoriani.empleomadrynbackend.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ozoriani.empleomadrynbackend.model.OfertaEmpleo;

@Repository
public interface OfertaEmpleoRepository extends JpaRepository<OfertaEmpleo, UUID> {
    
}
