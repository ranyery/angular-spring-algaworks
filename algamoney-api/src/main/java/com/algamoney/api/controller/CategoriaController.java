package com.algamoney.api.controller;

import com.algamoney.api.event.RecursoCriadoEvent;
import com.algamoney.api.model.Categoria;
import com.algamoney.api.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ApplicationEventPublisher publisher;

    @GetMapping
    public ResponseEntity<List<Categoria>> listar() {
        List<Categoria> categorias = categoriaRepository.findAll();

        return categorias.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(categorias);
    }

    @PostMapping
    public ResponseEntity<Categoria> criar(
            @Valid @RequestBody Categoria categoria,
            HttpServletResponse response
    ) {
        Categoria categoriaSalva = categoriaRepository.save(categoria);
        publisher.publishEvent(new RecursoCriadoEvent(this, response, categoriaSalva.getCodigo()));
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaSalva);
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Categoria> buscarPeloCodigo(@PathVariable Long codigo) {
        if (!categoriaRepository.existsById(codigo)) {
            return ResponseEntity.notFound().build();
        }

        Categoria categoria = categoriaRepository.findById(codigo).get();
        return ResponseEntity.ok(categoria);
    }

}
