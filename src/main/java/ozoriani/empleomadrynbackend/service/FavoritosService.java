package ozoriani.empleomadrynbackend.service;

import ozoriani.empleomadrynbackend.model.Favoritos;
import java.util.List;
import java.util.UUID;

public interface FavoritosService {
    Favoritos addFavorite(String userEmail, UUID ofertaId);
    void removeFavorite(String userEmail, UUID ofertaId);
    List<Favoritos> getUserFavorites(String userEmail);
    boolean isFavorite(String userEmail, UUID ofertaId);
}