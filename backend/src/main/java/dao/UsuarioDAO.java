package dao;

import model.Usuario;
import util.ConexaoDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    public Usuario autenticar(String email, String senha) {
        String sql = "SELECT id, nome, email, perfil FROM usuarios WHERE email = ? AND senha = ? LIMIT 1";

        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, senha);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("email"),
                            rs.getString("perfil")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao autenticar usuário: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }
 // Método que faz o INSERT no banco de dados
    public void cadastrar(Usuario usuario) {
        // A query de inserção (o id é gerado automaticamente pelo banco)
        String sql = "INSERT INTO usuarios (nome, email, senha, perfil) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Trocando as interrogações pelos dados do usuário
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha());
            stmt.setString(4, "leitor"); // Define um perfil padrão para quem se cadastra

            // O executeUpdate() é o comando que de fato roda o INSERT, UPDATE ou DELETE
            stmt.executeUpdate(); 

        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar usuário: " + e.getMessage());
            e.printStackTrace();
            // Lança uma exceção para o Servlet saber que deu erro (ex: email repetido)
            throw new RuntimeException("Erro ao salvar no banco de dados", e); 
        }
    }
}
