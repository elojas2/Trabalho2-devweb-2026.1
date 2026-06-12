package controller;

import dao.UsuarioDAO;
import model.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends BaseServlet {
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        com.google.gson.JsonObject body;
        try {
            body = lerJson(req);
        } catch (Exception e) {
            enviarJson(resp, new Resposta("Erro no corpo da requisição: " + e.getMessage(), "danger"), HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String email = body.has("email") ? body.get("email").getAsString() : null;
        String senha = body.has("senha") ? body.get("senha").getAsString() : null;

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

            // Adiciona o cabeçalho manualmente para garantir SameSite=None; Secure
            // Nota: O navegador aceita Secure no localhost mesmo sem HTTPS
            String cookieHeader = String.format("JSESSIONID=%s; Path=/; HttpOnly; SameSite=None; Secure", session.getId());
            resp.addHeader("Set-Cookie", cookieHeader);

            enviarJson(resp, usuario, HttpServletResponse.SC_OK);
        } else {
            enviarJson(resp, new Resposta("E-mail ou senha inválidos.", "danger"), HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
