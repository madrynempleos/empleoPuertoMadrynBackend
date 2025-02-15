package ozoriani.empleomadrynbackend.service.impl;

import ozoriani.empleomadrynbackend.errors.exception.ResourceNotFoundException;
import ozoriani.empleomadrynbackend.errors.exception.ValidationException;
import ozoriani.empleomadrynbackend.model.OfertaEmpleo;
import ozoriani.empleomadrynbackend.service.OfertaEmpleoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ozoriani.empleomadrynbackend.repository.CategoriaRepository;
import ozoriani.empleomadrynbackend.repository.OfertaEmpleoRepository;
import ozoriani.empleomadrynbackend.repository.UsuarioRepository;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import ozoriani.empleomadrynbackend.dto.OfertaEmpleoResponseDTO;
import ozoriani.empleomadrynbackend.model.FormaPostulacionEnum;

@Service
public class OfertaEmpleoServiceImpl implements OfertaEmpleoService {

    @Autowired
    private OfertaEmpleoRepository ofertaEmpleoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    public OfertaEmpleo createOferta(OfertaEmpleo ofertaEmpleo) {
        validateOfertaEmpleo(ofertaEmpleo);
        return ofertaEmpleoRepository.save(ofertaEmpleo);
    }

    @Override
    public List<OfertaEmpleoResponseDTO> getAllOfertas() {
        return ofertaEmpleoRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public OfertaEmpleoResponseDTO getOfertaById(UUID id) {
        OfertaEmpleo oferta = ofertaEmpleoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Oferta de empleo no encontrada con ID: " + id));
        return convertToDTO(oferta);
    }

    @Override
    public OfertaEmpleo updateOferta(UUID id, OfertaEmpleo ofertaEmpleo) {
        if (!ofertaEmpleoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Oferta de empleo no encontrada con ID: " + id);
        }
        validateOfertaEmpleo(ofertaEmpleo);
        ofertaEmpleo.setId(id);
        return ofertaEmpleoRepository.save(ofertaEmpleo);
    }

    @Override
    public boolean deleteOferta(UUID id) {
        if (ofertaEmpleoRepository.existsById(id)) {
            ofertaEmpleoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private void validateOfertaEmpleo(OfertaEmpleo ofertaEmpleo) {
        List<String> errors = new ArrayList<>();

        // Validaciones básicas
        if (ofertaEmpleo.getTitulo() == null || ofertaEmpleo.getTitulo().trim().isEmpty()) {
            errors.add("El título es requerido");
        }
        
        if (ofertaEmpleo.getDescripcion() == null || ofertaEmpleo.getDescripcion().trim().isEmpty()) {
            errors.add("La descripción es requerida");
        }

        // Validación de Usuario Publicador
        if (ofertaEmpleo.getUsuarioPublicador() == null || ofertaEmpleo.getUsuarioPublicador().getId() == null) {
            errors.add("El usuario publicador es requerido");
        } else {
            if (!usuarioRepository.existsById(ofertaEmpleo.getUsuarioPublicador().getId())) {
                errors.add("El usuario publicador especificado no existe");
            }
        }

        // Validación de Categoría
        if (ofertaEmpleo.getCategoria() == null || ofertaEmpleo.getCategoria().getId() == null) {
            errors.add("La categoría es requerida");
        } else {
            if (!categoriaRepository.existsById(ofertaEmpleo.getCategoria().getId())) {
                errors.add("La categoría especificada no existe");
            }
        }

        // Validación de fechas
        if (ofertaEmpleo.getFechaCierre() != null) {
            if (ofertaEmpleo.getFechaCierre().isBefore(LocalDateTime.now())) {
                errors.add("La fecha de cierre no puede ser anterior a la fecha actual");
            }
            if (ofertaEmpleo.getFechaCierre().isBefore(ofertaEmpleo.getFechaPublicacion())) {
                errors.add("La fecha de cierre no puede ser anterior a la fecha de publicación");
            }
        }

        // Validaciones de forma de postulación
        if (ofertaEmpleo.getFormaPostulacion() == null) {
            errors.add("La forma de postulación es requerida");
        } else {
            switch (ofertaEmpleo.getFormaPostulacion()) {
                case MAIL:
                    if (ofertaEmpleo.getEmailContacto() == null || ofertaEmpleo.getEmailContacto().trim().isEmpty()) {
                        errors.add("El email de contacto es requerido cuando la forma de postulación es por MAIL");
                    } else if (!isValidEmail(ofertaEmpleo.getEmailContacto())) {
                        errors.add("El formato del email de contacto no es válido");
                    }
                    break;
                case LINK:
                    if (ofertaEmpleo.getLinkPostulacion() == null || ofertaEmpleo.getLinkPostulacion().trim().isEmpty()) {
                        errors.add("El link de postulación es requerido cuando la forma de postulación es por LINK");
                    } else if (!isValidUrl(ofertaEmpleo.getLinkPostulacion())) {
                        errors.add("El formato del link de postulación no es válido");
                    }
                    break;
            }
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(String.join(", ", errors));
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }

    private boolean isValidUrl(String url) {
        try {
            URI uri = new URI(url);
            return uri.getScheme() != null && 
                   (uri.getScheme().equals("http") || uri.getScheme().equals("https"));
        } catch (URISyntaxException e) {
            return false;
        }
    }

    private OfertaEmpleoResponseDTO convertToDTO(OfertaEmpleo ofertaEmpleo) {
        OfertaEmpleoResponseDTO dto = new OfertaEmpleoResponseDTO();
        dto.setId(ofertaEmpleo.getId());
        dto.setTitulo(ofertaEmpleo.getTitulo());
        dto.setDescripcion(ofertaEmpleo.getDescripcion());
        
        // Usuario publicador
        OfertaEmpleoResponseDTO.UsuarioPublicadorDTO usuarioDTO = new OfertaEmpleoResponseDTO.UsuarioPublicadorDTO();
        usuarioDTO.setNombre(ofertaEmpleo.getUsuarioPublicador().getNombre());
        usuarioDTO.setEmail(ofertaEmpleo.getUsuarioPublicador().getEmail());
        dto.setUsuarioPublicador(usuarioDTO);
        
        dto.setEmpresaConsultora(ofertaEmpleo.getEmpresaConsultora());
        dto.setFechaPublicacion(ofertaEmpleo.getFechaPublicacion());
        dto.setFechaCierre(ofertaEmpleo.getFechaCierre());
        dto.setFormaPostulacion(ofertaEmpleo.getFormaPostulacion().toString());
        
        // Contacto según forma de postulación
        if (ofertaEmpleo.getFormaPostulacion() == FormaPostulacionEnum.MAIL) {
            dto.setContactoPostulacion(ofertaEmpleo.getEmailContacto());
        } else if (ofertaEmpleo.getFormaPostulacion() == FormaPostulacionEnum.LINK) {
            dto.setContactoPostulacion(ofertaEmpleo.getLinkPostulacion());
        }
        
        // Categoría
        OfertaEmpleoResponseDTO.CategoriaDTO categoriaDTO = new OfertaEmpleoResponseDTO.CategoriaDTO();
        categoriaDTO.setNombre(ofertaEmpleo.getCategoria().getNombre());
        dto.setCategoria(categoriaDTO);
        
        return dto;
    }
}
