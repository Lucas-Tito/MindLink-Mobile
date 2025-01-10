package com.titolucas.mindlink.perfil.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.titolucas.mindlink.perfil.repository.PerfilRepository;

public class PerfilViewModelFactory implements ViewModelProvider.Factory {
    private final PerfilRepository repository;

    public PerfilViewModelFactory(PerfilRepository repository) {
        this.repository = repository;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(PerfilViewModel.class)) {
            return (T) new PerfilViewModel(repository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
