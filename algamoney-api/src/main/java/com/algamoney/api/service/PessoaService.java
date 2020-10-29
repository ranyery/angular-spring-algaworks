package com.algamoney.api.service;

import com.algamoney.api.event.RecursoCriadoEvent;
import com.algamoney.api.model.Pessoa;
import com.algamoney.api.repository.PessoaRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
public class PessoaService {

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private ApplicationEventPublisher publisher;

    public Pessoa atualizar(Long codigo, Pessoa pessoa) {
        Pessoa pessoaSalva = buscarPessoaPeloCodigo(codigo);
        BeanUtils.copyProperties(pessoa, pessoaSalva, "codigo");

        return pessoaRepository.save(pessoaSalva);
    }

    public void atualizarPropriedadeAtivo(Long codigo, Boolean ativo) {
        Pessoa pessoaSalva = buscarPessoaPeloCodigo(codigo);
        pessoaSalva.setAtivo(ativo);
        pessoaRepository.save(pessoaSalva);
    }

    public ResponseEntity<List<Pessoa>> listarTodos() {
        List<Pessoa> pessoas = pessoaRepository.findAll();

        return pessoas.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(pessoas);
    }

    public void removerPessoaPeloCodigo(Long codigo) {
        Pessoa pessoa = buscarPessoaPeloCodigo(codigo);
        pessoaRepository.delete(pessoa);
    }

    public Pessoa buscarPessoaPeloCodigo(Long codigo) {
        if (!pessoaRepository.existsById(codigo)) {
            throw new EmptyResultDataAccessException(1);
        }

        Pessoa pessoaSalva = pessoaRepository.findById(codigo).get();
        return pessoaSalva;
    }

    public Pessoa criarPessoa(Pessoa pessoa, HttpServletResponse response) {
        Pessoa pessoaSalva = pessoaRepository.save(pessoa);
        publisher.publishEvent(
                new RecursoCriadoEvent(this, response, pessoaSalva.getCodigo())
        );
        return pessoaSalva;
    }
}
