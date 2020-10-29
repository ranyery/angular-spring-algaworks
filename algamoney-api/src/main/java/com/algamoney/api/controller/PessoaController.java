package com.algamoney.api.controller;

import com.algamoney.api.Service.PessoaService;
import com.algamoney.api.event.RecursoCriadoEvent;
import com.algamoney.api.model.Pessoa;
import com.algamoney.api.repository.PessoaRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/pessoas")
public class PessoaController {

	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	private PessoaService pessoaService;
	
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
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long codigo) {
		pessoaRepository.deleteById(codigo);
	}

	@PutMapping("/{codigo}")
	public ResponseEntity<Pessoa> atualizar(
			@PathVariable Long codigo,
			@Valid @RequestBody Pessoa pessoa
	) {
		Pessoa pessoaSalva = pessoaService.atualizar(codigo, pessoa);
		return ResponseEntity.ok(pessoaSalva);
	}
	
}
