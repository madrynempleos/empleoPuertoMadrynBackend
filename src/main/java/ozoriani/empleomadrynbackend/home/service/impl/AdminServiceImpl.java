package ozoriani.empleomadrynbackend.home.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ozoriani.empleomadrynbackend.home.model.entities.OfertaEmpleo;
import ozoriani.empleomadrynbackend.home.model.repository.CategoriaRepository;
import ozoriani.empleomadrynbackend.home.model.repository.OfertaEmpleoRepository;
import ozoriani.empleomadrynbackend.home.model.repository.UsuarioRepository;
import ozoriani.empleomadrynbackend.home.service.AdminService;
import ozoriani.empleomadrynbackend.home.service.EmailService;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class AdminServiceImpl implements AdminService {

    @PersistenceContext
    private EntityManager entityManager;

    private final Map<String, JpaRepository<?, UUID>> repositories = new HashMap<>();

    private final EmailService emailService;

    public AdminServiceImpl(OfertaEmpleoRepository ofertaRepository, CategoriaRepository categoriaRepository, UsuarioRepository usuarioRepository, EmailService emailService) {
        repositories.put("ofertas", ofertaRepository);
        repositories.put("categoria", categoriaRepository);
        repositories.put("usuario", usuarioRepository);
        this.emailService = emailService;
    }

    @Override
    public List<?> getAllFromEntity(String entityName) {
        JpaRepository<?, UUID> repository = getRepository(entityName);
        return repository.findAll();
    }

    @Override
    public Object getByIdFromEntity(String entityName, UUID id) {
        JpaRepository<?, UUID> repository = getRepository(entityName);
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException(entityName + " no encontrada con ID: " + id));
    }

    @Override
    public Object updateEntity(String entityName, UUID id, Object updatedEntity) {
        JpaRepository<?, UUID> repository = getRepository(entityName);
        Object existingEntity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException(entityName + " no encontrada con ID: " + id));
        
        copyProperties(updatedEntity, existingEntity);

        JpaRepository<Object, UUID> typedRepository = (JpaRepository<Object, UUID>) repository;
        return typedRepository.save(existingEntity);
    }

    @Override
    public void deleteEntity(String entityName, UUID id) {
        JpaRepository<?, UUID> repository = getRepository(entityName);
        if (!repository.existsById(id)) {
            throw new RuntimeException(entityName + " no encontrada con ID: " + id);
        }
        if (entityName.equals("ofertas")) {
            emailService.enviarCorreoAvisoRechazado((OfertaEmpleo) getByIdFromEntity(entityName, id));
        }
        repository.deleteById(id);
    }

    @Override
    public void enableJobOffer(UUID id) {
        OfertaEmpleoRepository ofertaRepository = (OfertaEmpleoRepository) repositories.get("ofertas");
        if (ofertaRepository == null) {
            throw new RuntimeException("Entidad no soportada: Oferta");
        }
        OfertaEmpleo oferta = ofertaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Oferta de empleo no encontrada con ID: " + id));
        if (oferta.getHabilitado()) {
            throw new RuntimeException("La oferta de empleo ya está habilitada con ID: " + id);
        }
        oferta.setHabilitado(true);
        ofertaRepository.save(oferta);
        emailService.enviarCorreoAvisoEmpresa(oferta);
    }

    private JpaRepository<?, UUID> getRepository(String entityName) {
        JpaRepository<?, UUID> repository = repositories.get(entityName);
        if (repository == null) {
            throw new RuntimeException("Entidad no soportada: " + entityName);
        }
        return repository;
    }

    private void copyProperties(Object source, Object target) {
        try {
            org.springframework.beans.BeanUtils.copyProperties(source, target);
        } catch (Exception e) {
            throw new RuntimeException("Error al copiar propiedades: " + e.getMessage(), e);
        }
    }
}