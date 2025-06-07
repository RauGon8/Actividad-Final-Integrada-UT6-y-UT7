package com.dam.usuarios_notas.service;



import com.dam.usuarios_notas.model.Usuario;
import com.dam.usuarios_notas.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository){
        this.usuarioRepository =usuarioRepository;
    }

    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> getUsuarioById(Long id){
        return usuarioRepository.findById(id);
    }

    public Usuario createUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public Usuario updateUsuario(Long id, Usuario usuarioDetails){
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"usuario con ID " + id +" no encontrado."));

        usuario.setNombre(usuarioDetails.getNombre());
        usuario.setEmail(usuarioDetails.getEmail());
        usuario.setPasswordHash(usuarioDetails.getPasswordHash());

        return usuarioRepository.save(usuario);
    }

    public void deleteUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"usuario con ID " + id +" no encontrado.");
        }
        usuarioRepository.deleteById(id);
    }

    public Usuario signIn(String email, String password){
        Optional<Usuario> optionalUsuario = usuarioRepository.findByEmail(email);
        if (optionalUsuario.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "credenciales inválidas: Usuario no encontrado.");
        }
        Usuario usuario =optionalUsuario.get();

        if (!usuario.getPasswordHash().equals(password)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "credenciales inválidas: Contraseña incorrecta.");
        }
        return usuario;
    }
}