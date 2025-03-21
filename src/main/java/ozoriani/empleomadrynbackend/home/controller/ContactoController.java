package ozoriani.empleomadrynbackend.home.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ozoriani.empleomadrynbackend.home.model.entities.Contacto;
import ozoriani.empleomadrynbackend.home.service.EmailService;

@RestController
@RequestMapping("/api/contacto")
public class ContactoController {

    @Autowired
    private EmailService emailService;

    @PostMapping
    public String enviarContacto(@RequestBody Contacto contacto) {
        try {
            emailService.enviarCorreoContacto(contacto);
            return "Mensaje enviado con éxito";
        } catch (Exception e) {
            return "Error al enviar el mensaje: " + e.getMessage();
        }
        
    }
}