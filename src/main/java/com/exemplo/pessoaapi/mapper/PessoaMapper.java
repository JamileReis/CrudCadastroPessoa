package com.exemplo.pessoaapi.mapper;

import com.exemplo.pessoaapi.dto.EnderecoDTO;
import com.exemplo.pessoaapi.dto.PessoaDTO;
import com.exemplo.pessoaapi.model.Endereco;
import com.exemplo.pessoaapi.model.Pessoa;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PessoaMapper {

    public Pessoa toEntity(PessoaDTO dto) {
        if (dto == null) return null;
        Pessoa pessoa = Pessoa.builder()
                .id(dto.getId())
                .nome(dto.getNome())
                .cpf(dto.getCpf())
                .email(dto.getEmail())
                .dataNascimento(dto.getDataNascimento())
                .telefones(dto.getTelefones())
                .enderecos(new ArrayList<>())
                .build();
        
        if (dto.getEnderecos() != null) {
            pessoa.setEnderecos(dto.getEnderecos().stream()
                    .map(e -> toEnderecoEntity(e, pessoa))
                    .collect(Collectors.toList()));
        }
        return pessoa;
    }

    public PessoaDTO toDto(Pessoa entity) {
        if (entity == null) return null;
        return PessoaDTO.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .cpf(entity.getCpf())
                .email(entity.getEmail())
                .dataNascimento(entity.getDataNascimento())
                .telefones(entity.getTelefones())
                .enderecos(entity.getEnderecos() != null ? 
                        entity.getEnderecos().stream()
                                .map(this::toEnderecoDto)
                                .collect(Collectors.toList()) : null)
                .build();
    }

    public Endereco toEnderecoEntity(EnderecoDTO dto, Pessoa pessoa) {
        if (dto == null) return null;
        return Endereco.builder()
                .id(dto.getId())
                .logradouro(dto.getLogradouro())
                .numero(dto.getNumero())
                .complemento(dto.getComplemento())
                .bairro(dto.getBairro())
                .cidade(dto.getCidade())
                .estado(dto.getEstado())
                .cep(dto.getCep())
                .principal(dto.isPrincipal())
                .pessoa(pessoa)
                .build();
    }

    public EnderecoDTO toEnderecoDto(Endereco entity) {
        if (entity == null) return null;
        return EnderecoDTO.builder()
                .id(entity.getId())
                .logradouro(entity.getLogradouro())
                .numero(entity.getNumero())
                .complemento(entity.getComplemento())
                .bairro(entity.getBairro())
                .cidade(entity.getCidade())
                .estado(entity.getEstado())
                .cep(entity.getCep())
                .principal(entity.isPrincipal())
                .build();
    }
}
