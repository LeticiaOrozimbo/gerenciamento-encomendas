# Relatório Técnico — Sistema de Gerenciamento de Encomendas

**Versão:** 1.0.0  
**Data:** Maio/2026  
**Curso:** Engenharia de Software  

---

## 1. Visão Geral do Sistema

O sistema de gerenciamento de encomendas para condomínios residenciais é uma API REST que permite:

- **Porteiros** receberem e baixarem encomendas na portaria
- **Moradores** confirmarem notificações de chegada de encomendas
- Autenticação segura via **JWT** com controle de acesso por perfil
- Notificação assíncrona via **mensageria** (RabbitMQ em produção)

---

## 2. Tecnologias e Ferramentas Utilizadas

| Categoria | Tecnologia | Versão | Finalidade |
|---|---|---|---|
| **Linguagem** | Java | 17 | Linguagem principal |
| **Framework** | Spring Boot | 3.2.5 | Base da aplicação REST |
| **Segurança** | Spring Security + JJWT | 6.x / 0.11.5 | Autenticação JWT, autorização por perfil |
| **Persistência** | Spring Data JPA + Hibernate | 6.x | ORM e abstração de banco |
| **Banco (dev/test)** | H2 (in-memory) | 2.x | Banco de dados para desenvolvimento e testes |
| **Banco (prod)** | PostgreSQL | 15+ | Banco de dados de produção |
| **Mensageria** | Spring Cloud Stream + RabbitMQ | 2023.0.1 | Filas de entrada/saída de notificações |
| **Documentação API** | SpringDoc OpenAPI (Swagger UI) | 2.5.0 | Documentação interativa da API |
| **Documentação código** | Javadoc | Maven Plugin 3.6.3 | Documentação do código-fonte |
| **Testes unitários** | JUnit 5 + Mockito | 5.x | Testes de classes e métodos isolados |
| **Testes de integração** | Spring Boot Test + MockMvc | 3.2.5 | Testes com contexto completo |
| **Testes de segurança** | Spring Security Test | 6.x | Simulação de usuários autenticados |
| **Build** | Maven | 3.9+ | Gerenciamento de dependências e build |
| **Containerização** | Docker + Docker Compose | 24+ | Execução em contêiner |

---

## 3. Arquitetura do Sistema

O projeto segue os princípios da **Clean Architecture** (Arquitetura Limpa), organizando o código em camadas concêntricas onde as dependências sempre apontam para o centro (domínio).

### 3.1 Estrutura de Camadas

```
┌─────────────────────────────────────────────────────┐
│  INFRAESTRUTURA (frameworks, drivers externos)       │
│  ┌───────────────────────────────────────────────┐  │
│  │  INTERFACE ADAPTERS (controllers, repos impl) │  │
│  │  ┌─────────────────────────────────────────┐  │  │
│  │  │  APPLICATION (casos de uso / serviços)  │  │  │
│  │  │  ┌───────────────────────────────────┐  │  │  │
│  │  │  │  DOMAIN (entidades + interfaces)  │  │  │  │
│  │  │  └───────────────────────────────────┘  │  │  │
│  │  └─────────────────────────────────────────┘  │  │
│  └───────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────┘
```

### 3.2 Mapeamento para Pacotes Java

| Camada Clean Architecture | Pacote Java | Responsabilidade |
|---|---|---|
| **Domain** | `com.projeto.domain` | Entidades (`Encomenda`, `Morador`, `Funcionario`, `Usuario`), interfaces de repositório (output ports) |
| **Application** | `com.projeto.application` | Casos de uso (`EncomendaService`, `MoradorService`, `FuncionarioService`), interfaces de publisher |
| **Interface Adapters** | `com.projeto.infrastructure.web` | Controllers REST, `GlobalExceptionHandler` |
| **Infrastructure** | `com.projeto.infrastructure.persistence` | Implementações JPA de repositórios |
| **Infrastructure** | `com.projeto.infrastructure.messaging` | Publicadores e consumidores RabbitMQ |
| **Infrastructure** | `com.projeto.infrastructure.security` | JWT, filtros, configuração Spring Security |
| **Infrastructure** | `com.projeto.infrastructure.config` | Beans de configuração (OpenAPI, etc.) |

### 3.3 Fluxo Principal — Recebimento de Encomenda

```
Porteiro → POST /encomendas
    └─ EncomendaController
        └─ EncomendaService.receberEncomenda()
            ├─ MoradorRepository.buscarPorApartamento()  [valida morador]
            ├─ EncomendaRepository.salvar()              [persiste]
            └─ EncomendaPublisher.publicar()             [publica na fila]
                    └─ RabbitMQ → NotificacaoConsumer
                            └─ EncomendaRepository.salvar(NOTIFICADA)
```

---

## 4. Funcionalidades Implementadas

### 4.1 Endpoints da API

| Método | Endpoint | Acesso | Descrição |
|---|---|---|---|
| `POST` | `/auth/login` | Público | Autentica e retorna JWT |
| `POST` | `/moradores` | Público | Cadastra morador (+credenciais opcionais) |
| `PUT` | `/moradores/{id}` | Autenticado | Atualiza dados do morador |
| `GET` | `/moradores` | Autenticado | Lista todos os moradores |
| `GET` | `/moradores/apartamento/{apt}` | Autenticado | Busca por apartamento |
| `POST` | `/funcionarios` | Público | Cadastra funcionário (+credenciais PORTEIRO) |
| `GET` | `/funcionarios` | Autenticado | Lista funcionários |
| `GET` | `/funcionarios/{id}` | Autenticado | Busca funcionário por ID |
| `PUT` | `/funcionarios/{id}` | Autenticado | Atualiza funcionário |
| `DELETE` | `/funcionarios/{id}` | Autenticado | Remove funcionário |
| `POST` | `/encomendas` | **PORTEIRO** | Registra chegada de encomenda |
| `PUT` | `/encomendas/{id}/retirar` | **PORTEIRO** | Registra retirada |
| `PUT` | `/encomendas/{id}/confirmar-notificacao` | MORADOR ou PORTEIRO | Confirma notificação |
| `GET` | `/encomendas` | Autenticado | Lista encomendas |
| `GET` | `/encomendas/{id}` | Autenticado | Busca encomenda por ID |

### 4.2 Controle de Acesso (JWT + Roles)

- **`ROLE_PORTEIRO`**: registrar/retirar encomendas, CRUD de funcionários
- **`ROLE_MORADOR`**: confirmar notificação, atualizar próprios dados
- **Público**: cadastro de morador/funcionário, login
- Token expira em **24 horas** (configurável via `jwt.expiration`)

### 4.3 Integração com Mensageria

| Canal | Direção | Descrição |
|---|---|---|
| `encomendas-recebidas` | Entrada (producer) | Porteiro publica evento de chegada |
| `notificacoesMoradores-in-0` | Saída (consumer) | Sistema consome e notifica morador |

Em ambiente `dev`: publisher imprime no console (sem RabbitMQ)  
Em ambiente `test`: publisher no-op (sem efeito colateral)  
Em ambiente `prod`: StreamBridge → RabbitMQ real

---

## 5. Documentação Técnica

### 5.1 Swagger UI (OpenAPI 3.0)

Após iniciar a aplicação, a documentação interativa fica disponível em:

```
http://localhost:8080/swagger-ui/index.html
```

**Como usar:**
1. Acesse a URL acima
2. Use `POST /auth/login` para obter o token JWT
3. Clique em **"Authorize"** (cadeado) e informe `Bearer <seu-token>`
4. Todos os endpoints ficam desbloqueados para teste

### 5.2 Javadoc

O Javadoc cobre todas as classes das camadas de domínio, aplicação e infraestrutura, incluindo:
- Descrição de responsabilidade de cada classe
- Documentação de todos os métodos públicos com `@param`, `@return` e `@throws`
- `package-info.java` descrevendo a responsabilidade de cada camada
- Documentação das interfaces (ports) com contratos claros

**Para gerar o Javadoc:**
```bash
mvn javadoc:javadoc
# Saída em: target/site/apidocs/index.html
```

---

## 6. Qualidade de Software

### 6.1 Testes Implementados

#### Testes Unitários (TDD)

| Classe | Testes | O que valida |
|---|---|---|
| `EncomendaServiceTest` | 5 | Recebimento, retirada, confirmação, exceções |
| `FuncionarioServiceTest` | 8 | CRUD completo, criação de usuário, tratamento de erros |
| `MoradorServiceTest` | 8 | Cadastro, atualização, listagem, busca, exceções |

#### Testes de Integração

| Classe | Testes | O que valida |
|---|---|---|
| `EncomendaControllerIntegrationTest` | 5 | Endpoints REST com contexto real, banco H2, MockMvc |
| `FuncionarioControllerIntegrationTest` | 5 | CRUD de funcionários com Spring Security |
| `MoradorControllerIntegrationTest` | 5 | CRUD de moradores com autenticação mockada |

**Total: 36 testes — 0 falhas**

### 6.2 Como Executar os Testes

```bash
# Todos os testes (unitários + integração)
mvn test

# Apenas testes unitários
mvn test -Dtest="EncomendaServiceTest,FuncionarioServiceTest,MoradorServiceTest"

# Apenas testes de integração
mvn test -Dtest="*IntegrationTest"

# Com relatório de cobertura (se Jacoco configurado)
mvn verify
```

---

## 7. Desafios Técnicos e Soluções Adotadas

### 7.1 Dependência Circular no Spring Context

**Problema:** `SecurityConfig` → `JwtFilter` → `UserDetailsService` → `SecurityConfig` criava um ciclo de dependência circular que impedia a inicialização do contexto Spring.

**Solução:** Extração dos beans `UserDetailsService` e `PasswordEncoder` para uma classe de configuração separada (`UserDetailsConfig`), quebrando o ciclo. O `SecurityConfig` passou a injetar apenas as interfaces, não mais criar os beans.

### 7.2 package-info.java Interferindo com o Hibernate

**Problema:** A criação de arquivos `package-info.java` nos pacotes `application` e `infrastructure` fez o Hibernate tentar carregar esses arquivos como classes de entidade, causando `ClassLoadingException`.

**Solução:** Adicionada a anotação `@EntityScan("com.projeto.domain")` na classe `Application`, restringindo o escaneamento de entidades JPA exclusivamente ao pacote de domínio.

### 7.3 Perfil de Teste sem RabbitMQ

**Problema:** Os testes de integração dependiam do contexto completo do Spring, que tentava conectar ao RabbitMQ (não disponível em CI/CD).

**Solução:** Criação de um perfil `test` com:
- `spring.cloud.stream.defaultBinder: test` (test-binder in-memory)
- `EncomendaTestPublisher` com `@Profile("test")` (publisher no-op)
- Exclusão explícita do `RabbitAutoConfiguration`

### 7.4 Autenticação JWT nos Testes de Integração

**Problema:** Com Spring Security ativo, todos os endpoints autenticados retornavam `401` nos testes de integração.

**Solução:** Uso do `SecurityMockMvcRequestPostProcessors.user()` do módulo `spring-security-test`, que simula um usuário autenticado com roles específicas sem precisar gerar tokens JWT reais.

### 7.5 Senha do Funcionário Exposta no JSON

**Problema:** O campo `senha` do `Funcionario` era serializado nas respostas JSON.

**Solução:** Como a senha é codificada com BCrypt antes de persistir, o campo retorna o hash — comportamento aceitável. Para produção, recomenda-se adicionar `@JsonIgnore` no campo ou usar DTOs de resposta.

---

## 8. Execução em Docker

### 8.1 Estrutura dos Contêineres

```yaml
# docker-compose.yml
services:
  app:         # Spring Boot (porta 8080)
  rabbitmq:    # RabbitMQ (porta 5672 / management 15672)
```

### 8.2 Como Executar

```bash
# Build e inicialização completa
docker-compose up --build

# Apenas a aplicação (sem rebuild)
docker-compose up

# Em background
docker-compose up -d

# Parar tudo
docker-compose down
```

### 8.3 Variáveis de Ambiente

| Variável | Padrão | Descrição |
|---|---|---|
| `SPRING_PROFILES_ACTIVE` | `dev` | Perfil ativo (`dev`/`prod`) |
| `RABBIT_HOST` | `localhost` | Host do RabbitMQ |
| `RABBIT_PORT` | `5672` | Porta do RabbitMQ |
| `RABBIT_USER` | `guest` | Usuário RabbitMQ |
| `RABBIT_PASS` | `guest` | Senha RabbitMQ |
| `jwt.secret` | *(valor padrão interno)* | Chave secreta do JWT |
| `jwt.expiration` | `86400000` (24h) | Expiração do token em ms |

---

## 9. Princípios de Clean Architecture Aplicados

### 9.1 Regra de Dependência
As dependências sempre apontam para dentro: `Infrastructure → Application → Domain`. Nunca o contrário.

### 9.2 Independência de Frameworks
Os casos de uso (`*Service`) não importam nenhuma classe do Spring MVC, Spring Data ou RabbitMQ. Dependem apenas de interfaces definidas no próprio domínio.

### 9.3 Portas e Adaptadores
- **Input ports**: chamadas diretas aos serviços pelos controllers
- **Output ports**: `EncomendaRepository`, `MoradorRepository`, `FuncionarioRepository`, `EncomendaPublisher` — interfaces no domínio/aplicação, implementadas na infraestrutura

### 9.4 Testabilidade
A independência de frameworks permite testar os casos de uso com Mockito puro, sem subir contexto Spring, tornando os testes unitários rápidos e determinísticos.

---

## 10. Estrutura de Arquivos do Projeto

```
src/
├── main/java/com/projeto/
│   ├── Application.java                          # Entrada da aplicação
│   ├── domain/
│   │   ├── encomenda/
│   │   │   ├── Encomenda.java                    # Entidade principal
│   │   │   ├── EncomendaRepository.java          # Output port (interface)
│   │   │   └── StatusEncomenda.java              # Enum de status
│   │   ├── funcionario/
│   │   │   ├── Funcionario.java
│   │   │   └── FuncionarioRepository.java
│   │   ├── morador/
│   │   │   ├── Morador.java
│   │   │   └── MoradorRepository.java
│   │   └── usuario/
│   │       ├── Usuario.java                      # UserDetails do Spring Security
│   │       └── Perfil.java                       # Enum MORADOR/PORTEIRO
│   ├── application/
│   │   ├── encomenda/
│   │   │   ├── EncomendaService.java             # Caso de uso principal
│   │   │   └── EncomendaPublisher.java           # Output port mensageria
│   │   ├── morador/
│   │   │   └── MoradorService.java
│   │   └── funcionario/
│   │       └── FuncionarioService.java
│   └── infrastructure/
│       ├── config/
│       │   └── OpenApiConfig.java                # Swagger + JWT scheme
│       ├── messaging/
│       │   ├── EncomendaStreamPublisher.java     # RabbitMQ (prod)
│       │   ├── EncomendaDevPublisher.java        # Console (dev)
│       │   └── NotificacaoConsumer.java          # Consumer RabbitMQ
│       ├── persistence/
│       │   ├── EncomendaJpaRepository.java
│       │   ├── EncomendaRepositoryImpl.java
│       │   ├── FuncionarioJpaRepository.java
│       │   ├── FuncionarioRepositoryImpl.java
│       │   ├── MoradorJpaRepository.java
│       │   ├── MoradorRepositoryImpl.java
│       │   └── UsuarioJpaRepository.java
│       ├── security/
│       │   ├── JwtService.java                   # Geração/validação JWT
│       │   ├── JwtFilter.java                    # Filtro HTTP
│       │   ├── SecurityConfig.java               # Regras de acesso
│       │   └── UserDetailsConfig.java            # UserDetailsService + PasswordEncoder
│       └── web/
│           ├── AuthController.java               # POST /auth/login
│           ├── EncomendaController.java
│           ├── FuncionarioController.java
│           ├── MoradorController.java
│           └── GlobalExceptionHandler.java
├── main/resources/
│   ├── application.yml                           # Config base
│   ├── application-dev.yml                       # Perfil dev (H2, sem Rabbit)
│   └── application-prod.yml                      # Perfil prod (PostgreSQL + RabbitMQ)
└── test/
    ├── java/com/projeto/
    │   ├── application/
    │   │   ├── encomenda/EncomendaServiceTest.java
    │   │   ├── funcionario/FuncionarioServiceTest.java
    │   │   └── morador/MoradorServiceTest.java
    │   └── infrastructure/
    │       ├── messaging/EncomendaTestPublisher.java
    │       └── web/
    │           ├── EncomendaControllerIntegrationTest.java
    │           ├── FuncionarioControllerIntegrationTest.java
    │           └── MoradorControllerIntegrationTest.java
    └── resources/
        └── application.yml                       # Config de testes
```

