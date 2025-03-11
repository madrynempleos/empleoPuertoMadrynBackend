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

    private final FavoritosRepository favoritosRepository;

    private final UsuarioRepository usuarioRepository;

    private final OfertaEmpleoRepository ofertaEmpleoRepository;

    @Autowired
    public FavoritosServiceImpl(FavoritosRepository favoritosRepository, UsuarioRepository usuarioRepository, OfertaEmpleoRepository ofertaEmpleoRepository) {
        this.favoritosRepository = favoritosRepository;
        this.usuarioRepository = usuarioRepository;
        this.ofertaEmpleoRepository = ofertaEmpleoRepository;
    }

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
        
        Optional<Favoritos> favorito = favoritosRepository.findByUsuarioIdAndOfertaEmpleoId(usuario.getId(), ofertaId);
        if (favorito.isEmpty()) {
            throw new ResourceNotFoundException("El favorito no existe para usuario: " + userEmail + " y oferta: " + ofertaId);
        }

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