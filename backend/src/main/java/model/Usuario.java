package model;

public class Usuario {
    private int id;
    private String nome;
    private String email;
    private String senha;
    private String perfil;
    private boolean admin;

    public Usuario() {
    }

    public Usuario(int id, String nome, String email, String perfil) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        setPerfil(perfil);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
        this.admin = "ADMIN".equalsIgnoreCase(perfil);
    }

    public boolean isAdmin() {
        return admin;
    }
    
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
