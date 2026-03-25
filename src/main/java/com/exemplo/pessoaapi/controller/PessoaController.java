package com.exemplo.pessoaapi.controller;

import com.exemplo.pessoaapi.dto.EnderecoDTO;
import com.exemplo.pessoaapi.dto.PessoaDTO;
import com.exemplo.pessoaapi.service.PessoaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pessoas")
@RequiredArgsConstructor
@Tag(name = "Pessoas", description = "Gerenciamento de pessoas e seus endereços")
public class PessoaController {

    private final PessoaService pessoaService;

    @GetMapping
    @Operation(summary = "Listar todas as pessoas")
    public ResponseEntity<List<PessoaDTO>> listar() {
        return ResponseEntity.ok(pessoaService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar pessoa por ID")
    public ResponseEntity<PessoaDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pessoaService.buscarPorId(id));
    }

    @PostMapping
    @Operation(summary = "Cadastrar uma nova pessoa")
    public ResponseEntity<PessoaDTO> criar(@Valid @RequestBody PessoaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pessoaService.salvar(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar dados de uma pessoa")
    public ResponseEntity<PessoaDTO> atualizar(@PathVariable Long id, @Valid @RequestBody PessoaDTO dto) {
        return ResponseEntity.ok(pessoaService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover uma pessoa")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        pessoaService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/enderecos")
    @Operation(summary = "Adicionar um novo endereço para uma pessoa")
    public ResponseEntity<EnderecoDTO> adicionarEndereco(@PathVariable Long id, @Valid @RequestBody EnderecoDTO enderecoDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pessoaService.adicionarEndereco(id, enderecoDto));
    }

    @PutMapping("/{id}/enderecos/{enderecoId}")
    @Operation(summary = "Atualizar um endereço de uma pessoa")
    public ResponseEntity<EnderecoDTO> atualizarEndereco(
            @PathVariable Long id, 
            @PathVariable Long enderecoId, 
            @Valid @RequestBody EnderecoDTO enderecoDto) {
        return ResponseEntity.ok(pessoaService.atualizarEndereco(id, enderecoId, enderecoDto));
    }

    @DeleteMapping("/{id}/enderecos/{enderecoId}")
    @Operation(summary = "Remover um endereço de uma pessoa")
    public ResponseEntity<Void> deletarEndereco(@PathVariable Long id, @PathVariable Long enderecoId) {
        pessoaService.deletarEndereco(id, enderecoId);
        return ResponseEntity.noContent().build();
    }
}
