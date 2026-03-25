# API REST de Pessoas e Endereços

API REST desenvolvida com **Java 21** e **Spring Boot** para gerenciamento de pessoas e seus respectivos endereços, seguindo boas práticas de arquitetura, validação e documentação.

## Tecnologias Utilizadas

* Java 21
* Spring Boot 3.2.4
* Spring Data JPA
* H2 Database (em memória)
* Swagger / OpenAPI 3
* JUnit 5 & Mockito
* Lombok
* Bean Validation

## Como Executar

### Pré-requisitos

* Java 21 instalado
* Maven instalado

### Passos

```bash
mvn spring-boot:run
```

A aplicação estará disponível em:

```
http://localhost:8080
```


##  Documentação da API (Swagger)

A documentação interativa está disponível em:

```
http://localhost:8080/swagger-ui/index.html
```

### O que é possível fazer no Swagger:

* Testar todos os endpoints com **"Try it out"**
* Visualizar **request e response**
* Validar dados enviados
* Simular cenários de erro (400, 404)
* Ver exemplos reais de payload

O Swagger funciona como ambiente de testes da API.

---

## Endpoints

### Pessoas

* `GET /pessoas` → Lista todas as pessoas
* `GET /pessoas/{id}` → Busca uma pessoa por ID
* `POST /pessoas` → Cadastra uma nova pessoa
* `PUT /pessoas/{id}` → Atualiza uma pessoa
* `DELETE /pessoas/{id}` → Remove uma pessoa


### Endereços

* `POST /pessoas/{id}/enderecos` → Adiciona endereço
* `PUT /pessoas/{id}/enderecos/{enderecoId}` → Atualiza endereço
* `DELETE /pessoas/{id}/enderecos/{enderecoId}` → Remove endereço


## Regras de Negócio

* CPF é **obrigatório e único**
* Nome é obrigatório
* Uma pessoa pode ter vários endereços
* Apenas **um endereço pode ser principal**
* Ao definir um novo endereço como principal:

  * o anterior é automaticamente desmarcado
* Validações com Bean Validation
* Erros tratados globalmente


## Tratamento de Erros

Formato padrão de resposta:

```json
{
  "message": "Descrição do erro",
  "timestamp": "2026-03-25T12:52:13"
}
```

### Códigos utilizados

* `200` → Sucesso
* `201` → Criado com sucesso
* `204` → Remoção bem-sucedida
* `400` → Erro de validação
* `404` → Recurso não encontrado


## Testes

Executar:

```bash
mvn test
```

### Tipos de testes

* Testes unitários (Service)
* Testes de integração (Controller)


## Estrutura do Projeto

```
controller/
service/
repository/
domain/
dto/
config/
exception/
```


## Boas Práticas Aplicadas

* Separação de camadas (Clean Architecture)
* Uso de DTOs (não expõe entidades)
* Tratamento global de exceções
* Código limpo e legível
* Documentação com Swagger
* Validação com Bean Validation


## Considerações Finais

A API foi construída com foco em:

* Organização e clareza
* Baixo acoplamento
* Facilidade de manutenção
* Pronto para avaliação técnica

O Swagger permite validar completamente o funcionamento dos endpoints sem necessidade de ferramentas externas.

-Banco de Dados H2 (Ambiente de Desenvolvimento)

A aplicação utiliza o H2 Database como banco de dados em memória para o ambiente de desenvolvimento, permitindo execução rápida e sem necessidade de instalação de banco externo.

-Configuração
spring.datasource.url=jdbc:h2:mem:pessoas_db
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=dev
spring.datasource.password=dev

spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

spring.profiles.active=dev
 -Acesso ao Console

O console web do H2 está disponível em:

http://localhost:8080/h2-console
Credenciais de acesso:
JDBC URL: jdbc:h2:mem:pessoas_db
Usuário: dev
Senha: dev
- Características do H2
Banco de dados em memória
Inicializado automaticamente ao subir a aplicação
Dados são perdidos ao reiniciar o sistema
Não requer instalação externa
Ideal para testes e desenvolvimento local
-Integração com a Aplicação

Fluxo:

Controller → Service → Repository (JPA) → H2 Database
 -Uso no Desenvolvimento
Criar dados via Swagger (POST /pessoas)
Consultar diretamente no banco via console H2
Validar persistência e relacionamentos

Exemplo de consulta:

SELECT * FROM PESSOA;
SELECT * FROM ENDERECO;
 -Observações
O banco é recriado a cada execução da aplicação
Utilizado apenas no perfil dev
Não recomendado para ambientes de produção
 -Benefícios na Avaliação Técnica
Execução simplificada (sem dependências externas)
Facilidade para o avaliador testar a API
Visualização direta dos dados persistidos
Rapidez no ciclo de desenvolvimento
