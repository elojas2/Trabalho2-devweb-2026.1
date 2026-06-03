package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Usuario;
import dao.UsuarioDAO;

@WebServlet("/cadastroUser")
public class CadastroServlet extends HttpServlet {
    
    // Esse método EXIBE a tela quando o usuário clica no link
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/cadastroUser.jsp").forward(request, response);
    }

    // Você vai precisar do doPost depois para SALVAR no banco
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. Configura para aceitar acentos (UTF-8)
        request.setCharacterEncoding("UTF-8");

        // 2. Pega os valores digitados no HTML (os nomes aqui devem ser IGUAIS aos atributos 'name' do HTML)
        String nomeRecebido = request.getParameter("nome");
        String emailRecebido = request.getParameter("email");
        String senhaRecebida = request.getParameter("senha");

        // 3. Cria um objeto Usuario e preenche com os dados
        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(nomeRecebido);
        novoUsuario.setEmail(emailRecebido);
        novoUsuario.setSenha(senhaRecebida); // Nota: Em um projeto real, aqui você faria o Hash da senha!

        // 4. Chama o DAO para salvar no banco de dados
        UsuarioDAO dao = new UsuarioDAO();
        try {
            // OBS: Verifique no seu UsuarioDAO qual é o nome exato do método de salvar (pode ser inserir(), salvar(), cadastrar() etc)
            dao.cadastrar(novoUsuario); 
            
            // 5. Se deu tudo certo, redireciona o usuário para a tela de login
            response.sendRedirect(request.getContextPath() + "/login");
            
        } catch (Exception e) {
            e.printStackTrace();
            // Se der erro (ex: email já existe), recarrega a página de cadastro com uma mensagem
            request.setAttribute("mensagemErro", "Erro ao cadastrar usuário. Tente novamente.");
            request.getRequestDispatcher("/WEB-INF/views/cadastroUser.jsp").forward(request, response);
        }
    
    }
}