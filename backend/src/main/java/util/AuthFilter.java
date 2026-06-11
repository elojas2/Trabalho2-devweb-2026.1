package util;

import model.Usuario;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        String uri = req.getRequestURI();
        String contextPath = req.getContextPath();

        // Rotas que não precisam de login
        boolean isStaticResource = uri.contains("/assets/");
        boolean isLoginPage = uri.equals(contextPath + "/login");
        boolean isCadastroPage = uri.contains("/cadastro");
        boolean isIndex = uri.equals(contextPath + "/") || uri.equals(contextPath + "/index.jsp");

        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuarioLogado") : null;
        boolean loggedIn = (usuario != null);


        if (isLoginPage || isCadastroPage || isIndex) {
            chain.doFilter(request, response);
            return;
        }

        if (isStaticResource) {
        // Injetar cabeçalhos para FORÇAR o cache de estáticos
            resp.setHeader("Cache-Control", "public, max-age=31536000");
    
            chain.doFilter(request, response);
        return;
        }

        if (!loggedIn) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.setContentType("application/json");
            resp.getWriter().write("{\"mensagem\": \"Sessão expirada ou usuário não autenticado.\", \"tipo\": \"danger\"}");
            return;
        }

        // Apenas ADMIN
        boolean isAdminRoute = uri.contains("/livros/cadastrar") ||
                               uri.contains("/livros/editar") ||
                               uri.contains("/livros/excluir");

        if (isAdminRoute && !usuario.isAdmin()) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            resp.setContentType("application/json");
            resp.getWriter().write("{\"mensagem\": \"Acesso negado: Você não tem permissão de administrador.\", \"tipo\": \"danger\"}");
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {}
}
