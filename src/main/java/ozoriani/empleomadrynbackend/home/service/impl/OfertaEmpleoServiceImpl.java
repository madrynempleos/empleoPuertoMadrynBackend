package ozoriani.empleomadrynbackend.home.service.impl;

import ozoriani.empleomadrynbackend.errors.exception.ResourceNotFoundException;
import ozoriani.empleomadrynbackend.errors.exception.ValidationException;
import ozoriani.empleomadrynbackend.home.model.dto.OfertaEmpleoResponseDTO;
import ozoriani.empleomadrynbackend.home.model.entities.FormaPostulacionEnum;
import ozoriani.empleomadrynbackend.home.model.entities.OfertaEmpleo;
import ozoriani.empleomadrynbackend.home.model.repository.CategoriaRepository;
import ozoriani.empleomadrynbackend.home.model.repository.OfertaEmpleoRepository;
import ozoriani.empleomadrynbackend.home.model.repository.UsuarioRepository;
import ozoriani.empleomadrynbackend.home.service.OfertaEmpleoService;

import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OfertaEmpleoServiceImpl implements OfertaEmpleoService {

    private final OfertaEmpleoRepository ofertaEmpleoRepository;

    private final UsuarioRepository usuarioRepository;

    private final CategoriaRepository categoriaRepository;

    public OfertaEmpleoServiceImpl(OfertaEmpleoRepository ofertaEmpleoRepository, UsuarioRepository usuarioRepository,
            CategoriaRepository categoriaRepository) {
        this.ofertaEmpleoRepository = ofertaEmpleoRepository;
        this.usuarioRepository = usuarioRepository;
        this.categoriaRepository = categoriaRepository;
    }

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
        OfertaEmpleo existingOferta = ofertaEmpleoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Oferta de empleo no encontrada con ID: " + id));

        if (!ofertaEmpleoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Oferta de empleo no encontrada con ID: " + id);
        }

        ofertaEmpleo.setFechaPublicacion(existingOferta.getFechaPublicacion());
        validateOfertaEmpleo(ofertaEmpleo);
        ofertaEmpleo.setId(id);
        return ofertaEmpleoRepository.save(ofertaEmpleo);
    }

    @Override
    public void deleteOferta(UUID id) {
        if (!ofertaEmpleoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Oferta de empleo no encontrada con ID: " + id);
        }
        ofertaEmpleoRepository.deleteById(id);
    }

    @Override
    public List<OfertaEmpleoResponseDTO> getUserJobPosts(String userEmail) {
        return ofertaEmpleoRepository.findByUsuarioEmail(userEmail).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private void validateOfertaEmpleo(OfertaEmpleo ofertaEmpleo) {
        List<String> errors = new ArrayList<>();

        if (ofertaEmpleo.getUsuario() == null || ofertaEmpleo.getUsuario().getId() == null) {
            errors.add("El usuario publicador es requerido");
        } else if (!usuarioRepository.existsById(ofertaEmpleo.getUsuario().getId())) {
            errors.add("El usuario publicador especificado no existe");
        }

        // Validación de Categoría
        if (ofertaEmpleo.getCategoria() == null || ofertaEmpleo.getCategoria().getId() == null) {
            errors.add("La categoría es requerida");
        } else if (!categoriaRepository.existsById(ofertaEmpleo.getCategoria().getId())) {
            errors.add("La categoría especificada no existe");
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
                case MAIL -> {
                    if (ofertaEmpleo.getEmailContacto() == null || ofertaEmpleo.getEmailContacto().trim().isEmpty()) {
                        errors.add("El email de contacto es requerido cuando la forma de postulación es por MAIL");
                    } else if (!isValidEmail(ofertaEmpleo.getEmailContacto())) {
                        errors.add("El formato del email de contacto no es válido");
                    }
                }
                case LINK -> {
                    if (ofertaEmpleo.getLinkPostulacion() == null
                            || ofertaEmpleo.getLinkPostulacion().trim().isEmpty()) {
                        errors.add("El link de postulación es requerido cuando la forma de postulación es por LINK");
                    } else if (!isValidUrl(ofertaEmpleo.getLinkPostulacion())) {
                        errors.add("El formato del link de postulación no es válido");
                    }
                }
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
            throw new ValidationException("El formato del link de postulación no es válido");
        }
    }

    private OfertaEmpleoResponseDTO convertToDTO(OfertaEmpleo ofertaEmpleo) {
        OfertaEmpleoResponseDTO dto = new OfertaEmpleoResponseDTO();
        dto.setId(ofertaEmpleo.getId());
        dto.setTitulo(ofertaEmpleo.getTitulo());
        dto.setDescripcion(ofertaEmpleo.getDescripcion());

        OfertaEmpleoResponseDTO.UsuarioPublicadorDTO usuarioDTO = new OfertaEmpleoResponseDTO.UsuarioPublicadorDTO();
        usuarioDTO.setEmail(ofertaEmpleo.getUsuario().getEmail());
        dto.setUsuarioPublicador(usuarioDTO);

        dto.setEmpresaConsultora(ofertaEmpleo.getEmpresaConsultora());
        dto.setFechaPublicacion(ofertaEmpleo.getFechaPublicacion());
        dto.setFechaCierre(ofertaEmpleo.getFechaCierre());
        dto.setFormaPostulacion(ofertaEmpleo.getFormaPostulacion().toString());

        if (ofertaEmpleo.getFormaPostulacion() == FormaPostulacionEnum.MAIL) {
            dto.setContactoPostulacion(ofertaEmpleo.getEmailContacto());
        } else if (ofertaEmpleo.getFormaPostulacion() == FormaPostulacionEnum.LINK) {
            dto.setContactoPostulacion(ofertaEmpleo.getLinkPostulacion());
        }

        OfertaEmpleoResponseDTO.CategoriaDTO categoriaDTO = new OfertaEmpleoResponseDTO.CategoriaDTO();
        categoriaDTO.setId(ofertaEmpleo.getCategoria().getId().toString()); 
        categoriaDTO.setNombre(ofertaEmpleo.getCategoria().getNombre());
        dto.setCategoria(categoriaDTO);

        dto.setLogoUrl(ofertaEmpleo.getLogoUrl());

        return dto;
    }
}