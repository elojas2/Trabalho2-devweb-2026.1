<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>BiblioTech - Bem-vindo</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
    <div class="bt-home-hero">
        <h1 class="display-3 fw-bold mb-3">BiblioTech</h1>
        <p class="lead mb-4" style="max-width: 500px;">Sua porta de entrada para o conhecimento. Explore, aprenda e gerencie sua coleção.</p>

        <div class="d-flex gap-3 flex-wrap justify-content-center">
            <a href="${pageContext.request.contextPath}/login" class="bt-btn bt-btn-accent btn-lg fw-semibold px-4">Entrar</a>
            <a href="${pageContext.request.contextPath}/cadastroUser" class="bt-btn bt-btn-outline btn-lg fw-semibold px-4">Criar Conta</a>
        </div>

        <footer class="mt-5 text-white-50 small">
            &copy; 2026 Sistema de Biblioteca Digital
        </footer>
    </div>
</body>
</html>
