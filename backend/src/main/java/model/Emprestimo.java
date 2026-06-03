package model;

import java.time.LocalDate;

public class Emprestimo {
    private int id;
    private Usuario usuario; // Objeto Usuario completo para facilitar na View
    private Livro livro;     // Objeto Livro completo
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucao;
    private String status;

    // Construtor vazio
    public Emprestimo() {}

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Livro getLivro() { return livro; }
    public void setLivro(Livro livro) { this.livro = livro; }

    public LocalDate getDataEmprestimo() { return dataEmprestimo; }
    public void setDataEmprestimo(LocalDate dataEmprestimo) { this.dataEmprestimo = dataEmprestimo; }

    public LocalDate getDataDevolucao() { return dataDevolucao; }
    public void setDataDevolucao(LocalDate dataDevolucao) { this.dataDevolucao = dataDevolucao; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}