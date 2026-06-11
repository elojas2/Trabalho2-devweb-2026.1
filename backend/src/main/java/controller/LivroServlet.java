package controller;
import com.google.gson.JsonObject;
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
				// não é um ID numérico, segue para busca
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

	// POST → cadastrar novo livro (body: JSON)
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		Usuario usuarioLogado = (Usuario) req.getSession().getAttribute("usuarioLogado");
		if (usuarioLogado == null || !usuarioLogado.isAdmin()) {
			enviarJson(resp, new Resposta("Acesso negado. Apenas administradores podem realizar esta ação.", "danger"), HttpServletResponse.SC_FORBIDDEN);
			return;
		}

		try {
			JsonObject body = lerJson(req);
			String titulo = body.get("titulo").getAsString().trim();
			String autor = body.get("autor").getAsString().trim();
			int ano = body.get("ano").getAsInt();
			boolean disponivel = body.get("disponivel").getAsBoolean();

			if (titulo.isEmpty() || autor.isEmpty()) {
				throw new IllegalArgumentException("Todos os campos (Título, Autor e Ano) são obrigatórios.");
			}
			if (ano < 0 || ano > 2100) {
				throw new IllegalArgumentException("Por favor, insira um ano válido.");
			}

			Livro livro = new Livro();
			livro.setTitulo(titulo);
			livro.setAutor(autor);
			livro.setAno(ano);
			livro.setDisponivel(disponivel);
			dao.cadastrar(livro);
			enviarJson(resp, new Resposta("Livro cadastrado com sucesso!", "success"), HttpServletResponse.SC_CREATED);

		} catch (IllegalArgumentException e) {
			enviarJson(resp, new Resposta(e.getMessage(), "danger"), HttpServletResponse.SC_BAD_REQUEST);
		} catch (Exception e) {
			enviarJson(resp, new Resposta("Erro ao cadastrar livro: " + e.getMessage(), "danger"), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	// PUT /livros/{id} → atualizar livro (body: JSON)
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		Usuario usuarioLogado = (Usuario) req.getSession().getAttribute("usuarioLogado");
		if (usuarioLogado == null || !usuarioLogado.isAdmin()) {
			enviarJson(resp, new Resposta("Acesso negado. Apenas administradores podem realizar esta ação.", "danger"), HttpServletResponse.SC_FORBIDDEN);
			return;
		}

		String pathInfo = req.getPathInfo();
		if (pathInfo == null || pathInfo.length() <= 1) {
			enviarJson(resp, new Resposta("ID do livro não informado.", "danger"), HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		try {
			int id = Integer.parseInt(pathInfo.substring(1));
			JsonObject body = lerJson(req);
			String titulo = body.get("titulo").getAsString().trim();
			String autor = body.get("autor").getAsString().trim();
			int ano = body.get("ano").getAsInt();
			boolean disponivel = body.get("disponivel").getAsBoolean();

			if (titulo.isEmpty() || autor.isEmpty()) {
				throw new IllegalArgumentException("Todos os campos (Título, Autor e Ano) são obrigatórios.");
			}
			if (ano < 0 || ano > 2100) {
				throw new IllegalArgumentException("Por favor, insira um ano válido.");
			}

			Livro livro = new Livro();
			livro.setId(id);
			livro.setTitulo(titulo);
			livro.setAutor(autor);
			livro.setAno(ano);
			livro.setDisponivel(disponivel);
			dao.atualizar(livro);
			enviarJson(resp, new Resposta("Livro atualizado com sucesso!", "success"), HttpServletResponse.SC_OK);

		} catch (NumberFormatException e) {
			enviarJson(resp, new Resposta("ID inválido.", "danger"), HttpServletResponse.SC_BAD_REQUEST);
		} catch (IllegalArgumentException e) {
			enviarJson(resp, new Resposta(e.getMessage(), "danger"), HttpServletResponse.SC_BAD_REQUEST);
		} catch (Exception e) {
			enviarJson(resp, new Resposta("Erro ao atualizar livro: " + e.getMessage(), "danger"), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	// DELETE /livros/{id} → excluir livro
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		Usuario usuarioLogado = (Usuario) req.getSession().getAttribute("usuarioLogado");
		if (usuarioLogado == null || !usuarioLogado.isAdmin()) {
			enviarJson(resp, new Resposta("Acesso negado. Apenas administradores podem realizar esta ação.", "danger"), HttpServletResponse.SC_FORBIDDEN);
			return;
		}

		String pathInfo = req.getPathInfo();
		if (pathInfo == null || pathInfo.length() <= 1) {
			enviarJson(resp, new Resposta("ID do livro não informado.", "danger"), HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		try {
			int id = Integer.parseInt(pathInfo.substring(1));
			dao.remover(id);
			enviarJson(resp, new Resposta("Livro excluído com sucesso!", "success"), HttpServletResponse.SC_OK);
		} catch (NumberFormatException e) {
			enviarJson(resp, new Resposta("ID inválido.", "danger"), HttpServletResponse.SC_BAD_REQUEST);
		} catch (IllegalStateException e) {
			enviarJson(resp, new Resposta("Erro ao excluir: " + e.getMessage(), "danger"), HttpServletResponse.SC_CONFLICT);
		} catch (Exception e) {
			enviarJson(resp, new Resposta("Erro ao excluir livro: " + e.getMessage(), "danger"), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}
}
