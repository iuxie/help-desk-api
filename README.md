# Help Desk API

API REST para gerenciamento de chamados de suporte técnico, desenvolvida como projeto de portfólio com Java e Spring Boot.

O sistema permite cadastrar usuários e categorias, abrir chamados, atribuir técnicos, controlar o fluxo de status, registrar comentários e concluir atendimentos. As regras de negócio são tratadas na camada de serviço e os erros são retornados no formato `ProblemDetail`.

## Funcionalidades

- Cadastro, consulta, atualização e ativação/inativação de usuários.
- Perfis de usuário: `SOLICITANTE`, `TECNICO` e `ADMIN`.
- Cadastro, consulta, atualização e ativação/inativação de categorias.
- Abertura e atualização de chamados.
- Geração automática de código único para cada chamado.
- Cálculo do prazo de SLA de acordo com a prioridade.
- Atribuição de chamados a técnicos ativos.
- Controle das transições de status dos chamados.
- Resolução de chamados com registro da solução e da data de conclusão.
- Criação, consulta e atualização de comentários.
- Suporte a comentários internos para técnicos e administradores.
- Validação dos dados recebidos com Jakarta Validation.
- Tratamento centralizado de erros com respostas no padrão `ProblemDetail`.
- Versionamento do banco de dados com Flyway.
- Documentação interativa com Swagger UI e OpenAPI 3.

## Regras principais

### Prioridades e SLA

| Prioridade | Prazo |
|---|---:|
| `BAIXA` | 72 horas |
| `MEDIA` | 48 horas |
| `ALTA` | 24 horas |
| `CRITICA` | 4 horas |

### Status dos chamados

Os chamados podem assumir os seguintes status:

- `ABERTO`
- `EM_ATENDIMENTO`
- `AGUARDANDO_SOLICITANTE`
- `RESOLVIDO`
- `CANCELADO`

Entre as regras aplicadas estão:

- um chamado é criado com o status `ABERTO`;
- atribuir um técnico altera o status para `EM_ATENDIMENTO`;
- apenas usuários ativos com o perfil `TECNICO` podem receber chamados;
- a resolução exige um técnico atribuído e uma solução informada;
- chamados resolvidos ou cancelados não podem receber novos comentários;
- comentários internos só podem ser criados por técnicos ou administradores;
- categorias e usuários inativos não podem ser utilizados na abertura de chamados.

## Tecnologias

- Java 21
- Spring Boot 4
- Spring Web MVC
- Spring Data JPA
- Jakarta Validation
- PostgreSQL 17
- Flyway
- MapStruct
- Lombok
- Spring Security Crypto
- Springdoc OpenAPI
- Maven
- Docker Compose

> O projeto utiliza somente o módulo `spring-security-crypto` para armazenar senhas de forma segura. Não há autenticação ou autorização com Spring Security.

## Estrutura do projeto

O código está organizado por domínio:

```text
src/main/java/dev/iuredev/HelpDeskAPI
├── categories
├── comments
├── config
├── enums
├── exceptions
├── tickets
└── users
```

Cada domínio possui seus próprios controllers, DTOs, mappers, models, repositories e services.

As migrations estão localizadas em:

```text
src/main/resources/db/migration
```

## Pré-requisitos

Para executar o projeto, é necessário ter instalado:

- Java 21 ou superior;
- Docker com suporte ao Docker Compose.

O Maven Wrapper está incluído no repositório, portanto não é necessário instalar o Maven separadamente.

## Configuração do ambiente

Crie o arquivo `.env` a partir do exemplo disponível no repositório.

No Windows PowerShell:

```powershell
Copy-Item .env.example .env
```

No Linux ou macOS:

```bash
cp .env.example .env
```

## Executando o projeto

Inicie o PostgreSQL:

```bash
docker compose up -d
```

Confirme se o contêiner está saudável:

```bash
docker compose ps
```

No Windows PowerShell, carregue as variáveis e execute a aplicação:

```powershell
Get-Content .env | ForEach-Object {
    if ($_ -match '^[^#].*=') {
        $name, $value = $_ -split '=', 2
        Set-Item -Path "Env:$name" -Value $value
    }
}

.\mvnw.cmd spring-boot:run
```

No Linux ou macOS:

```bash
set -a
source .env
set +a
./mvnw spring-boot:run
```

Quando a inicialização for concluída, a API estará disponível em:

```text
http://localhost:8080
```

### Documentação da API

Com a aplicação em execução, a documentação pode ser acessada nos seguintes endereços:

| Recurso | URL |
|---|---|
| Swagger UI | `http://localhost:8080/swagger-ui.html` |
| OpenAPI JSON | `http://localhost:8080/v3/api-docs` |
| OpenAPI YAML | `http://localhost:8080/v3/api-docs.yaml` |

O Swagger UI permite consultar os contratos e executar requisições diretamente pelo navegador.

Para encerrar o PostgreSQL preservando os dados:

```bash
docker compose down
```

> O comando `docker compose down -v` também remove o volume e apaga os dados do banco.

## Endpoints

### Usuários

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/api/users` | Lista todos os usuários |
| `GET` | `/api/users/{id}` | Busca um usuário por ID |
| `POST` | `/api/users` | Cria um usuário |
| `PUT` | `/api/users/{id}` | Atualiza um usuário |
| `PATCH` | `/api/users/{id}/status` | Ativa ou inativa um usuário |

### Categorias

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/api/categories` | Lista todas as categorias |
| `GET` | `/api/categories/active` | Lista as categorias ativas |
| `GET` | `/api/categories/{id}` | Busca uma categoria por ID |
| `POST` | `/api/categories` | Cria uma categoria |
| `PUT` | `/api/categories/{id}` | Atualiza uma categoria |
| `PATCH` | `/api/categories/{id}/status` | Ativa ou inativa uma categoria |

### Chamados

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/api/tickets` | Lista todos os chamados |
| `GET` | `/api/tickets/{id}` | Busca um chamado por ID |
| `GET` | `/api/tickets/code/{code}` | Busca um chamado pelo código |
| `POST` | `/api/tickets` | Abre um chamado |
| `PUT` | `/api/tickets/{id}` | Atualiza os dados de um chamado |
| `PATCH` | `/api/tickets/{id}/assignment` | Atribui um técnico ao chamado |
| `PATCH` | `/api/tickets/{id}/resolution` | Resolve um chamado |
| `PATCH` | `/api/tickets/{id}/status` | Altera o status do chamado |

### Comentários

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/api/comments/ticket/{ticketId}` | Lista os comentários públicos de um chamado |
| `GET` | `/api/comments/{id}` | Busca um comentário por ID |
| `POST` | `/api/comments` | Adiciona um comentário |
| `PUT` | `/api/comments/{id}` | Atualiza um comentário |

## Exemplos de requisições

### Criar usuário

```http
POST /api/users
Content-Type: application/json
```

```json
{
  "name": "Usuário Solicitante",
  "email": "solicitante@email.com",
  "password": "senha123",
  "role": "SOLICITANTE"
}
```

### Criar categoria

```http
POST /api/categories
Content-Type: application/json
```

```json
{
  "name": "Hardware",
  "description": "Problemas relacionados aos equipamentos"
}
```

### Abrir chamado

```http
POST /api/tickets
Content-Type: application/json
```

```json
{
  "title": "Computador não liga",
  "description": "O equipamento não apresenta nenhum sinal.",
  "priority": "ALTA",
  "requesterId": 1,
  "categoryId": 1
}
```

### Adicionar comentário

```http
POST /api/comments
Content-Type: application/json
```

```json
{
  "message": "O equipamento será analisado.",
  "internal": false,
  "authorId": 2,
  "ticketId": 1
}
```

## Respostas de erro

Erros de recurso não encontrado, violações de regras de negócio e falhas de validação são tratados globalmente e retornados no formato `ProblemDetail`.

Exemplo de erro de validação:

```json
{
  "type": "about:blank",
  "title": "Dados inválidos.",
  "status": 400,
  "detail": "Um ou mais campos enviados são inválidos.",
  "timestamp": "2026-08-28T00:00:00Z",
  "errors": [
    {
      "field": "email",
      "message": "O e-mail informado é inválido."
    }
  ]
}
```

## Limitações do escopo

Este projeto foi desenvolvido para fins de estudo e portfólio. O escopo não inclui:

- autenticação e autorização;
- testes automatizados;
- paginação e filtros avançados;
- recursos necessários para uma implantação em produção.
