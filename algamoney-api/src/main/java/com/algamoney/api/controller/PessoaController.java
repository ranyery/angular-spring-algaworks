package com.algamoney.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algamoney.api.model.Categoria;
import com.algamoney.api.model.Pessoa;
import com.algamoney.api.repository.PessoaRepository;

@RestController
@RequestMapping("/pessoas")
public class PessoaController {

	@Autowired
	private PessoaRepository pessoaRepository;
	
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
	public ResponseEntity<Pessoa> criarPessoa(@Valid @RequestBody Pessoa pessoa) {
		pessoaRepository.save(pessoa);
		
		return ResponseEntity.created(null).body(pessoa);
	}
	
}
