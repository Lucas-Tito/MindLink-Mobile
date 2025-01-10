package com.titolucas.mindlink.perfil.data;

public class PerfilData {
    private final String nome;
    private final String email;

    public PerfilData(String nome, String email) {
        this.nome = nome;
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }
}
