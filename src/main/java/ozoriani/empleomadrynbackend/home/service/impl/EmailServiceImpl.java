package ozoriani.empleomadrynbackend.home.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import ozoriani.empleomadrynbackend.home.model.entities.Contacto;
import ozoriani.empleomadrynbackend.home.model.entities.OfertaEmpleo;
import ozoriani.empleomadrynbackend.home.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;
    private static final String emailNuestro = "empleospuertomadryn@gmail.com";

    @Override
    public void enviarCorreoContacto(Contacto contacto) {
        validateContacto(contacto);
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(emailNuestro);
        mensaje.setSubject("Nuevo mensaje desde el formulario de contacto");
        mensaje.setText("Nombre: " + contacto.getNombre() + "\n" +
                        "Apellido: " + contacto.getApellido() + "\n" +
                        "Email: " + contacto.getEmail() + "\n" +
                        "Mensaje: " + contacto.getMensaje());
        mensaje.setFrom(emailNuestro);
        mensaje.setReplyTo(contacto.getEmail());

        mailSender.send(mensaje);
    }

    @Override
    public void enviarCorreoAvisoEmpresa(OfertaEmpleo ofertaEmpleo){
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(ofertaEmpleo.getEmailContacto());
        mensaje.setSubject("Publicación de oferta de empleo en Madryn Empleos");
        mensaje.setText("Gracias por enviar su publicación a Madryn Empleos. Queremos informarle que su publicación ha sido registrada exitosamente y será revisada por nuestro equipo. Una vez que verifiquemos que todo esté en orden, la publicaremos en nuestra web. \n" +
        "Si tiene alguna consulta, no dude en contactarnos. \n" +
                        "Gracias por confiar en nosotros. \n \n" + "El equipo de Madryn Empleos");
        mensaje.setFrom(emailNuestro);

        mailSender.send(mensaje);
    }

    @Override
    public void enviarCorreoAviso(OfertaEmpleo ofertaEmpleo){
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(emailNuestro);
        mensaje.setSubject("Publicación de oferta de empleo en Madryn Empleos");
        mensaje.setText("Se registro una nueva oferta en la pagina. \n" +
        "Titulo de la oferta: " + ofertaEmpleo.getTitulo()+ "\n" +
        "Nombre de la empresa: " + ofertaEmpleo.getEmpresaConsultora() + "\n" +
        "Email de contacto: " + ofertaEmpleo.getEmailContacto() + "\n" +
        "Forma de postulacion: " + ofertaEmpleo.getFormaPostulacion() + "\n" +
        "Link de la oferta: " + ofertaEmpleo.getLinkPostulacion() + "\n" +
        "Fecha de publicacion: " + ofertaEmpleo.getFechaPublicacion() + "\n" +
        "Fecha de cierre: " + ofertaEmpleo.getFechaCierre() + "\n" +
        "Descripcion: " + ofertaEmpleo.getDescripcion() + "\n" +
        "Categoria: " + ofertaEmpleo.getCategoria() + "\n" +
        "Imagen: " + ofertaEmpleo.getLogoUrl() + "\n");
        mensaje.setFrom(emailNuestro);

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