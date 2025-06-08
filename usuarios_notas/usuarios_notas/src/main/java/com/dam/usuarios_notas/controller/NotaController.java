package com.dam.usuarios_notas.controller;
import com.dam.usuarios_notas.model.Nota;
import com.dam.usuarios_notas.service.NotaService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notas")
public class NotaController {

    private final NotaService notaService;

    public NotaController(NotaService notaService) {
    this.notaService = notaService;
    }

    @GetMapping
    public ResponseEntity<List<Nota>> getAllNotas(
            @RequestParam(required = false) @Positive(message = "El ID de usuario debe ser positivo") Long usuarioId,
            @RequestParam(defaultValue = "asc") String order){
        if (usuarioId != null) {
            return ResponseEntity.ok(notaService.getNotasByUsuarioId(usuarioId, order));
        }
        return ResponseEntity.ok(notaService.getAllNotas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Nota> getNotaById(@PathVariable Long id) {
        return notaService.getNotaById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nota con ID " + id + " no encontrada."));
    }

    @PostMapping
    public ResponseEntity<Nota> createNota(@Valid @RequestBody Nota nota,@RequestParam @Positive(message = "El ID de usuario debe ser positivo para crear la nota") Long usuarioId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(notaService.createNota(nota, usuarioId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Nota> updateNota(@PathVariable Long id, @Valid @RequestBody Nota nota) {
        if (nota.getId() != null && !nota.getId().equals(id)) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El ID en la ruta no coincide con el ID en el cuerpo de la solicitud.");
        }
        nota.setId(id);
        return ResponseEntity.ok(notaService.updateNota(id, nota));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNota(@PathVariable Long id) {
        notaService.deleteNota(id);
        return ResponseEntity.noContent().build();
    }
}