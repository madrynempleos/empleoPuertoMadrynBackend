package ozoriani.empleomadrynbackend.service.impl;

import ozoriani.empleomadrynbackend.model.OfertaEmpleo;
import ozoriani.empleomadrynbackend.service.OfertaEmpleoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ozoriani.empleomadrynbackend.repository.OfertaEmpleoRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OfertaEmpleoServiceImpl implements OfertaEmpleoService {

    @Autowired
    private OfertaEmpleoRepository ofertaEmpleoRepository;

    @Override
    public OfertaEmpleo createOferta(OfertaEmpleo ofertaEmpleo) {
        return ofertaEmpleoRepository.save(ofertaEmpleo);
    }

    @Override
    public List<OfertaEmpleo> getAllOfertas() {
        return ofertaEmpleoRepository.findAll();
    }

    @Override
    public OfertaEmpleo getOfertaById(UUID id) {
        Optional<OfertaEmpleo> oferta = ofertaEmpleoRepository.findById(id);
        return oferta.orElse(null);
    }

    @Override
    public OfertaEmpleo updateOferta(UUID id, OfertaEmpleo ofertaEmpleo) {
        if (ofertaEmpleoRepository.existsById(id)) {
            ofertaEmpleo.setId(id);
            return ofertaEmpleoRepository.save(ofertaEmpleo);
        }
        return null;
    }

    @Override
    public boolean deleteOferta(UUID id) {
        if (ofertaEmpleoRepository.existsById(id)) {
            ofertaEmpleoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
