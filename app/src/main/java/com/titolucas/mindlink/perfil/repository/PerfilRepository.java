package com.titolucas.mindlink.perfil.repository;

import com.titolucas.mindlink.perfil.data.PerfilData;

public class PerfilRepository {
    public PerfilData fetchPerfilData() {
        // Simula a busca de dados do perfil
        return new PerfilData("Nome do Usuário", "email@example.com");
    }
}
