<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Catálogo - BiblioTech</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body class="bg-light">

<nav class="bt-navbar">
    <a href="${pageContext.request.contextPath}/" class="bt-logo">BiblioTech</a>
    <div class="bt-user-info">
        <c:choose>
            <c:when test="${not empty sessionScope.usuarioLogado}">
                Olá, <strong><c:out value="${sessionScope.usuarioLogado.nome}" /></strong> (<c:out value="${sessionScope.usuarioLogado.perfil}" />)
                
                <a href="${pageContext.request.contextPath}/emprestimos" class="bt-btn bt-btn-outline ms-2">Meus Empréstimos</a>
                
                <a href="${pageContext.request.contextPath}/logout" class="bt-btn bt-btn-outline ms-2">Sair</a>
            </c:when>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/login" class="bt-btn bt-btn-outline">Entrar</a>
            </c:otherwise>
        </c:choose>
    </div>
</nav>

<div class="bt-container">
    <jsp:include page="../common/mensagens.jsp" />

    <div class="d-flex justify-content-between align-items-center mb-4 flex-wrap gap-3">
        <h1 class="h3 fw-bold mb-0">Catálogo de Livros</h1>
        
        <div class="d-flex gap-2 flex-grow-1 justify-content-md-end">
            <form action="${pageContext.request.contextPath}/livros" method="get" class="d-flex gap-2">
                <input type="text" name="q" class="form-control" placeholder="Título ou autor..." value="<c:out value='${termoBusca}' />" style="max-width: 300px;">
                <button type="submit" class="bt-btn bt-btn-accent">Buscar</button>
            </form>
            
            <c:if test="${sessionScope.usuarioLogado.admin}">
                <a href="${pageContext.request.contextPath}/livros/cadastrar" class="bt-btn bt-btn-accent">+ Novo Livro</a>
            </c:if>
        </div>
    </div>

    <div class="bt-page-card">
        <div class="table-responsive">
            <table class="bt-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Título</th>
                        <th>Autor</th>
                        <th>Ano</th>
                        <th>Status</th>
                        <th>Ações</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="livro" items="${livros}">
                        <tr>
                            <td><c:out value="${livro.id}" /></td>
                            <td><c:out value="${livro.titulo}" /></td>
                            <td><c:out value="${livro.autor}" /></td>
                            <td><c:out value="${livro.ano}" /></td>
                            <td>
                                <c:choose>
                                    <c:when test="${livro.disponivel}">
                                        <span class="bt-badge bt-badge-success">Disponível</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="bt-badge bt-badge-danger">Emprestado</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                            	<c:if test="${livro.disponivel}">
        							<form action="${pageContext.request.contextPath}/emprestimos" method="POST" class="d-inline">
           								 <input type="hidden" name="action" value="emprestar">
            							 <input type="hidden" name="idLivro" value="${livro.id}">
            							 <button type="submit" class="bt-btn bt-btn-accent bt-btn-sm">Emprestar</button>
        							</form>
    							</c:if>
                                <c:if test="${sessionScope.usuarioLogado.admin}">
                                    <div class="d-flex gap-2">
                                        <a href="${pageContext.request.contextPath}/livros/editar?id=${livro.id}" class="bt-btn bt-btn-edit">Editar</a>
                                        <form action="${pageContext.request.contextPath}/livros/excluir" method="post" onsubmit="return confirm('Excluir livro?')">
                                            <input type="hidden" name="id" value="${livro.id}">
                                            <button type="submit" class="bt-btn bt-btn-delete">Excluir</button>
                                        </form>
                                    </div>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
