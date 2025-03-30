package ozoriani.empleomadrynbackend.home.service;

import ozoriani.empleomadrynbackend.home.model.dto.OfertaEmpleoResponseDTO;
import ozoriani.empleomadrynbackend.home.model.entities.OfertaEmpleo;

import java.util.List;
import java.util.UUID;

public interface OfertaEmpleoService {
    OfertaEmpleo createOferta(OfertaEmpleo ofertaEmpleo);

    List<OfertaEmpleoResponseDTO> getAllOfertas();

    OfertaEmpleoResponseDTO getOfertaById(UUID id);

    OfertaEmpleo updateOferta(UUID id, OfertaEmpleo ofertaEmpleo);

    void deleteOferta(UUID id);

    List<OfertaEmpleoResponseDTO> getUserJobPosts(String userEmail);

    List<OfertaEmpleoResponseDTO> getOfertasNoHabilitadas();

    OfertaEmpleo habilitarOferta(UUID id);

    OfertaEmpleoResponseDTO convertToDTO(OfertaEmpleo ofertaEmpleo) ;
}
