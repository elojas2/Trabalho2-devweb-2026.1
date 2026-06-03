<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Editar Livro - BiblioTech</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body class="bg-light">

<nav class="bt-navbar">
    <a href="${pageContext.request.contextPath}/" class="bt-logo">BiblioTech</a>
    <div class="bt-user-info">
        <c:if test="${not empty sessionScope.usuarioLogado}">
            Olá, <strong><c:out value="${sessionScope.usuarioLogado.nome}" escapeXml="true" /></strong>
            <a href="${pageContext.request.contextPath}/logout" class="bt-btn bt-btn-outline ms-2">Sair</a>
        </c:if>
    </div>
</nav>

<div class="bt-container">
    <div class="row justify-content-center">
        <div class="col-lg-6">
            <div class="bt-page-card">
                <div class="card-body p-4">
                    <h1 class="h4 fw-bold mb-4">Editar livro</h1>

                    <form id="formLivro" action="${pageContext.request.contextPath}/livros/editar" method="post">
                        <input type="hidden" name="id" value="${livro.id}">
                        <div class="bt-form-group">
                            <label for="titulo" class="form-label fw-semibold">Título</label>
                            <input type="text" class="form-control" name="titulo" id="titulo" value="${livro.titulo}" required>
                        </div>
                        <div class="bt-form-group">
                            <label for="autor" class="form-label fw-semibold">Autor</label>
                            <input type="text" class="form-control" name="autor" id="autor" value="${livro.autor}" required>
                        </div>
                        <div class="bt-form-group">
                            <label for="ano" class="form-label fw-semibold">Ano</label>
                            <input type="number" class="form-control" name="ano" id="ano" value="${livro.ano}" required>
                        </div>
                        <div class="bt-form-group">
                            <label for="disponivel" class="form-label fw-semibold">Disponível</label>
                            <select class="form-select" name="disponivel" id="disponivel">
                                <option value="true" ${livro.disponivel ? 'selected' : ''}>Sim</option>
                                <option value="false" ${not livro.disponivel ? 'selected' : ''}>Não</option>
                            </select>
                        </div>
                        <span id="msgErro" class="text-danger small" style="display:none;"></span>
                        <button type="submit" class="bt-btn bt-btn-accent w-100 fw-bold mt-2">Salvar Alterações</button>
                    </form>

                    <a href="${pageContext.request.contextPath}/livros" class="d-inline-block mt-3 text-decoration-none">← Voltar ao catálogo</a>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/assets/js/validacao-livro.js"></script>
</body>
</html>