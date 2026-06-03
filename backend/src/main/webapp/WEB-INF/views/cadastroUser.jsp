<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Criar Conta - BiblioTech</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body class="bg-light">
    <div class="auth-wrapper d-flex align-items-center justify-content-center py-5">
        <div class="bt-page-card" style="width: 100%; max-width: 420px;">
            <div class="card-body p-4">
                <h2 class="card-title text-center fw-bold mb-4">Criar uma nova conta</h2>

                <c:if test="${not empty mensagemErro}">
                    <div class="alert alert-danger text-center"><c:out value="${mensagemErro}" /></div>
                </c:if>

                <form id="formCadastro" action="${pageContext.request.contextPath}/cadastroUser" method="post">
                    
                    <div class="bt-form-group">
                        <label for="nome" class="form-label fw-semibold">Nome</label>
                        <input type="text" class="form-control" id="nome" name="nome" required placeholder="Seu nome completo">
                    </div>

                    <div class="bt-form-group">
                        <label for="email" class="form-label fw-semibold">E-mail</label>
                        <input type="email" class="form-control" id="email" name="email" required placeholder="seu@email.com">
                    </div>
                    
                    <div class="bt-form-group">
                        <label for="senha" class="form-label fw-semibold">Senha</label>
                        <input type="password" class="form-control" id="senha" name="senha" required placeholder="******">
                    </div>
                    
                    <button type="submit" class="bt-btn bt-btn-accent w-100 fw-bold mt-2">Cadastrar</button>
                </form>

                <p class="text-center text-muted mt-3 mb-0">
                    Já tem uma conta? <a href="${pageContext.request.contextPath}/login" class="fw-bold">Fazer login</a>
                </p>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>