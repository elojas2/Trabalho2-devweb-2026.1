package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Emprestimo;
import model.Livro;
import model.Usuario;
import util.ConexaoDB;

public class EmprestimoDAO {
	
	public void registrarEmprestimo(Emprestimo emprestimo) throws SQLException {
	    String sqlEmprestimo = "INSERT INTO emprestimos (id_usuario, id_livro, data_emprestimo, status) VALUES (?, ?, ?, 'ATIVO')";
	    String sqlLivro = "UPDATE livros SET disponivel = 0 WHERE id = ? AND disponivel = 1";
	    
	    Connection conn = null;
	    try {
	        conn = ConexaoDB.getConnection();
	        conn.setAutoCommit(false); 

	        try (PreparedStatement stmtEmp = conn.prepareStatement(sqlEmprestimo)) {
	            stmtEmp.setInt(1, emprestimo.getUsuario().getId());
	            stmtEmp.setInt(2, emprestimo.getLivro().getId());
	            stmtEmp.setDate(3, java.sql.Date.valueOf(emprestimo.getDataEmprestimo()));
	            stmtEmp.executeUpdate();
	        }

	        try (PreparedStatement stmtLiv = conn.prepareStatement(sqlLivro)) {
	            stmtLiv.setInt(1, emprestimo.getLivro().getId());
	            int linhasAfetadas = stmtLiv.executeUpdate();
	            if (linhasAfetadas == 0) {
	                throw new SQLException("Livro já emprestado por outro processo.");
	            }
	            stmtLiv.executeUpdate();
	        }

	        conn.commit(); 
	    } catch (SQLException e) {
	        if (conn != null) conn.rollback(); 
	        throw e;
	    } finally {
	        if (conn != null) conn.close();
	    }
	}


    public List<Emprestimo> listarPorUsuario(int idUsuario) throws SQLException {
        List<Emprestimo> lista = new ArrayList<>(); 
        String sql = "SELECT e.*, l.titulo FROM emprestimos e " +
                     "JOIN livros l ON e.id_livro = l.id " +
                     "WHERE e.id_usuario = ? AND e.status = 'ATIVO'";

        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Emprestimo e = new Emprestimo();
                e.setId(rs.getInt("id"));
                e.setDataEmprestimo(rs.getDate("data_emprestimo").toLocalDate());
                e.setStatus(rs.getString("status"));
                
                Livro l = new Livro();
                l.setId(rs.getInt("id_livro"));
                l.setTitulo(rs.getString("titulo"));
                e.setLivro(l);
                
                lista.add(e);
            }
        }
        return lista;
    }


public void devolverLivro(int idEmprestimo) throws SQLException {
    	String sqlEmprestimo = "UPDATE emprestimos SET data_devolucao = ?, status = 'DEVOLVIDO' WHERE id = ?";
    
    	String sqlLivro = "UPDATE livros SET disponivel = 1 WHERE id = (SELECT id_livro FROM emprestimos WHERE id = ?)";

    	Connection conn = null;
    	try {
    		conn = ConexaoDB.getConnection();
    		conn.setAutoCommit(false); 

    		try (PreparedStatement stmtEmp = conn.prepareStatement(sqlEmprestimo)) {
    			stmtEmp.setDate(1, java.sql.Date.valueOf(java.time.LocalDate.now()));
    			stmtEmp.setInt(2, idEmprestimo);
    			stmtEmp.executeUpdate();
    		}

    		try (PreparedStatement stmtLiv = conn.prepareStatement(sqlLivro)) {
    			stmtLiv.setInt(1, idEmprestimo); 
    			stmtLiv.executeUpdate();
    		}

    		conn.commit(); 
    	} catch (SQLException e) {
    		if (conn != null) {
    			conn.rollback(); 
    		}
    		throw e;
    	} finally {
    		if (conn != null) {
    			conn.close(); 
    		}
    	}
	}
}