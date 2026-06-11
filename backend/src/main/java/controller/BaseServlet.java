package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import util.LocalDateAdapter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;

public abstract class BaseServlet extends HttpServlet {
    protected final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();

    protected void enviarJson(HttpServletResponse resp, Object dado, int status) throws IOException {
        resp.setStatus(status);
        resp.setContentType("application/json; charset=UTF-8");

        // Injetar cabeçalhos para EVITAR cache
        resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        resp.setHeader("Pragma", "no-cache"); 
        resp.setHeader("Expires", "0"); 

        PrintWriter out = resp.getWriter();
        out.print(this.gson.toJson(dado));
        out.flush();
    }

    protected void enviarJson(HttpServletResponse resp, Object dado) throws IOException {
        enviarJson(resp, dado, HttpServletResponse.SC_OK);
    }

    // Classe utilitária para respostas padrão de mensagem
    protected static class Resposta {
        String mensagem;
        String tipo;
        Resposta(String mensagem, String tipo) {
            this.mensagem = mensagem;
            this.tipo = tipo;
        }
    }
}
