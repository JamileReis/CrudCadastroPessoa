package com.exemplo.pessoaapi.service;

import com.exemplo.pessoaapi.dto.PessoaDTO;
import com.exemplo.pessoaapi.exception.BusinessException;
import com.exemplo.pessoaapi.mapper.PessoaMapper;
import com.exemplo.pessoaapi.model.Pessoa;
import com.exemplo.pessoaapi.repository.EnderecoRepository;
import com.exemplo.pessoaapi.repository.PessoaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PessoaServiceTest {

    @Mock
    private PessoaRepository pessoaRepository;
    @Mock
    private EnderecoRepository enderecoRepository;
    @Mock
    private PessoaMapper pessoaMapper;

    @InjectMocks
    private PessoaService pessoaService;

    private PessoaDTO pessoaDto;
    private Pessoa pessoa;

    @BeforeEach
    void setUp() {
        pessoaDto = PessoaDTO.builder()
                .nome("João Silva")
                .cpf("12345678901")
                .build();
        
        pessoa = Pessoa.builder()
                .id(1L)
                .nome("João Silva")
                .cpf("12345678901")
                .build();
    }

    @Test
    void deveSalvarPessoaComSucesso() {
        when(pessoaRepository.findByCpf(anyString())).thenReturn(Optional.empty());
        when(pessoaMapper.toEntity(any())).thenReturn(pessoa);
        when(pessoaRepository.save(any())).thenReturn(pessoa);
        when(pessoaMapper.toDto(any())).thenReturn(pessoaDto);

        PessoaDTO resultado = pessoaService.salvar(pessoaDto);

        assertNotNull(resultado);
        assertEquals(pessoaDto.getNome(), resultado.getNome());
        verify(pessoaRepository, times(1)).save(any());
    }

    @Test
    void deveLancarExcecaoAoSalvarPessoaComCpfExistente() {
        when(pessoaRepository.findByCpf(anyString())).thenReturn(Optional.of(pessoa));

        assertThrows(BusinessException.class, () -> pessoaService.salvar(pessoaDto));
        verify(pessoaRepository, never()).save(any());
    }
}
