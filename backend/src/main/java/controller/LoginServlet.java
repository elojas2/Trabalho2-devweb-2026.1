package controller;

import com.google.gson.Gson;
import dao.UsuarioDAO;
import model.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private Gson gson = new Gson();

    private void enviarJson(HttpServletResponse resp, Object dado, int status) throws IOException {
        resp.setStatus(status);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();
        out.print(this.gson.toJson(dado));
        out.flush();
    }

    private class Resposta {
        String mensagem;
        String tipo;
        Resposta(String mensagem, String tipo) {
            this.mensagem = mensagem;
            this.tipo = tipo;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String senha = req.getParameter("senha");

        if (email == null || email.trim().isEmpty() || senha == null || senha.trim().isEmpty()) {
            enviarJson(resp, new Resposta("E-mail e senha são obrigatórios.", "danger"), HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Usuario usuario = usuarioDAO.autenticar(email, senha);

        if (usuario != null) {
            HttpSession session = req.getSession();
            req.changeSessionId();
            // Removemos a senha por segurança antes de enviar o JSON
            usuario.setSenha(null); 
            session.setAttribute("usuarioLogado", usuario);
            enviarJson(resp, usuario, HttpServletResponse.SC_OK);
        } else {
            enviarJson(resp, new Resposta("E-mail ou senha inválidos.", "danger"), HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
