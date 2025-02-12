package ozoriani.empleomadrynbackend.service.impl;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import ozoriani.empleomadrynbackend.model.Usuario;
import ozoriani.empleomadrynbackend.service.UsuarioService;

import java.util.Collections;

@Service
public class OAuth2UserServiceImpl implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UsuarioService usuarioService;

    public OAuth2UserServiceImpl(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");
        String nombre = oAuth2User.getAttribute("name");

        crearUsuarioAPI(email, nombre);

        return new DefaultOAuth2User(Collections.singleton(() -> "ROLE_USER"), oAuth2User.getAttributes(), "email");
    }


    private void crearUsuarioAPI(String email, String name) {
        usuarioService.findByEmail(email).orElseGet(() -> {
            Usuario usuario = new Usuario();
            usuario.setEmail(email);
            usuario.setNombre(name);
            usuario.setPassword("auth2");
            usuarioService.createUsuario(usuario);
            return usuario;
        });
    }
}
