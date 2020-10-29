package com.algamoney.api.controller;

import com.algamoney.api.model.Pessoa;
import com.algamoney.api.service.PessoaService;
import org.springframework.beans.factory.annotation.Autowired;
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
	private PessoaService pessoaService;

	@GetMapping
	public ResponseEntity<List<Pessoa>> buscarTodos() {
		return pessoaService.listarTodos();
	}
	
	@GetMapping("/{codigo}")
	public ResponseEntity<Pessoa> buscarUmaPessoa(@PathVariable Long codigo) {
		Pessoa pessoaSalva = pessoaService.buscarPessoaPeloCodigo(codigo);
		return ResponseEntity.ok(pessoaSalva);
	}
	
	@PostMapping
	public ResponseEntity<Pessoa> criarPessoa(
			@Valid @RequestBody Pessoa pessoa, 
			HttpServletResponse response
	) {
		Pessoa pessoaSalva = pessoaService.criarPessoa(pessoa, response);
		return ResponseEntity.status(HttpStatus.CREATED).body(pessoaSalva);
	}

	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long codigo) {
		pessoaService.removerPessoaPeloCodigo(codigo);
	}

	@PutMapping("/{codigo}")
	public ResponseEntity<Pessoa> atualizar(
			@PathVariable Long codigo,
			@Valid @RequestBody Pessoa pessoa
	) {
		Pessoa pessoaSalva = pessoaService.atualizar(codigo, pessoa);
		return ResponseEntity.ok(pessoaSalva);
	}

	@PutMapping("/{codigo}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void atualizarProriedadeAtivo(
			@PathVariable Long codigo,
			@RequestBody Boolean ativo
	) {
		pessoaService.atualizarPropriedadeAtivo(codigo, ativo);
	}

}
