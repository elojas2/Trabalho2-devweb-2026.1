package controller;
import dao.LivroDAO;
import model.Livro;
import model.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/livros/*")
public class LivroServlet extends HttpServlet {

	private LivroDAO dao = new LivroDAO();

	// GET → listar livros, abrir formulário de cadastro, editar ou excluir
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String pathInfo = req.getPathInfo();
		Usuario usuarioLogado = (Usuario) req.getSession().getAttribute("usuarioLogado");

		// Proteção: Apenas ADMIN acessa formulários de cadastro e edição
		if ("/cadastrar".equals(pathInfo) || "/editar".equals(pathInfo)) {
			if (usuarioLogado == null || !usuarioLogado.isAdmin()) {
				req.getSession().setAttribute("mensagem", "Acesso negado. Apenas administradores podem realizar esta ação.");
				req.getSession().setAttribute("tipoMensagem", "danger");
				resp.sendRedirect(req.getContextPath() + "/livros");
				return;
			}
		}

		if ("/cadastrar".equals(pathInfo)) {
			req.getRequestDispatcher("/WEB-INF/views/livros/cadastrar.jsp").forward(req, resp);
			return;
		}

		if ("/editar".equals(pathInfo)) {
			try {
				int id = Integer.parseInt(req.getParameter("id"));
				Livro livro = dao.buscarPorId(id);
				if (livro == null) {
					resp.sendRedirect(req.getContextPath() + "/livros");
					return;
				}
				req.setAttribute("livro", livro);
				req.getRequestDispatcher("/WEB-INF/views/livros/editar.jsp").forward(req, resp);
			} catch (NumberFormatException e) {
				resp.sendRedirect(req.getContextPath() + "/livros");
			}
			return;
		}

		// Se houver um pathInfo que não seja nulo, "/" ou as opções acima, é um 404
		if (pathInfo != null && !"/".equals(pathInfo)) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		String termo = req.getParameter("q");
		String termoNormalizado = termo == null ? null : termo.trim();
		List<Livro> lista;
		
		if (termoNormalizado != null && !termoNormalizado.isEmpty()) {
			lista = dao.buscar(termoNormalizado);
			req.setAttribute("termoBusca", termoNormalizado);
		} else {
			lista = dao.listarTodos();
		}
		
		req.setAttribute("livros", lista);
		req.getRequestDispatcher("/WEB-INF/views/livros/listar.jsp").forward(req, resp);
	}

	// POST → cadastrar ou atualizar livro
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String pathInfo = req.getPathInfo();
		Usuario usuarioLogado = (Usuario) req.getSession().getAttribute("usuarioLogado");

		// Proteção: Apenas ADMIN pode salvar, atualizar ou excluir dados
		if (usuarioLogado == null || !usuarioLogado.isAdmin()) {
			req.getSession().setAttribute("mensagem", "Acesso negado. Apenas administradores podem realizar esta ação.");
			req.getSession().setAttribute("tipoMensagem", "danger");
			resp.sendRedirect(req.getContextPath() + "/livros");
			return;
		}

		// POST /livros/excluir — exclusão segura
		if ("/excluir".equals(pathInfo)) {
			try {
				int id = Integer.parseInt(req.getParameter("id"));
				dao.remover(id);
				req.getSession().setAttribute("mensagem", "Livro excluído com sucesso!");
				req.getSession().setAttribute("tipoMensagem", "success");
			} catch (NumberFormatException e) {
				req.getSession().setAttribute("mensagem", "ID inválido para exclusão.");
				req.getSession().setAttribute("tipoMensagem", "danger");
			} catch (IllegalStateException e) {
				req.getSession().setAttribute("mensagem", "Erro ao excluir: " + e.getMessage());
				req.getSession().setAttribute("tipoMensagem", "danger");
			}
			resp.sendRedirect(req.getContextPath() + "/livros");
			return;
		}
// POST /livros/editar ou POST /livros — cadastrar/atualizar
try {
	String titulo = req.getParameter("titulo");
	String autor = req.getParameter("autor");
	String anoStr = req.getParameter("ano");
	boolean disponivel = Boolean.parseBoolean(req.getParameter("disponivel"));

	// Validação Básica
	if (titulo == null || titulo.trim().isEmpty() || 
		autor == null || autor.trim().isEmpty() || 
		anoStr == null || anoStr.trim().isEmpty()) {
		throw new IllegalArgumentException("Todos os campos (Título, Autor e Ano) são obrigatórios.");
	}

	int ano = Integer.parseInt(anoStr);
	if (ano < 0 || ano > 2100) {
		throw new IllegalArgumentException("Por favor, insira um ano válido.");
	}

	Livro livro = new Livro();
	livro.setTitulo(titulo.trim());
	livro.setAutor(autor.trim());
	livro.setAno(ano);
	livro.setDisponivel(disponivel);

	if ("/editar".equals(pathInfo)) {
		int id = Integer.parseInt(req.getParameter("id"));
		livro.setId(id);
		dao.atualizar(livro);
		req.getSession().setAttribute("mensagem", "Livro atualizado com sucesso!");
	} else {
		dao.cadastrar(livro);
		req.getSession().setAttribute("mensagem", "Livro cadastrado com sucesso!");
	}
	req.getSession().setAttribute("tipoMensagem", "success");

} catch (NumberFormatException e) {
	req.getSession().setAttribute("mensagem", "O campo Ano deve ser um número válido.");
	req.getSession().setAttribute("tipoMensagem", "danger");
} catch (IllegalArgumentException | IllegalStateException e) {
	req.getSession().setAttribute("mensagem", e.getMessage());
	req.getSession().setAttribute("tipoMensagem", "danger");
}


		resp.sendRedirect(req.getContextPath() + "/livros");
	}
}
