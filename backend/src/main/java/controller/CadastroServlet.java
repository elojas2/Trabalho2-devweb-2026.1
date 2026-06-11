package controller;

import com.google.gson.Gson;
import dao.UsuarioDAO;
import model.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/cadastroUser")
public class CadastroServlet extends BaseServlet {
    private UsuarioDAO dao = new UsuarioDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        com.google.gson.JsonObject body;
        try {
            body = lerJson(request);
        } catch (Exception e) {
            enviarJson(response, new Resposta("Erro no corpo da requisição: " + e.getMessage(), "danger"), HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String nomeRecebido = body.has("nome") ? body.get("nome").getAsString() : null;
        String emailRecebido = body.has("email") ? body.get("email").getAsString() : null;
        String senhaRecebida = body.has("senha") ? body.get("senha").getAsString() : null;

        if (nomeRecebido == null || nomeRecebido.trim().isEmpty() ||
            emailRecebido == null || emailRecebido.trim().isEmpty() ||
            senhaRecebida == null || senhaRecebida.trim().isEmpty()) {
            enviarJson(response, new Resposta("Todos os campos são obrigatórios.", "danger"), HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(nomeRecebido);
        novoUsuario.setEmail(emailRecebido);
        novoUsuario.setSenha(senhaRecebida);

        try {
            dao.cadastrar(novoUsuario);
            enviarJson(response, new Resposta("Usuário cadastrado com sucesso!", "success"), HttpServletResponse.SC_CREATED);
        } catch (Exception e) {
            enviarJson(response, new Resposta("Erro ao cadastrar usuário: " + e.getMessage(), "danger"), HttpServletResponse.SC_CONFLICT);
        }
    }
}