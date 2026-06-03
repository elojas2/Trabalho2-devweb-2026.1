USE biblioteca;

SELECT * FROM livros;

SELECT id, nome, email, perfil FROM usuarios;

SELECT titulo, ano FROM livros 
WHERE autor LIKE '%Machado de Assis%' 
ORDER BY ano DESC;

SELECT COUNT(*) AS total_disponiveis 
FROM livros 
WHERE disponivel = TRUE;


SELECT * FROM livros 
WHERE ano BETWEEN 1900 AND 1950 
ORDER BY ano ASC;


SELECT * FROM usuarios 
WHERE email = 'admin@biblioteca.com';


SELECT titulo, autor FROM livros 
WHERE disponivel = FALSE;


SELECT titulo, autor, created_at 
FROM livros 
ORDER BY created_at DESC 
LIMIT 5;
