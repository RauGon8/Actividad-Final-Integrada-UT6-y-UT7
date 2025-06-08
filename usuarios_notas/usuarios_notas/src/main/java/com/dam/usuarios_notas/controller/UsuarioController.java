package com.dam.usuarios_notas.controller;

import com.dam.usuarios_notas.model.Usuario;
import com.dam.usuarios_notas.service.UsuarioService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email; 
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // Endpoints v1
    @GetMapping("/api/v1/usuarios")
    public ResponseEntity<List<Usuario>> getAllUsuariosV1() {
        return ResponseEntity.ok(usuarioService.getAllUsuarios());
    }

    @GetMapping("/api/v1/usuarios/{id}")
    public ResponseEntity<Usuario> getUsuarioByIdV1(@PathVariable Long id) {
        return usuarioService.getUsuarioById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario con ID " + id + " no encontrado."));
    }

    @PostMapping("/api/v1/usuarios")
    public ResponseEntity<Usuario> createUsuarioV1(@Valid @RequestBody Usuario usuario) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.createUsuario(usuario));
    }

    @PutMapping("/api/v1/usuarios/{id}")
    public ResponseEntity<Usuario> updateUsuarioV1(@PathVariable Long id, @Valid @RequestBody Usuario usuario) {
        if (usuario.getId() != null && !usuario.getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El ID en la ruta no coincide con el ID en el cuerpo de la solicitud.");
        }
        usuario.setId(id);
        return ResponseEntity.ok(usuarioService.updateUsuario(id, usuario));
    }

    @DeleteMapping("/api/v1/usuarios/{id}")
    public ResponseEntity<Void> deleteUsuarioV1(@PathVariable Long id) {
        usuarioService.deleteUsuario(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoints v2
    @PostMapping("/api/v2/sign-in")
    public ResponseEntity<Usuario> signInV2(
            @RequestParam @NotBlank(message = "El email no puede estar vacío") @Email(message = "El email debe ser válido") String email,
            @RequestParam @NotBlank(message = "La contraseña no puede estar vacía") String password) {
        Usuario usuario = usuarioService.signIn(email, password);
        return ResponseEntity.ok(usuario);
    }
}