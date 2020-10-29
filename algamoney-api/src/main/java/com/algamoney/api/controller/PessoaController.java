package com.algamoney.api.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algamoney.api.event.RecursoCriadoEvent;
import com.algamoney.api.model.Pessoa;
import com.algamoney.api.repository.PessoaRepository;

@RestController
@RequestMapping("/pessoas")
public class PessoaController {

	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@GetMapping
	public ResponseEntity<List<Pessoa>> listar() {
		List<Pessoa> pessoas = pessoaRepository.findAll();
		
		return pessoas.isEmpty()
				? ResponseEntity.noContent().build()
				: ResponseEntity.ok(pessoas);
	}
	
	@GetMapping("/{codigo}")
	public ResponseEntity<Pessoa> buscarUmaPessoa(@PathVariable Long codigo) {
		if (!pessoaRepository.existsById(codigo)) {
			return ResponseEntity.notFound().build();
		}
		
		Pessoa pessoa = pessoaRepository.findById(codigo).get();
		return ResponseEntity.ok(pessoa);
	}
	
	@PostMapping
	public ResponseEntity<Pessoa> criarPessoa(
			@Valid @RequestBody Pessoa pessoa, 
			HttpServletResponse response
	) {
		Pessoa pessoaSalva = pessoaRepository.save(pessoa);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, pessoaSalva.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(pessoaSalva);
	}
	
	@DeleteMapping("/{codigo}")
	public ResponseEntity<Pessoa> remover(@PathVariable Long codigo) {
		if (!pessoaRepository.existsById(codigo)) {
			return ResponseEntity.notFound().build();
		}
		
		pessoaRepository.deleteById(codigo);
		return ResponseEntity.noContent().build();
	}
	
}
