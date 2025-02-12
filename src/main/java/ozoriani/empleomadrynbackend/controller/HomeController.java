package ozoriani.empleomadrynbackend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Bienvenido a Empleo Madryn Backend";
    }

    @GetMapping("/secured")
    public String secured() {
        return "Este es un endpoint de seguridad";
    }
}
