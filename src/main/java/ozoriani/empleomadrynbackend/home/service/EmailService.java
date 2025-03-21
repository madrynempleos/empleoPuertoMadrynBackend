package ozoriani.empleomadrynbackend.home.service;

import ozoriani.empleomadrynbackend.home.model.entities.Contacto;

public interface EmailService {
    void enviarCorreoContacto(Contacto contacto);
}
