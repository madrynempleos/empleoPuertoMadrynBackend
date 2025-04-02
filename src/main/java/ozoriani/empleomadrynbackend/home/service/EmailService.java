package ozoriani.empleomadrynbackend.home.service;

import ozoriani.empleomadrynbackend.home.model.entities.Contacto;
import ozoriani.empleomadrynbackend.home.model.entities.OfertaEmpleo;

public interface EmailService {
    void enviarCorreoContacto(Contacto contacto);
    void enviarCorreoAvisoEmpresa(OfertaEmpleo ofertaEmpleo);
    void enviarCorreoAviso(OfertaEmpleo ofertaEmpleo);
    void enviarCorreoAvisoRechazado(OfertaEmpleo ofertaEmpleo);
    void enviarCorreoPostulacion(OfertaEmpleo ofertaEmpleo, String applicantEmail, byte[] curriculumData, String curriculumFileName);
    void enviarConfirmacionPostulante(String applicantEmail, OfertaEmpleo oferta);
}
