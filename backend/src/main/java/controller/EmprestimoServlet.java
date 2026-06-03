package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.EmprestimoDAO;
import dao.LivroDAO;
import model.Emprestimo;
import model.Livro;
import model.Usuario;

@WebServlet("/emprestimos")
public class EmprestimoServlet extends HttpServlet {
    private EmprestimoDAO emprestimoDAO = new EmprestimoDAO();
    private LivroDAO livroDAO = new LivroDAO();

    // GET: Mostra a lista de empréstimos do utilizador
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

        try {
            List<Emprestimo> meusEmprestimos = emprestimoDAO.listarPorUsuario(usuario.getId());
            request.setAttribute("meusEmprestimos", meusEmprestimos);
            request.getRequestDispatcher("/WEB-INF/views/emprestimos/meusEmprestimos.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    // POST: Realiza o empréstimo ou a devolução
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        String action = request.getParameter("action");

        try {
            if ("emprestar".equals(action)) {
                int idLivro = Integer.parseInt(request.getParameter("idLivro"));
                Livro livro = livroDAO.buscarPorId(idLivro);
                
                if (livro != null && livro.isDisponivel()) {
                    Emprestimo e = new Emprestimo();
                    e.setUsuario(usuario);
                    e.setLivro(livro);
                    e.setDataEmprestimo(LocalDate.now());
                    
                    emprestimoDAO.registrarEmprestimo(e);
                    session.setAttribute("mensagem", "Livro '" + livro.getTitulo() + "' emprestado com sucesso!");
                }else{
                    session.setAttribute("erro", "Desculpe, este livro já não está mais disponível.");
                }
            } else if ("devolver".equals(action)) {
                int idEmprestimo = Integer.parseInt(request.getParameter("idEmprestimo"));
                emprestimoDAO.devolverLivro(idEmprestimo);
                session.setAttribute("mensagem", "Livro devolvido com sucesso!");
            }
            response.sendRedirect(request.getContextPath() + "/emprestimos");
        } catch (SQLException e) {
            e.printStackTrace();
            session.setAttribute("erro", "Erro ao processar a operação.");
            response.sendRedirect(request.getContextPath() + "/livros?action=listar");
        }
    }
}