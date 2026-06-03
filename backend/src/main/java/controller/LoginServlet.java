package controller;

import dao.UsuarioDAO;
import model.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String senha = req.getParameter("senha");

        if (email == null || email.trim().isEmpty() || senha == null || senha.trim().isEmpty()) {
            req.setAttribute("erro", "E-mail ou senha inválidos.");
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
            return;
        }

        Usuario usuario = usuarioDAO.autenticar(email, senha);

        if (usuario != null) {
            HttpSession session = req.getSession();
            req.changeSessionId();
            session.setAttribute("usuarioLogado", usuario);
            resp.sendRedirect(req.getContextPath() + "/livros");
        } else {
            req.setAttribute("erro", "E-mail ou senha inválidos.");
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
        }
    }
}
