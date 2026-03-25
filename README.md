# API REST de Pessoas e Endereços

Esta é uma API REST desenvolvida com **Java 21** e **Spring Boot** para o gerenciamento de pessoas e seus respectivos endereços.

## Tecnologias Utilizadas

- **Java 21**
- **Spring Boot 3.2.4**
- **Spring Data JPA**
- **Banco de Dados H2** (Em memória)
- **Swagger/OpenAPI 3** (Documentação)
- **JUnit 5 & Mockito** (Testes)
- **Lombok** (Produtividade)
- **Bean Validation** (Validação de dados)

## Como Executar

1. Certifique-se de ter o Java 21 e o Maven instalados.
2. Clone o projeto ou extraia os arquivos.
3. No diretório raiz, execute:
   ```bash
   mvn spring-boot:run
   ```
4. A API estará disponível em `http://localhost:8080`.

## Documentação (Swagger)

Após iniciar a aplicação, a documentação interativa do Swagger pode ser acessada em:
`http://localhost:8080/swagger-ui/index.html`

## Endpoints Principais

### Pessoas
- `GET /pessoas`: Lista todas as pessoas.
- `GET /pessoas/{id}`: Busca uma pessoa por ID.
- `POST /pessoas`: Cadastra uma nova pessoa.
- `PUT /pessoas/{id}`: Atualiza os dados de uma pessoa.
- `DELETE /pessoas/{id}`: Remove uma pessoa.

### Endereços
- `POST /pessoas/{id}/enderecos`: Adiciona um endereço a uma pessoa.
- `PUT /pessoas/{id}/enderecos/{enderecoId}`: Atualiza um endereço.
- `DELETE /pessoas/{id}/enderecos/{enderecoId}`: Remove um endereço.

## Regras de Negócio Implementadas

- O CPF é obrigatório e deve ser único no sistema.
- Uma pessoa pode ter vários endereços, mas apenas um pode ser marcado como **principal**.
- Ao marcar um novo endereço como principal, o anterior deixa de ser automaticamente.
- Validações de campos obrigatórios e formatos (ex: CPF com 11 dígitos).
- Tratamento global de exceções com retornos amigáveis.

## Testes

Para rodar os testes unitários e de integração:
```bash
mvn test
```
# CrudCadastroPessoa
