package com.algamoney.api.Service;

import com.algamoney.api.model.Pessoa;
import com.algamoney.api.repository.PessoaRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class PessoaService {

    @Autowired
    private PessoaRepository pessoaRepository;

     public Pessoa atualizar(Long codigo, Pessoa pessoa) {
         if (!pessoaRepository.existsById(codigo)) {
             throw new EmptyResultDataAccessException(1);
         }

         Pessoa pessoaSalva = pessoaRepository.findById(codigo).get();
         BeanUtils.copyProperties(pessoa, pessoaSalva, "codigo");

         return pessoaRepository.save(pessoaSalva);
     }

}
