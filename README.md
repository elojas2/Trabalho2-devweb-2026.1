# BiblioTech — Sistema de Gerenciamento de Biblioteca

Projeto acadêmico desenvolvido para a disciplina de Desenvolvimento Web (UFF).
Sistema de gerenciamento de biblioteca com autenticação, CRUD de livros e
controle de empréstimos.


---

## 🔗 Acesso Online (Live Demo)
O Front-end desta aplicação encontra-se hospedado e disponível para acesso público através do GitHub Pages.
**Acesse aqui:** [https://elojas2.github.io/Trabalho2-devweb-2026.1/](https://elojas2.github.io/Trabalho2-devweb-2026.1/)

---

## 🧪 Testes de API (Bruno) 
Na raiz do repositório, encontra-se a pasta `/bruno` contendo a Collection exportada com as requisições GET, POST, PUT e DELETE. 
Para testar:
1. Faça o download da ferramenta [Bruno](https://www.usebruno.com/).
2. Clique em **Open Collection** e selecione a pasta `bruno/` deste repositório.

---

## Tecnologias

**Backend**
- Java 17 + Servlets 4.0
- MySQL 8.4
- HikariCP (pool de conexões)
- Maven + Apache Tomcat 9

**Frontend**
- React 19 + TypeScript
- React Router DOM
- Vite

---

## Como Executar

### Opção 1 — Docker (recomendado)

**Pré-requisitos:** Docker Compose instalados.

```bash
docker compose up --build
```

Aguarde o build finalizar. Os serviços sobem automaticamente:

| Serviço  | URL                     |
| :------- | :---------------------- |
| Frontend | http://localhost:5173   |
| Backend  | http://localhost:8080   |
| MySQL    | localhost:3307          |

---

### Opção 2 — Execução Local

**Pré-requisitos:** Java 17+, Maven 3.8+, MySQL 8+, Node.js 20+, Apache Tomcat 9.

#### 1. Banco de Dados

Inicie o MySQL e execute os scripts:

```bash
mysql -u root -p < backend/sql/schema.sql
mysql -u root -p < backend/sql/seed.sql
```

#### 2. Configuração do Backend

Copie o arquivo de exemplo e edite com suas credenciais:

```bash
cp backend/src/main/resources/db.properties.example backend/src/main/resources/db.properties
```

```properties
db.url=jdbc:mysql://localhost:3306/biblioteca?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Sao_Paulo
db.user=seu_usuario
db.password=sua_senha
```

#### 3. Instalar o Tomcat 9

**macOS (Homebrew):**
```bash
brew install tomcat@9
```

**Windows/Linux:** Baixe em https://tomcat.apache.org/download-90.cgi e extraia
em uma pasta de sua preferência.

#### 4. Build e Deploy

```bash
cd backend
mvn clean package -DskipTests
```

Copie o `.war` gerado para a pasta `webapps` do Tomcat:

**macOS (Homebrew):**
```bash
cp target/biblioteca.war /opt/homebrew/Cellar/tomcat@9/9.0.118/libexec/webapps/ROOT.war
/opt/homebrew/Cellar/tomcat@9/9.0.118/bin/catalina start
```

**Windows:**
```bash
copy target\biblioteca.war C:\caminho-do-tomcat\webapps\ROOT.war
C:\caminho-do-tomcat\bin\startup.bat
```

**Linux:**
```bash
cp target/biblioteca.war /opt/tomcat/webapps/ROOT.war
/opt/tomcat/bin/catalina.sh start
```

Acesse `http://localhost:8080`.

#### 4. Frontend

```bash
cd frontend
npm install
npm run dev
```

Acesse `http://localhost:5173`.

---

## Credenciais de Teste

| Perfil            | E-mail                  | Senha      |
| :---------------- | :---------------------- | :--------- |
| **Administrador** | admin@biblioteca.com    | 123456     |
| **Usuário Comum** | elo@biblioteca.com      | senha123   |

---

## Estrutura do Projeto

```
/
├── bruno/
├── docker-compose.yml
├── backend/
│   ├── Dockerfile
│   ├── pom.xml
│   ├── sql/               # Scripts de banco (schema + seed)
│   └── src/main/java/
│       ├── controller/    # Servlets (API REST)
│       ├── dao/           # Acesso ao banco
│       ├── model/         # Entidades
│       └── util/          # Filtros, conexão DB
└── frontend/
    ├── Dockerfile
    └── src/
        ├── api.ts          # Wrapper de chamadas à API
        ├── contexts/       # AuthContext (estado de autenticação)
        ├── components/     # Navbar, ProtectedRoute
        └── pages/          # Login, Cadastro, Livros, Empréstimos
```

---

## Feito por

- Eloyse Fernanda
- Patricia Carvalho
