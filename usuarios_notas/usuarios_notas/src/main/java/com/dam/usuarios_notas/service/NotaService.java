package com.dam.usuarios_notas.service;


import com.dam.usuarios_notas.model.Nota;
import com.dam.usuarios_notas.model.Usuario;
import com.dam.usuarios_notas.repository.NotaRepository;
import com.dam.usuarios_notas.repository.UsuarioRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NotaService {

    private final NotaRepository notaRepository;
    private final UsuarioRepository usuarioRepository;

    public NotaService(NotaRepository notaRepository, UsuarioRepository usuarioRepository) {
        this.notaRepository = notaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Nota> getAllNotas() {
        return notaRepository.findAll();
    }

    public Optional<Nota> getNotaById(Long id) {
        return notaRepository.findById(id);
    }

    public List<Nota> getNotasByUsuarioId(Long usuarioId, String order) {
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario con ID " + usuarioId + " no encontrado.");
        }
        Sort sort = Sort.by(order.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, "fechaCreacion");
        return notaRepository.findByUsuarioId(usuarioId, sort);
    }

    public Nota createNota(Nota nota, Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario con ID " + usuarioId + " no encontrado."));

        nota.setUsuario(usuario);
        nota.setFechaCreacion(LocalDateTime.now());
        return notaRepository.save(nota);
    }

    public Nota updateNota(Long id, Nota notaDetails) {
        Nota existingNota = notaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nota con ID " + id + " no encontrada."));

        existingNota.setTitulo(notaDetails.getTitulo());
        existingNota.setContenido(notaDetails.getContenido());

        return notaRepository.save(existingNota);
    }

    public void deleteNota(Long id) {
        if (!notaRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nota con ID " + id + " no encontrada.");
        }
        notaRepository.deleteById(id);
    }
}