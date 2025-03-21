package ozoriani.empleomadrynbackend.home.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import ozoriani.empleomadrynbackend.home.model.entities.Contacto;
import ozoriani.empleomadrynbackend.home.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void enviarCorreoContacto(Contacto contacto) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo("empleospuertomadryn@gmail.com");
        mensaje.setSubject("Nuevo mensaje desde el formulario de contacto");
        mensaje.setText("Nombre: " + contacto.getNombre() + "\n" +
                        "Apellido: " + contacto.getApellido() + "\n" +
                        "Email: " + contacto.getEmail() + "\n" +
                        "Mensaje: " + contacto.getMensaje());
        mensaje.setFrom("empleospuertomadryn@gmail.com"); // O un correo genérico si prefieres

        mailSender.send(mensaje);
    }
}