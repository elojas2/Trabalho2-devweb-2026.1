<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Ocorreu um erro - BiblioTech</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body class="bg-light d-flex align-items-center justify-content-center" style="height: 100vh;">

<div class="container text-center">
    <div class="bt-page-card py-5 shadow-sm">
        <h2 class="h1 fw-bold mb-4">Ops! Algo deu errado.</h2>
        
        <p class="lead mb-5">
            <c:choose>
                <c:when test="${pageContext.errorData.statusCode == 404}">
                    A página que você está procurando não existe ou foi movida.
                </c:when>
                <c:otherwise>
                    Tivemos um problema interno, mas já estamos tentando resolver.
                </c:otherwise>
            </c:choose>
        </p>

        <a href="${pageContext.request.contextPath}/" class="bt-btn bt-btn-accent px-4 py-2">
            Voltar para o Início
        </a>
    </div>
</div>

</body>
</html>
