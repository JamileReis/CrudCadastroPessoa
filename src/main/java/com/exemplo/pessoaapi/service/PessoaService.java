package com.exemplo.pessoaapi.service;

import com.exemplo.pessoaapi.dto.EnderecoDTO;
import com.exemplo.pessoaapi.dto.PessoaDTO;
import com.exemplo.pessoaapi.exception.BusinessException;
import com.exemplo.pessoaapi.exception.ResourceNotFoundException;
import com.exemplo.pessoaapi.mapper.PessoaMapper;
import com.exemplo.pessoaapi.model.Endereco;
import com.exemplo.pessoaapi.model.Pessoa;
import com.exemplo.pessoaapi.repository.EnderecoRepository;
import com.exemplo.pessoaapi.repository.PessoaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PessoaService {

    private final PessoaRepository pessoaRepository;
    private final EnderecoRepository enderecoRepository;
    private final PessoaMapper pessoaMapper;

    @Transactional(readOnly = true)
    public List<PessoaDTO> listarTodos() {
        return pessoaRepository.findAll().stream()
                .map(pessoaMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PessoaDTO buscarPorId(Long id) {
        Pessoa pessoa = pessoaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pessoa não encontrada com ID: " + id));
        return pessoaMapper.toDto(pessoa);
    }

    @Transactional
    public PessoaDTO salvar(PessoaDTO dto) {
        if (pessoaRepository.findByCpf(dto.getCpf()).isPresent()) {
            throw new BusinessException("Já existe uma pessoa cadastrada com este CPF");
        }
        Pessoa pessoa = pessoaMapper.toEntity(dto);
        
        // Garantir consistência da relação bidirecional e regra de endereço principal
        if (pessoa.getEnderecos() != null) {
            validarEnderecoPrincipal(pessoa.getEnderecos());
            pessoa.getEnderecos().forEach(e -> e.setPessoa(pessoa));
        }
        
        return pessoaMapper.toDto(pessoaRepository.save(pessoa));
    }

    @Transactional
    public PessoaDTO atualizar(Long id, PessoaDTO dto) {
        Pessoa pessoaExistente = pessoaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pessoa não encontrada com ID: " + id));
        
        if (!pessoaExistente.getCpf().equals(dto.getCpf()) && 
            pessoaRepository.findByCpf(dto.getCpf()).isPresent()) {
            throw new BusinessException("Já existe outra pessoa cadastrada com este CPF");
        }

        pessoaExistente.setNome(dto.getNome());
        pessoaExistente.setCpf(dto.getCpf());
        pessoaExistente.setEmail(dto.getEmail());
        pessoaExistente.setDataNascimento(dto.getDataNascimento());
        pessoaExistente.setTelefones(dto.getTelefones());

        return pessoaMapper.toDto(pessoaRepository.save(pessoaExistente));
    }

    @Transactional
    public void deletar(Long id) {
        if (!pessoaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Pessoa não encontrada com ID: " + id);
        }
        pessoaRepository.deleteById(id);
    }

    @Transactional
    public EnderecoDTO adicionarEndereco(Long pessoaId, EnderecoDTO enderecoDto) {
        Pessoa pessoa = pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new ResourceNotFoundException("Pessoa não encontrada com ID: " + pessoaId));
        
        Endereco novoEndereco = pessoaMapper.toEnderecoEntity(enderecoDto, pessoa);
        
        if (novoEndereco.isPrincipal()) {
            desmarcarEnderecosPrincipais(pessoa);
        }
        
        pessoa.getEnderecos().add(novoEndereco);
        pessoaRepository.save(pessoa);
        
        return pessoaMapper.toEnderecoDto(novoEndereco);
    }

    @Transactional
    public EnderecoDTO atualizarEndereco(Long pessoaId, Long enderecoId, EnderecoDTO enderecoDto) {
        Pessoa pessoa = pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new ResourceNotFoundException("Pessoa não encontrada com ID: " + pessoaId));
        
        Endereco enderecoExistente = pessoa.getEnderecos().stream()
                .filter(e -> e.getId().equals(enderecoId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Endereço não encontrado com ID: " + enderecoId));

        if (enderecoDto.isPrincipal() && !enderecoExistente.isPrincipal()) {
            desmarcarEnderecosPrincipais(pessoa);
        }

        enderecoExistente.setLogradouro(enderecoDto.getLogradouro());
        enderecoExistente.setNumero(enderecoDto.getNumero());
        enderecoExistente.setComplemento(enderecoDto.getComplemento());
        enderecoExistente.setBairro(enderecoDto.getBairro());
        enderecoExistente.setCidade(enderecoDto.getCidade());
        enderecoExistente.setEstado(enderecoDto.getEstado());
        enderecoExistente.setCep(enderecoDto.getCep());
        enderecoExistente.setPrincipal(enderecoDto.isPrincipal());

        pessoaRepository.save(pessoa);
        return pessoaMapper.toEnderecoDto(enderecoExistente);
    }

    @Transactional
    public void deletarEndereco(Long pessoaId, Long enderecoId) {
        Pessoa pessoa = pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new ResourceNotFoundException("Pessoa não encontrada com ID: " + pessoaId));
        
        Endereco endereco = pessoa.getEnderecos().stream()
                .filter(e -> e.getId().equals(enderecoId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Endereço não encontrado com ID: " + enderecoId));

        pessoa.getEnderecos().remove(endereco);
        pessoaRepository.save(pessoa);
    }

    private void desmarcarEnderecosPrincipais(Pessoa pessoa) {
        pessoa.getEnderecos().forEach(e -> e.setPrincipal(false));
    }

    private void validarEnderecoPrincipal(List<Endereco> enderecos) {
        long principais = enderecos.stream().filter(Endereco::isPrincipal).count();
        if (principais > 1) {
            throw new BusinessException("Apenas um endereço pode ser marcado como principal");
        }
    }
}
