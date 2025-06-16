# 🔐 Sistema CRUD com Autenticação JWT

Uma API REST em Java Spring Boot que implementa um sistema completo de CRUD (Create, Read, Update, Delete) para usuários com autenticação JWT e controle de acesso baseado em roles.

## 🚀 Tecnologias Utilizadas

- **Java 17+**
- **Spring Boot 3.x**
- **Spring Security**
- **JWT (JSON Web Tokens)**
- **Spring Data JPA**
- **H2 Database** (desenvolvimento)
- **Maven**

## 📋 Funcionalidades

### 🔑 Autenticação
- Login com email e senha
- Registro de novos usuários
- Geração de tokens JWT
- Controle de expiração de tokens

### 👥 Sistema de Roles
- **USER**: Acesso limitado aos próprios dados
- **ADMIN**: Acesso completo ao sistema

### 📊 CRUD de Usuários
- **Usuários comuns**: Podem ver e editar apenas seu próprio perfil
- **Administradores**: Podem gerenciar todos os usuários

## 🎯 Casos de Uso

### Usuário Comum (USER)
- ✅ Fazer login
- ✅ Ver próprio perfil
- ✅ Atualizar próprios dados
- ❌ Ver outros usuários
- ❌ Deletar usuários

### Administrador (ADMIN)
- ✅ Fazer login
- ✅ Ver próprio perfil
- ✅ Atualizar próprios dados
- ✅ Listar todos os usuários
- ✅ Atualizar qualquer usuário
- ✅ Deletar usuários

## 🏗️ Estrutura do Projeto

```
src/main/java/atividadeJwt/demo/
├── controller/
│   ├── AdminController.java      # Endpoints administrativos
│   ├── AuthController.java       # Autenticação e registro
│   └── UsuarioController.java    # Perfil do usuário
├── entity/
│   └── Usuario.java              # Entidade usuário
├── repository/
│   └── UsuarioRepository.java    # Repositório JPA
├── security/
│   ├── JwtUtil.java             # Utilitários JWT
│   └── SecurityConfig.java      # Configurações de segurança
└── DemoApplication.java         # Classe principal
```

## 🔌 Endpoints da API

### 🔓 Públicos (Sem autenticação)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/auth/login` | Login do usuário |
| POST | `/auth/register` | Registro de novo usuário |

### 🔒 Protegidos (Requer token JWT)

#### 👤 Usuário Comum
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/usuario/profile` | Ver próprio perfil |
| PUT | `/usuario/profile` | Atualizar próprio perfil |

#### 👑 Apenas Administradores
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/admin/usuarios` | Listar todos os usuários |
| PUT | `/admin/usuarios/{id}` | Atualizar qualquer usuário |
| DELETE | `/admin/usuarios/{id}` | Deletar usuário |

## 📝 Exemplos de Uso

### 1. Registro de Usuário
```bash
POST /auth/register
Content-Type: application/json

{
  "nome": "João Silva",
  "email": "joao@exemplo.com",
  "senha": "123456",
  "role": "USER"
}
```

### 2. Login
```bash
POST /auth/login
Content-Type: application/json

{
  "email": "joao@exemplo.com",
  "senha": "123456"
}
```

**Resposta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "email": "joao@exemplo.com",
  "role": "USER"
}
```

### 3. Acessar Endpoints Protegidos
```bash
GET /usuario/profile
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

## 🛠️ Como Executar

### Pré-requisitos
- Java 17 ou superior
- Maven 3.6+

### Passos
1. Clone o repositório:
```bash
git clone <url-do-repositorio>
cd atividade-jwt-demo
```

2. Execute a aplicação:
```bash
mvn spring-boot:run
```

3. A API estará disponível em: `http://localhost:8080`

## 🗄️ Banco de Dados

Por padrão, a aplicação usa H2 Database em memória para desenvolvimento.

**Console H2:** `http://localhost:8080/h2-console`
- **JDBC URL:** `jdbc:h2:mem:testdb`
- **User:** `sa`
- **Password:** *(vazio)*

## 🔐 Segurança

### JWT Configuration
- **Algoritmo:** HS256
- **Expiração:** 2 horas
- **Header:** `Authorization: Bearer <token>`

### Roles e Permissões
- **USER**: Acesso aos endpoints `/usuario/*`
- **ADMIN**: Acesso a todos os endpoints, incluindo `/admin/*`

## 📊 Status Codes

| Code | Descrição |
|------|-----------|
| 200 | Sucesso |
| 201 | Criado com sucesso |
| 400 | Dados inválidos |
| 401 | Não autenticado |
| 403 | Sem permissão |
| 404 | Não encontrado |
| 500 | Erro interno |
