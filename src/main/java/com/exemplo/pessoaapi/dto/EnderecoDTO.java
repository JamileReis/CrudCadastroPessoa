package com.exemplo.pessoaapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnderecoDTO {
    private Long id;
    @NotBlank(message = "Logradouro é obrigatório")
    private String logradouro;
    @NotBlank(message = "Número é obrigatório")
    private String numero;
    private String complemento;
    @NotBlank(message = "Bairro é obrigatório")
    private String bairro;
    @NotBlank(message = "Cidade é obrigatório")
    private String cidade;
    @NotBlank(message = "Estado é obrigatório")
    private String estado;
    @NotBlank(message = "CEP é obrigatório")
    private String cep;
    private boolean principal;
}
