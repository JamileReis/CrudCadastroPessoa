package com.exemplo.pessoaapi.controller;

import com.exemplo.pessoaapi.dto.PessoaDTO;
import com.exemplo.pessoaapi.service.PessoaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(PessoaController.class)
public class PessoaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PessoaService pessoaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveCriarPessoaComSucesso() throws Exception {
        PessoaDTO pessoaDto = PessoaDTO.builder()
                .nome("João Silva")
                .cpf("12345678901")
                .build();

        when(pessoaService.salvar(any())).thenReturn(pessoaDto);

        mockMvc.perform(post("/pessoas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pessoaDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("João Silva"))
                .andExpect(jsonPath("$.cpf").value("12345678901"));
    }

    @Test
    void deveRetornarBadRequestAoCriarPessoaComDadosInvalidos() throws Exception {
        PessoaDTO pessoaInvalida = PessoaDTO.builder()
                .nome("") // Nome vazio
                .cpf("123") // CPF inválido
                .build();

        mockMvc.perform(post("/pessoas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pessoaInvalida)))
                .andExpect(status().isBadRequest());
    }
}
