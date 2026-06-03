package controller;
import dao.LivroDAO;
import model.Livro;
import model.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/livros/*")
public class LivroServlet extends BaseServlet {

	private LivroDAO dao = new LivroDAO();

	// GET → listar livros ou buscar por ID
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String pathInfo = req.getPathInfo();

		// Se for /livros/{id}, buscar livro específico
		if (pathInfo != null && pathInfo.length() > 1) {
			try {
				int id = Integer.parseInt(pathInfo.substring(1));
				Livro livro = dao.buscarPorId(id);
				if (livro == null) {
					resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Livro não encontrado");
					return;
				}
				enviarJson(resp, livro);
				return;
			} catch (NumberFormatException e) {
				// Se não for número, segue para busca comum
			}
		}

		String termo = req.getParameter("q");
		String termoNormalizado = termo == null ? null : termo.trim();
		List<Livro> lista;

		if (termoNormalizado != null && !termoNormalizado.isEmpty()) {
			lista = dao.buscar(termoNormalizado);
		} else {
			lista = dao.listarTodos();
		}

		enviarJson(resp, lista);
	}

	// POST → cadastrar ou atualizar livro
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String pathInfo = req.getPathInfo();
		Usuario usuarioLogado = (Usuario) req.getSession().getAttribute("usuarioLogado");

		// Proteção: Apenas ADMIN pode salvar, atualizar ou excluir dados
		if (usuarioLogado == null || !usuarioLogado.isAdmin()) {
			enviarJson(resp, new Resposta("Acesso negado. Apenas administradores podem realizar esta ação.", "danger"), HttpServletResponse.SC_FORBIDDEN);
			return;
		}

		// POST /livros/excluir
		if ("/excluir".equals(pathInfo)) {
			try {
				int id = Integer.parseInt(req.getParameter("id"));
				dao.remover(id);
				enviarJson(resp, new Resposta("Livro excluído com sucesso!", "success"), HttpServletResponse.SC_OK);
			} catch (NumberFormatException e) {
				enviarJson(resp, new Resposta("ID inválido para exclusão.", "danger"), HttpServletResponse.SC_BAD_REQUEST);
			} catch (IllegalStateException e) {
				enviarJson(resp, new Resposta("Erro ao excluir: " + e.getMessage(), "danger"), HttpServletResponse.SC_CONFLICT);
			}
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
				enviarJson(resp, new Resposta("Livro atualizado com sucesso!", "success"), HttpServletResponse.SC_OK);
			} else {
				dao.cadastrar(livro);
				enviarJson(resp, new Resposta("Livro cadastrado com sucesso!", "success"), HttpServletResponse.SC_CREATED);
			}

		} catch (NumberFormatException e) {
			enviarJson(resp, new Resposta("O campo Ano deve ser um número válido.", "danger"), HttpServletResponse.SC_BAD_REQUEST);
		} catch (IllegalArgumentException | IllegalStateException e) {
			enviarJson(resp, new Resposta(e.getMessage(), "danger"), HttpServletResponse.SC_BAD_REQUEST);
		}
	}
}
