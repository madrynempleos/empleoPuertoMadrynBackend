package ozoriani.empleomadrynbackend.service;

import ozoriani.empleomadrynbackend.model.OfertaEmpleo;
import java.util.List;
import java.util.UUID;

public interface OfertaEmpleoService {
    OfertaEmpleo createOferta(OfertaEmpleo ofertaEmpleo);
    List<OfertaEmpleo> getAllOfertas();
    OfertaEmpleo getOfertaById(UUID id);
    OfertaEmpleo updateOferta(UUID id, OfertaEmpleo ofertaEmpleo);
    boolean deleteOferta(UUID id);
}
