package com.titolucas.mindlink.agenda.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.titolucas.mindlink.agenda.repository.AgendaRepository;

public class AgendaViewModelFactory implements ViewModelProvider.Factory {

    private final AgendaRepository repository;

    public AgendaViewModelFactory(AgendaRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AgendaViewModel.class)) {
            return (T) new AgendaViewModel(repository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
