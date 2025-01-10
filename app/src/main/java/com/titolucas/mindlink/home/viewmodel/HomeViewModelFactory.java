package com.titolucas.mindlink.home.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.titolucas.mindlink.home.repository.HomeRepository;

public class HomeViewModelFactory implements ViewModelProvider.Factory {
    private final HomeRepository repository;

    public HomeViewModelFactory(HomeRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(HomeViewModel.class)) {
            return (T) new HomeViewModel(repository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}