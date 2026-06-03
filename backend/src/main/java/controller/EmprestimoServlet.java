package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.EmprestimoDAO;
import dao.LivroDAO;
import model.Emprestimo;
import model.Livro;
import model.Usuario;
import util.LocalDateAdapter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/emprestimos")
public class EmprestimoServlet extends BaseServlet {
    private EmprestimoDAO emprestimoDAO = new EmprestimoDAO();
    private LivroDAO livroDAO = new LivroDAO();

    // GET: Lista de empréstimos do usuário
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

        if (usuario == null) {
            enviarJson(response, new Resposta("Usuário não autenticado", "danger"), HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        try {
            List<Emprestimo> meusEmprestimos = emprestimoDAO.listarPorUsuario(usuario.getId());
            enviarJson(response, meusEmprestimos);
        } catch (SQLException e) {
            enviarJson(response, new Resposta("Erro ao listar empréstimos: " + e.getMessage(), "danger"), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    // POST: Realiza o empréstimo ou a devolução
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        String action = request.getParameter("action");

        if (usuario == null) {
            enviarJson(response, new Resposta("Usuário não autenticado", "danger"), HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        try {
            if ("emprestar".equals(action)) {
                String idLivroStr = request.getParameter("idLivro");
                if (idLivroStr == null) {
                    enviarJson(response, new Resposta("ID do livro é obrigatório.", "danger"), HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
                int idLivro = Integer.parseInt(idLivroStr);
                Livro livro = livroDAO.buscarPorId(idLivro);

                if (livro != null && livro.isDisponivel()) {
                    Emprestimo e = new Emprestimo();
                    e.setUsuario(usuario);
                    e.setLivro(livro);
                    e.setDataEmprestimo(LocalDate.now());

                    emprestimoDAO.registrarEmprestimo(e);
                    enviarJson(response, new Resposta("Livro '" + livro.getTitulo() + "' emprestado com sucesso!", "success"));
                } else {
                    enviarJson(response, new Resposta("Desculpe, este livro já não está mais disponível.", "danger"), HttpServletResponse.SC_CONFLICT);
                }
            } else if ("devolver".equals(action)) {
                String idEmprestimoStr = request.getParameter("idEmprestimo");
                if (idEmprestimoStr == null) {
                    enviarJson(response, new Resposta("ID do empréstimo é obrigatório.", "danger"), HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
                int idEmprestimo = Integer.parseInt(idEmprestimoStr);
                emprestimoDAO.devolverLivro(idEmprestimo);
                enviarJson(response, new Resposta("Livro devolvido com sucesso!", "success"));
            } else {
                enviarJson(response, new Resposta("Ação inválida.", "danger"), HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (SQLException | NumberFormatException e) {
            enviarJson(response, new Resposta("Erro ao processar a operação: " + e.getMessage(), "danger"), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}