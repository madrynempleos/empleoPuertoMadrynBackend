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
        validateContacto(contacto);
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo("empleospuertomadryn@gmail.com");
        mensaje.setSubject("Nuevo mensaje desde el formulario de contacto");
        mensaje.setText("Nombre: " + contacto.getNombre() + "\n" +
                        "Apellido: " + contacto.getApellido() + "\n" +
                        "Email: " + contacto.getEmail() + "\n" +
                        "Mensaje: " + contacto.getMensaje());
        mensaje.setFrom("empleospuertomadryn@gmail.com");
        mensaje.setReplyTo(contacto.getEmail());

        mailSender.send(mensaje);
    }

    private void validateContacto(Contacto contacto) {
        if (contacto.getNombre() == null || contacto.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo o vacío");
        }
        if (contacto.getApellido() == null || contacto.getApellido().isEmpty()) {
            throw new IllegalArgumentException("El apellido no puede ser nulo o vacío");
        }
        if (contacto.getEmail() == null || contacto.getEmail().isEmpty()) {
            throw new IllegalArgumentException("El email no puede ser nulo o vacío");
        }else if (!isValidEmail(contacto.getEmail())) {
            throw new IllegalArgumentException("El email no es válido");
        }
        if (contacto.getMensaje() == null || contacto.getMensaje().isEmpty()) {
            throw new IllegalArgumentException("El mensaje no puede ser nulo o vacío");
        }        
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }
}