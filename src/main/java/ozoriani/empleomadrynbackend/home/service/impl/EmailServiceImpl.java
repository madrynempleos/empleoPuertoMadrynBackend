package ozoriani.empleomadrynbackend.home.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import ozoriani.empleomadrynbackend.home.model.entities.Contacto;
import ozoriani.empleomadrynbackend.home.model.entities.OfertaEmpleo;
import ozoriani.empleomadrynbackend.home.service.EmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.ByteArrayResource;

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
        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true);
            helper.setTo(ofertaEmpleo.getUsuario().getEmail());
            helper.setSubject("¡Tu oferta de empleo ya está activa en Madryn Empleos!");
            helper.setFrom(emailNuestro);

            String sanitizedTitulo = ofertaEmpleo.getTitulo() != null ? ofertaEmpleo.getTitulo().replace(";", "")
                    : "Sin título";
            String sanitizedEmpresa = ofertaEmpleo.getEmpresaConsultora() != null
                    ? ofertaEmpleo.getEmpresaConsultora().replace(";", "")
                    : "Sin empresa";
            String sanitizedEmail = ofertaEmpleo.getEmailContacto() != null
                    ? ofertaEmpleo.getEmailContacto().replace(";", "")
                    : "Sin email";
            String sanitizedFechaCierre = ofertaEmpleo.getFechaCierre() != null
                    ? ofertaEmpleo.getFechaCierre().toString().replace(";", "")
                    : "Sin fecha límite";

            String htmlContent = String.format(
                    """
                            <html>
                            <body style="font-family: Arial, sans-serif; color: #333; max-width: 600px; margin: 0 auto; padding: 20px;">
                                <div style="background-color: #1e90ff; padding: 15px; text-align: center; color: white; border-radius: 8px 8px 0 0;">
                                    <h1 style="margin: 0; font-size: 24px;">Madryn Empleos</h1>
                                </div>
                                <div style="background-color: #f9f9f9; padding: 20px; border: 1px solid #ddd; border-radius: 0 0 8px 8px;">
                                    <h2 style="color: #1e90ff;">¡Oferta Activa!</h2>
                                    <p>Estimado/a,</p>
                                    <p>¡Buenas noticias! Tu oferta ha sido aprobada y ya está publicada en nuestra plataforma.</p>
                                    <table style="width: 100%%; border-collapse: collapse; margin: 20px 0;">
                                        <tr style="background-color: #f1f1f1;">
                                            <td style="padding: 10px; border: 1px solid #ddd;"><strong>Título:</strong></td>
                                            <td style="padding: 10px; border: 1px solid #ddd;">%s</td>
                                        </tr>
                                        <tr>
                                            <td style="padding: 10px; border: 1px solid #ddd;"><strong>Empresa:</strong></td>
                                            <td style="padding: 10px; border: 1px solid #ddd;">%s</td>
                                        </tr>
                                        <tr style="background-color: #f1f1f1;">
                                            <td style="padding: 10px; border: 1px solid #ddd;"><strong>Email:</strong></td>
                                            <td style="padding: 10px; border: 1px solid #ddd;">%s</td>
                                        </tr>
                                        <tr>
                                            <td style="padding: 10px; border: 1px solid #ddd;"><strong>Fecha de cierre:</strong></td>
                                            <td style="padding: 10px; border: 1px solid #ddd;">%s</td>
                                        </tr>
                                    </table>
                                    <p>Si necesitas ayuda, escríbenos a <a href="mailto:%s" style="color: #1e90ff;">%s</a>.</p>
                                    <p style="color: #777; font-size: 12px; text-align: center;">El equipo de Madryn Empleos</p>
                                </div>
                            </body>
                            </html>
                            """,
                    sanitizedTitulo,
                    sanitizedEmpresa,
                    sanitizedEmail,
                    sanitizedFechaCierre,
                    emailNuestro,
                    emailNuestro);

            helper.setText(htmlContent, true);
            mailSender.send(mensaje);
        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar el aviso a la empresa: " + e.getMessage(), e);
        }
    }

    @Override
    public void enviarCorreoAviso(OfertaEmpleo ofertaEmpleo) {
        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true);
            helper.setTo(emailNuestro);
            helper.setSubject("Nueva Publicación: " + ofertaEmpleo.getEmpresaConsultora());
            helper.setFrom(emailNuestro);

            String titulo = ofertaEmpleo.getTitulo() != null ? ofertaEmpleo.getTitulo().replace(";", "") : "Sin título";
            String empresa = ofertaEmpleo.getEmpresaConsultora() != null
                    ? ofertaEmpleo.getEmpresaConsultora().replace(";", "")
                    : "Sin empresa";
            String email = ofertaEmpleo.getEmailContacto() != null ? ofertaEmpleo.getEmailContacto().replace(";", "")
                    : "Sin email";
            String forma = ofertaEmpleo.getFormaPostulacion() != null
                    ? ofertaEmpleo.getFormaPostulacion().toString().replace(";", "")
                    : "Sin forma";

            String htmlContent = String.format(
                    """
                            <html>
                            <body style="font-family: Arial, sans-serif; color: #333; max-width: 600px; margin: 0 auto; padding: 20px;">
                                <div style="background-color: #1e90ff; padding: 15px; text-align: center; color: white; border-radius: 8px 8px 0 0;">
                                    <h1 style="margin: 0; font-size: 24px;">Madryn Empleos</h1>
                                </div>
                                <div style="background-color: #f9f9f9; padding: 20px; border: 1px solid #ddd; border-radius: 0 0 8px 8px;">
                                    <h2 style="color: #1e90ff;">Nueva Oferta Registrada</h2>
                                    <p>¡Hola equipo!</p>
                                    <p>Se ha registrado una nueva oferta en la plataforma:</p>
                                    <table style="width: 100%%; border-collapse: collapse; margin: 20px 0;">
                                        <tr style="background-color: #f1f1f1;">
                                            <td style="padding: 10px; border: 1px solid #ddd;"><strong>Título:</strong></td>
                                            <td style="padding: 10px; border: 1px solid #ddd;">%s</td>
                                        </tr>
                                        <tr>
                                            <td style="padding: 10px; border: 1px solid #ddd;"><strong>Empresa:</strong></td>
                                            <td style="padding: 10px; border: 1px solid #ddd;">%s</td>
                                        </tr>
                                        <tr style="background-color: #f1f1f1;">
                                            <td style="padding: 10px; border: 1px solid #ddd;"><strong>Email:</strong></td>
                                            <td style="padding: 10px; border: 1px solid #ddd;">%s</td>
                                        </tr>
                                        <tr>
                                            <td style="padding: 10px; border: 1px solid #ddd;"><strong>Forma:</strong></td>
                                            <td style="padding: 10px; border: 1px solid #ddd;">%s</td>
                                        </tr>
                                    </table>
                                    <p style="color: #777; font-size: 12px; text-align: center;">Equipo de Madryn Empleos</p>
                                </div>
                            </body>
                            </html>
                            """,
                    titulo,
                    empresa,
                    email,
                    forma);

            helper.setText(htmlContent, true);
            mailSender.send(mensaje);
        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar el aviso: " + e.getMessage(), e);
        }
    }

    @Override
    public void enviarCorreoAvisoRechazado(OfertaEmpleo ofertaEmpleo) {
        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true);
            helper.setTo(ofertaEmpleo.getUsuario().getEmail());
            helper.setSubject("Tu oferta de empleo ha sido rechazada");
            helper.setFrom(emailNuestro);

            String htmlContent = """
                    <html>
                    <body style="font-family: Arial, sans-serif; color: #333; max-width: 600px; margin: 0 auto; padding: 20px;">
                        <div style="background-color: #1e90ff; padding: 15px; text-align: center; color: white; border-radius: 8px 8px 0 0;">
                            <h1 style="margin: 0; font-size: 24px;">Madryn Empleos</h1>
                        </div>
                        <div style="background-color: #f9f9f9; padding: 20px; border: 1px solid #ddd; border-radius: 0 0 8px 8px;">
                            <h2 style="color: #ff4500;">Oferta Rechazada</h2>
                            <p>Estimado/a,</p>
                            <p>Lamentamos informarte que tu oferta ha sido rechazada por nuestro equipo.</p>
                            <table style="width: 100%; border-collapse: collapse; margin: 20px 0;">
                                <tr style="background-color: #f1f1f1;">
                                    <td style="padding: 10px; border: 1px solid #ddd;"><strong>Título:</strong></td>
                                    <td style="padding: 10px; border: 1px solid #ddd;">%s</td>
                                </tr>
                                <tr>
                                    <td style="padding: 10px; border: 1px solid #ddd;"><strong>Empresa:</strong></td>
                                    <td style="padding: 10px; border: 1px solid #ddd;">%s</td>
                                </tr>
                            </table>
                            <p>Si tienes preguntas, contáctanos en <a href="mailto:%s" style="color: #1e90ff;">%s</a>.</p>
                            <p style="color: #777; font-size: 12px; text-align: center;">Equipo de Madryn Empleos</p>
                        </div>
                    </body>
                    </html>
                    """
                    .formatted(
                            ofertaEmpleo.getTitulo(),
                            ofertaEmpleo.getEmpresaConsultora(),
                            emailNuestro,
                            emailNuestro);

            helper.setText(htmlContent, true);
            mailSender.send(mensaje);
        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar el aviso de rechazo: " + e.getMessage(), e);
        }
    }

    @Override
    public void enviarCorreoPostulacion(OfertaEmpleo oferta, String applicantEmail, byte[] curriculumData,
            String curriculumFileName) {
        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true);
            String recipientEmail = oferta.getEmailContacto() != null && !oferta.getEmailContacto().isEmpty()
                    ? oferta.getEmailContacto()
                    : oferta.getUsuario().getEmail();
            helper.setTo(recipientEmail);
            helper.setSubject("Nueva postulación para '"
                    + (oferta.getTitulo() != null ? oferta.getTitulo().replace(";", "") : "Sin título") + "'");
            helper.setFrom(emailNuestro);
            helper.setReplyTo(applicantEmail);

            // Sanitizamos los valores
            String sanitizedApplicantEmail = applicantEmail != null ? applicantEmail.replace(";", "") : "Sin email";
            String sanitizedTitulo = oferta.getTitulo() != null ? oferta.getTitulo().replace(";", "") : "Sin título";
            String sanitizedEmpresa = oferta.getEmpresaConsultora() != null
                    ? oferta.getEmpresaConsultora().replace(";", "")
                    : "Sin empresa";

            String htmlContent = String.format(
                    """
                            <html>
                            <body style="font-family: Arial, sans-serif; color: #333; max-width: 600px; margin: 0 auto; padding: 20px;">
                                <div style="background-color: #1e90ff; padding: 15px; text-align: center; color: white; border-radius: 8px 8px 0 0;">
                                    <h1 style="margin: 0; font-size: 24px;">Madryn Empleos</h1>
                                </div>
                                <div style="background-color: #f9f9f9; padding: 20px; border: 1px solid #ddd; border-radius: 0 0 8px 8px;">
                                    <h2 style="color: #1e90ff;">Nueva Postulación</h2>
                                    <p>Estimado/a,</p>
                                    <p><strong>%s</strong> se ha postulado para su puesto de "<strong>%s</strong>" a través de Madryn Empleos.</p>
                                    <p>Adjuntamos su currículum para que pueda revisarlo. ¡Gracias por confiar en nosotros para conectar con el talento que busca!</p>
                                    <table style="width: 100%%; border-collapse: collapse; margin: 20px 0;">
                                        <tr style="background-color: #f1f1f1;">
                                            <td style="padding: 10px; border: 1px solid #ddd;"><strong>Título:</strong></td>
                                            <td style="padding: 10px; border: 1px solid #ddd;">%s</td>
                                        </tr>
                                        <tr>
                                            <td style="padding: 10px; border: 1px solid #ddd;"><strong>Empresa:</strong></td>
                                            <td style="padding: 10px; border: 1px solid #ddd;">%s</td>
                                        </tr>
                                    </table>
                                    <p>Si tiene alguna duda, contáctenos en <a href="mailto:%s" style="color: #1e90ff;">%s</a>.</p>
                                    <p style="color: #777; font-size: 12px; text-align: center;">El equipo de Madryn Empleos</p>
                                </div>
                            </body>
                            </html>
                            """,
                    sanitizedApplicantEmail,
                    sanitizedTitulo,
                    sanitizedTitulo,
                    sanitizedEmpresa,
                    emailNuestro,
                    emailNuestro);

            helper.setText(htmlContent, true);
            helper.addAttachment(curriculumFileName, new ByteArrayResource(curriculumData));
            mailSender.send(mensaje);
        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar el correo de postulación: " + e.getMessage(), e);
        }
    }

    @Override
    public void enviarConfirmacionPostulante(String applicantEmail, OfertaEmpleo oferta) {
        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true);
            helper.setTo(applicantEmail);
            helper.setSubject("¡Tu postulación fue enviada con éxito!");
            helper.setFrom(emailNuestro);

            String sanitizedTitulo = oferta.getTitulo() != null ? oferta.getTitulo().replace(";", "") : "Sin título";
            String sanitizedEmpresa = oferta.getEmpresaConsultora() != null
                    ? oferta.getEmpresaConsultora().replace(";", "")
                    : "Sin empresa";

            String htmlContent = String.format(
                    """
                            <html>
                            <body style="font-family: Arial, sans-serif; color: #333; max-width: 600px; margin: 0 auto; padding: 20px;">
                                <div style="background-color: #1e90ff; padding: 15px; text-align: center; color: white; border-radius: 8px 8px 0 0;">
                                    <h1 style="margin: 0; font-size: 24px;">Madryn Empleos</h1>
                                </div>
                                <div style="background-color: #f9f9f9; padding: 20px; border: 1px solid #ddd; border-radius: 0 0 8px 8px;">
                                    <h2 style="color: #1e90ff;">¡Postulación Enviada!</h2>
                                    <p>Hola,</p>
                                    <p>Hemos enviado tu currículum para el puesto de "<strong>%s</strong>" en <strong>%s</strong>.</p>
                                    <p>Si el empleador necesita contactarte, lo hará directamente. ¡Te deseamos mucho éxito!</p>
                                    <p style="background-color: #e6f3ff; padding: 10px; border-radius: 4px; margin: 20px 0;">
                                        Somos un proyecto sin ánimo de lucro. Si conocés empresas que quieran publicar ofertas, invitalas a visitar
                                        <a href="https://madryn-empleos.vercel.app" style="color: #1e90ff;">madryn-empleos.vercel.app</a>.
                                    </p>
                                    <p style="color: #777; font-size: 12px; text-align: center;">El equipo de Madryn Empleos</p>
                                </div>
                            </body>
                            </html>
                            """,
                    sanitizedTitulo,
                    sanitizedEmpresa);

            helper.setText(htmlContent, true);
            mailSender.send(mensaje);
        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar la confirmación al postulante: " + e.getMessage(), e);
        }
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
        } else if (!isValidEmail(contacto.getEmail())) {
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