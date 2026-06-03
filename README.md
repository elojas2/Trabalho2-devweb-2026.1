# Sistema de Gerenciamento de Biblioteca

Este é um projeto acadêmico desenvolvido para a disciplina de Desenvolvimento Web.
O sistema permite o gerenciamento (CRUD) de livros, autenticação de usuários e controle
de acesso.

## Tecnologias Utilizadas

- **Linguagem:** Java 17
- **Tecnologias Web:** Servlets 4.0 e JSP (JavaServer Pages)
- **Banco de Dados:** MySQL 8.4
- **Pool de Conexão:** HikariCP
- **Gerenciador de Dependências:** Maven
- **Containerização:** Docker & Docker Compose

---

## Como Executar o Projeto

Você pode rodar o projeto de duas maneiras: utilizando Docker (recomendado por ser mais rápido) ou configurando o ambiente localmente.

### Opção 1: Via Docker (Recomendado)

Esta opção configura automaticamente o Servidor (Tomcat) e o Banco de Dados (MySQL) com todos os dados populados.

**Pré-requisitos:** Docker e Docker Compose instalados.

1. No terminal, na raiz do projeto (pasta `/biblioteca`), execute:
   ```bash
   docker-compose up --build
   ```
2. Aguarde a finalização do build. A aplicação estará disponível em:
   [http://localhost:8080](http://localhost:8080)
3. O banco de dados MySQL estará acessível na porta `3307` do seu host.

---

### Opção 2: Execução Local (Manual)

**Pré-requisitos:** Java 17, Maven 3.8+, MySQL 8.0+ e Apache Tomcat 9.

#### 1. Banco de Dados (Passo a Passo)

Você precisará criar a estrutura do banco antes de rodar a aplicação. Existem duas formas principais:

**Via Terminal (Linha de Comando):**
1. Na raiz do projeto, acesse a pasta `/sql`:
   ```bash
   cd sql
   ```
2. Execute o comando para importar tudo (o script já cria o banco automaticamente):
   ```bash
   mysql -u seu_usuario -p < schema.sql
   mysql -u seu_usuario -p < seed.sql
   ```

**Via Interface Gráfica (MySQL Workbench / DBeaver):**
1. Abra seu gerenciador e conecte-se ao servidor.
2. Abra o arquivo `sql/schema.sql`, e execute-o por completo. O banco `biblioteca` aparecerá na sua lista de schemas.
3. Abra o arquivo `sql/seed.sql` e execute-o para popular os dados.

#### 2. Configuração da Aplicação
- Vá em `src/main/resources/` e renomeie o arquivo `db.properties.example` para `db.properties`.
- Edite o arquivo com suas credenciais locais do MySQL.

#### 3. Build e Deploy
- No terminal, na raiz do projeto (pasta `/biblioteca`), execute:
  ```bash
  mvn clean package
  ```
- O comando gerará um arquivo `biblioteca.war` dentro da pasta `target/`.
- **Deploy no Tomcat:**
  1. Localize a pasta onde o Apache Tomcat foi instalado.
  2. Copie o arquivo `biblioteca.war` para a subpasta `/webapps`.
  3. Para iniciar o servidor:
     - **Windows:** Execute `bin/startup.bat`.
     - **Linux/Mac:** Execute `bin/startup.sh`.
- Após o servidor subir, acesse: [http://localhost:8080/biblioteca](http://localhost:8080/biblioteca)

---

## Credenciais para Teste

O banco de dados já vem populado com os seguintes usuários:

| Perfil | Email | Senha |
| :--- | :--- | :--- |
| **Administrador** | admin@biblioteca.com | 123456 |
| **Usuário Comum** | elo@biblioteca.com | senha123 |

---

## Estrutura de Pastas

- `/src/main/java`: Código fonte Java (Controllers, DAOs, Models, Utils).
- `/src/main/webapp`: Arquivos JSP e recursos estáticos (CSS, JS).
- `/sql`: Scripts de banco de dados e consultas de exemplo.
- `Dockerfile` & `docker-compose.yml`: Configurações de ambiente isolado.

---

## Feito por

- Eloyse Fernanda
- Patricia Carvalho
