USE biblioteca;

-- Populando livros
INSERT INTO livros (titulo, autor, ano, disponivel)
VALUES 
    ('Dom Casmurro', 'Machado de Assis', 1899, TRUE),
    ('O Cortiço', 'Aluísio Azevedo', 1890, FALSE),
    ('Grande Sertão: Veredas', 'Guimarães Rosa', 1956, TRUE),
    ('Capitães da Areia', 'Jorge Amado', 1937, FALSE),
    ('Memórias Póstumas de Brás Cubas', 'Machado de Assis', 1881, TRUE),
    ('O Guarani', 'José de Alencar', 1857, TRUE),
    ('Iracema', 'José de Alencar', 1865, TRUE),
    ('A Hora da Estrela', 'Clarice Lispector', 1977, TRUE),
    ('Vidas Secas', 'Graciliano Ramos', 1938, TRUE),
    ('Sagarana', 'Guimarães Rosa', 1946, TRUE),
    ('1984', 'George Orwell', 1949, TRUE),
    ('O Senhor dos Anéis: A Sociedade do Anel', 'J.R.R. Tolkien', 1954, TRUE),
    ('O Hobbit', 'J.R.R. Tolkien', 1937, TRUE),
    ('Fundação', 'Isaac Asimov', 1951, TRUE),
    ('O Pequeno Príncipe', 'Antoine de Saint-Exupéry', 1943, TRUE),
    ('Moby Dick', 'Herman Melville', 1851, FALSE),
    ('Orgulho e Preconceito', 'Jane Austen', 1813, TRUE),
    ('O Código Da Vinci', 'Dan Brown', 2003, TRUE),
    ('O Alquimista', 'Paulo Coelho', 1988, TRUE),
    ('Ensaio Sobre a Cegueira', 'José Saramago', 1995, TRUE)
ON DUPLICATE KEY UPDATE titulo=titulo;

-- Populando usuários (admin e usuários comuns)
INSERT INTO usuarios (nome, email, senha, perfil)
VALUES 
    ('Administrador', 'admin@biblioteca.com', '123456', 'ADMIN'),
    ('Eloyse Fernanda', 'elo@biblioteca.com', 'senha123', 'USUARIO'),
    ('Patricia Carvallho', 'patricia@email.com', '123456', 'USUARIO'),
    ('Maria Oliveira', 'maria@email.com', '654321', 'USUARIO')
ON DUPLICATE KEY UPDATE email=email;
