<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Meus Empréstimos - BiblioTech</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="container mt-5">
    <h2>Meus Livros Emprestados</h2>
    
    <c:if test="${not empty sessionScope.mensagem}">
    	<div class="alert alert-success">${sessionScope.mensagem}</div>
    	<% session.removeAttribute("mensagem"); %> 
	</c:if>
	
    <c:if test="${not empty mensagem}">
        <div class="alert alert-success">${mensagem}</div>
        <% session.removeAttribute("mensagem"); %>
    </c:if>

    <table class="table table-striped mt-4">
        <thead>
            <tr>
                <th>Livro</th>
                <th>Data Empréstimo</th>
                <th>Status</th>
                <th>Ação</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="emp" items="${meusEmprestimos}">
                <tr>
                    <td>${emp.livro.titulo}</td>
                    <td>${emp.dataEmprestimo}</td>
                    <td><span class="badge bg-info">${emp.status}</span></td>
                    <td>
                        <c:if test="${emp.status == 'ATIVO'}">
                            <form action="${pageContext.request.contextPath}/emprestimos" method="POST">
                                <input type="hidden" name="action" value="devolver">
                                <input type="hidden" name="idEmprestimo" value="${emp.id}">
                                <button type="submit" class="btn btn-primary btn-sm">Devolver</button>
                            </form>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    <a href="${pageContext.request.contextPath}/livros?action=listar" class="btn btn-secondary">Voltar para a Biblioteca</a>
</body>
</html>