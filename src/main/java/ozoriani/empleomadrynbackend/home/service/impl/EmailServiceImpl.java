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
        mensaje.setSubject("Nuevo Mensaje desde el Formulario de Contacto");
        mensaje.setFrom(emailNuestro);
        mensaje.setReplyTo(contacto.getEmail());

        StringBuilder cuerpoCorreo = new StringBuilder();
        cuerpoCorreo.append("¡Hola equipo!\n\n")
                    .append("Hemos recibido un nuevo mensaje a través del formulario de contacto de Madryn Empleos. Aquí están los detalles:\n\n")
                    .append("--------------------------------------------------\n")
                    .append("Nombre: ").append(contacto.getNombre()).append("\n")
                    .append("Apellido: ").append(contacto.getApellido()).append("\n")
                    .append("Email: ").append(contacto.getEmail()).append("\n")
                    .append("Mensaje: ").append(contacto.getMensaje()).append("\n")
                    .append("--------------------------------------------------\n\n")
                    .append("Por favor, revisa este mensaje y responde al remitente si es necesario utilizando la opción 'Responder' (Reply), ya que el campo Reply-To está configurado con su email.\n\n")
                    .append("Saludos,\n")
                    .append("Sistema Automático de Madryn Empleos");

        mensaje.setText(cuerpoCorreo.toString());
        mailSender.send(mensaje);
    }

    @Override
public void enviarCorreoAvisoEmpresa(OfertaEmpleo ofertaEmpleo) {
    SimpleMailMessage mensaje = new SimpleMailMessage();
    mensaje.setTo(ofertaEmpleo.getEmailContacto());
    mensaje.setSubject("¡Tu oferta de empleo ya está activa en Madryn Empleos!");
    mensaje.setFrom(emailNuestro);

    StringBuilder cuerpoCorreo = new StringBuilder();
    cuerpoCorreo.append("Estimado/a,\n\n")
                .append("¡Buenas noticias! Tu oferta de empleo ha sido revisada y aprobada por nuestro equipo, y ya está publicada en la plataforma de Madryn Empleos. A partir de ahora, miles de candidatos podrán verla y postularse a esta gran oportunidad que ofrece tu empresa.\n\n")
                .append("Detalles de tu oferta activa:\n")
                .append("--------------------------------------------------\n")
                .append("Título: ").append(ofertaEmpleo.getTitulo()).append("\n")
                .append("Empresa: ").append(ofertaEmpleo.getEmpresaConsultora()).append("\n")
                .append("Descripción: ").append(ofertaEmpleo.getDescripcion()).append("\n")
                .append("Email de contacto: ").append(ofertaEmpleo.getEmailContacto()).append("\n")
                .append("Link de postulación: ").append(ofertaEmpleo.getLinkPostulacion() != null ? ofertaEmpleo.getLinkPostulacion() : "No especificado").append("\n")
                .append("Fecha de publicación: ").append(ofertaEmpleo.getFechaPublicacion()).append("\n")
                .append("Fecha de cierre: ").append(ofertaEmpleo.getFechaCierre() != null ? ofertaEmpleo.getFechaCierre() : "Sin fecha límite").append("\n")
                .append("--------------------------------------------------\n\n")
                .append("¿Qué puedes hacer ahora?\n")
                .append("Puedes compartir el enlace de tu oferta con tu red de contactos o simplemente esperar a que los candidatos comiencen a postularse. Si necesitas realizar algún ajuste o tienes preguntas, no dudes en escribirnos a ").append(emailNuestro).append(". ¡Estamos aquí para apoyarte!\n\n")
                .append("Gracias por confiar en Madryn Empleos para conectar tu empresa con el talento que buscas. ¡Te deseamos mucho éxito con esta oferta!\n\n")
                .append("Un cordial saludo,\n")
                .append("El equipo de Madryn Empleos");

    mensaje.setText(cuerpoCorreo.toString());
    mailSender.send(mensaje);
}

    @Override
    public void enviarCorreoAviso(OfertaEmpleo ofertaEmpleo) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(emailNuestro);
        mensaje.setSubject("Nueva Publicación: " + ofertaEmpleo.getEmpresaConsultora());
        mensaje.setFrom(emailNuestro);

        StringBuilder cuerpoCorreo = new StringBuilder();
        cuerpoCorreo.append("¡Hola!\n\n")
                    .append("Se ha registrado una nueva oferta de empleo en la plataforma. A continuación, te compartimos los detalles:\n\n")
                    .append("--------------------------------------------------\n")
                    .append("Título: ").append(ofertaEmpleo.getTitulo()).append("\n")
                    .append("Empresa: ").append(ofertaEmpleo.getEmpresaConsultora()).append("\n")
                    .append("Email de contacto: ").append(ofertaEmpleo.getEmailContacto()).append("\n")
                    .append("Forma de postulación: ").append(ofertaEmpleo.getFormaPostulacion()).append("\n")
                    .append("Link de la oferta: ").append(ofertaEmpleo.getLinkPostulacion()).append("\n")
                    .append("Fecha de publicación: ").append(ofertaEmpleo.getFechaPublicacion()).append("\n")
                    .append("Fecha de cierre: ").append(ofertaEmpleo.getFechaCierre()).append("\n")
                    .append("Categoría: ").append(ofertaEmpleo.getCategoria()).append("\n")
                    .append("Descripción: ").append(ofertaEmpleo.getDescripcion()).append("\n")
                    .append("URL de la imagen: ").append(ofertaEmpleo.getLogoUrl()).append("\n")
                    .append("--------------------------------------------------\n\n")
                    .append("Saludos,\n")
                    .append("Equipo de Empleo Puerto Madryn");

        mensaje.setText(cuerpoCorreo.toString());
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