package ozoriani.empleomadrynbackend.home.service;

import java.util.List;
import java.util.UUID;

import ozoriani.empleomadrynbackend.home.model.entities.Favoritos;

public interface FavoritosService {
    Favoritos addFavorite(String userEmail, UUID ofertaId);

    void removeFavorite(String userEmail, UUID ofertaId);

    List<Favoritos> getUserFavorites(String userEmail);

    boolean isFavorite(String userEmail, UUID ofertaId);
}