# BibliAF - API de Gerenciamento de Bibliotecas

**BibliAF** é uma API REST desenvolvida em **Spring Boot** para o gerenciamento eficiente de bibliotecas. Esta API foi projetada para facilitar o acesso e a organização de informações sobre livros, autores, usuários, reservas e empréstimos, proporcionando um sistema robusto para operações de empréstimos e devoluções.

## Funcionalidades

- **Gerenciamento de Livros**: Adicionar, listar, editar e excluir livros.
- **Cadastro de Autores**: Registrar, listar e atualizar informações sobre autores.
- **Gestão de Usuários**: Criar, editar e excluir usuários.
- **Reservas de Livros**: Realizar reservas de livros.
- **Empréstimos e Devoluções**: Realizar operações de empréstimo e devolução de livros entre os usuários.
- **Autenticação e Autorização**: Proteção de rotas por meio de autenticação de usuários e permissões adequadas.

## Tecnologias Utilizadas

- **Spring Boot**: Framework para criação de APIs Java.
- **Spring Data JPA**: Para acesso a banco de dados utilizando JPA.
- **PostgreSQL**: Banco de dados relacional utilizado para persistência de dados.
- **Spring Security**: Implementação de segurança e autenticação.
- **JWT (JSON Web Token)**: Para autenticação de usuários.
- **Maven**: Gerenciador de dependências e build.

## Como Rodar o Projeto

### Pré-requisitos

- **Java 17+** instalado.
- **Maven** instalado.

### Passos para Rodar

1. Clone o repositório:

   ```bash
   git clone https://github.com/RenanTGMF/bibliAF-backend.git
   ```

2. Navegue até o diretório do projeto:

   ```bash
   cd bibliaf
   ```

3. Execute o projeto com Maven:

   ```bash
   mvn spring-boot:run
   ```

4. A API estará disponível em `http://localhost:8888`.

## Endpoints

### **Autenticação**
Endpoint utilizado para operações de login e registro de usuários.

- **POST** `/auth/register`: Realiza o registro de um novo usuário.
- **POST** `/auth/login`: Realiza o login do usuário.

### **Usuários**
Endpoint usado para operações que envolvem usuários.

- **GET** `/users`: Busca todos os usuários com paginação.
- **PUT** `/users`: Atualiza as informações de um usuário.
- **POST** `/users`: Cria um novo usuário.
- **GET** `/users/{id}`: Busca um usuário pelo ID.
- **DELETE** `/users/{id}`: Deleta um usuário.
- **GET** `/users/find/username/{username}`: Busca usuários pelo nome de usuário.
- **GET** `/users/find/email/{email}`: Busca usuários por e-mail.

### **Autores**
Endpoint usado para operações que envolvem autores.

- **GET** `/authors`: Lista todos os autores.
- **PUT** `/authors`: Atualiza um autor.
- **POST** `/authors`: Cria um novo autor.
- **GET** `/authors/{id}`: Busca um autor pelo ID.
- **DELETE** `/authors/{id}`: Deleta um autor pelo ID.
- **GET** `/authors/paged`: Lista autores paginados.
- **GET** `/authors/find/name/{name}`: Busca autores pelo nome.

### **Reservas**
Endpoint usado para operações relacionadas a reservas de livros.

- **GET** `/reservations`: Obter todas as reservas.
- **PUT** `/reservations`: Atualizar uma reserva existente.
- **POST** `/reservations`: Criar uma nova reserva.
- **GET** `/reservations/{id}`: Obter reserva pelo ID.
- **DELETE** `/reservations/{id}`: Excluir uma reserva pelo ID.
- **GET** `/reservations/find/user/{userId}`: Obter reservas por usuário.
- **GET** `/reservations/find/book/{bookId}`: Obter reservas por livro.

### **Livros**
Endpoint usado para operações que envolvem livros.

- **GET** `/books`: Busca todos os livros com paginação.
- **PUT** `/books`: Atualiza as informações de um livro.
- **POST** `/books`: Cria um novo livro.
- **GET** `/books/{id}`: Busca um livro pelo ID.
- **DELETE** `/books/{id}`: Deleta um livro.
- **GET** `/books/find/title/{title}`: Busca livros pelo título.
- **GET** `/books/find/genre/{genre}`: Busca livros por gênero.
- **GET** `/books/find/author/{author}`: Busca livros por autor.

### **Gêneros**
Endpoint usado para operações que envolvem gêneros.

- **GET** `/genres`: Obter todos os gêneros.
- **PUT** `/genres`: Atualizar um gênero existente.
- **POST** `/genres`: Criar um novo gênero.
- **GET** `/genres/{id}`: Obter gênero pelo ID.
- **DELETE** `/genres/{id}`: Deletar um gênero pelo ID.
- **GET** `/genres/paged`: Obter gêneros com paginação.
- **GET** `/genres/find/name/{name}`: Encontrar gêneros pelo nome.

### **Empréstimos**
Endpoint usado para operações relacionadas a empréstimos.

- **GET** `/loans`: Obter todos os empréstimos.
- **PUT** `/loans`: Atualizar um empréstimo existente.
- **POST** `/loans`: Criar um novo empréstimo.
- **GET** `/loans/{id}`: Obter empréstimo pelo ID.
- **DELETE** `/loans/{id}`: Excluir um empréstimo pelo ID.
- **GET** `/loans/find/user/{userId}`: Obter empréstimos por usuário.
- **GET** `/loans/find/book/{bookId}`: Obter empréstimos por livro.

## Contribuição

1. Faça um fork do projeto.
2. Crie uma branch para suas alterações (`git checkout -b feature/nome-da-feature`).
3. Commit suas alterações (`git commit -am 'Adiciona nova funcionalidade'`).
4. Envie para o repositório (`git push origin feature/nome-da-feature`).
5. Crie uma nova Pull Request.
