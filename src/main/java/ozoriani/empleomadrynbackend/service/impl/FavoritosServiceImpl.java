package ozoriani.empleomadrynbackend.service.impl;

import ozoriani.empleomadrynbackend.errors.exception.ResourceNotFoundException;
import ozoriani.empleomadrynbackend.model.Favoritos;
import ozoriani.empleomadrynbackend.model.OfertaEmpleo;
import ozoriani.empleomadrynbackend.model.Usuario;
import ozoriani.empleomadrynbackend.repository.FavoritosRepository;
import ozoriani.empleomadrynbackend.repository.OfertaEmpleoRepository;
import ozoriani.empleomadrynbackend.repository.UsuarioRepository;
import ozoriani.empleomadrynbackend.service.FavoritosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FavoritosServiceImpl implements FavoritosService {

    @Autowired
    private FavoritosRepository favoritosRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private OfertaEmpleoRepository ofertaEmpleoRepository;

    @Override
    public Favoritos addFavorite(String userEmail, UUID ofertaId) {
        Usuario usuario = usuarioRepository.findByEmail(userEmail)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con email: " + userEmail));
        OfertaEmpleo oferta = ofertaEmpleoRepository.findById(ofertaId)
            .orElseThrow(() -> new ResourceNotFoundException("Oferta no encontrada con ID: " + ofertaId));

        if (favoritosRepository.findByUsuarioIdAndOfertaEmpleoId(usuario.getId(), ofertaId).isPresent()) {
            throw new IllegalStateException("La oferta ya está en favoritos");
        }

        Favoritos favorito = new Favoritos();
        favorito.setUsuario(usuario);
        favorito.setOfertaEmpleo(oferta);
        return favoritosRepository.save(favorito);
    }

    @Override
    public void removeFavorite(String userEmail, UUID ofertaId) {
        Usuario usuario = usuarioRepository.findByEmail(userEmail)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con email: " + userEmail));
        
        // Buscar el favorito por ID de usuario y oferta
        Optional<Favoritos> favorito = favoritosRepository.findByUsuarioIdAndOfertaEmpleoId(usuario.getId(), ofertaId);
        if (!favorito.isPresent()) {
            throw new ResourceNotFoundException("El favorito no existe para usuario: " + userEmail + " y oferta: " + ofertaId);
        }

        // Eliminar el favorito por su ID en lugar de usar deleteByUsuarioIdAndOfertaEmpleoId
        favoritosRepository.delete(favorito.get());
    }

    @Override
    public List<Favoritos> getUserFavorites(String userEmail) {
        Usuario usuario = usuarioRepository.findByEmail(userEmail)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con email: " + userEmail));
        return favoritosRepository.findByUsuarioId(usuario.getId());
    }

    @Override
    public boolean isFavorite(String userEmail, UUID ofertaId) {
        Usuario usuario = usuarioRepository.findByEmail(userEmail)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con email: " + userEmail));
        return favoritosRepository.findByUsuarioIdAndOfertaEmpleoId(usuario.getId(), ofertaId).isPresent();
    }
}