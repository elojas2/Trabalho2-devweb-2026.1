<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${not empty sessionScope.mensagem}">
    <div class="container mt-3">
        <div class="alert alert-${sessionScope.tipoMensagem != null ? sessionScope.tipoMensagem : 'info'} alert-dismissible fade show" role="alert">
            ${sessionScope.mensagem}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </div>
    <%-- Limpa a mensagem da sessão após exibir para não repetir no próximo refresh --%>
    <c:remove var="mensagem" scope="session" />
    <c:remove var="tipoMensagem" scope="session" />
</c:if>
