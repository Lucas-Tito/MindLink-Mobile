package com.titolucas.mindlink.perfil.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.titolucas.mindlink.perfil.data.PerfilData;
import com.titolucas.mindlink.perfil.repository.PerfilRepository;

public class PerfilViewModel extends ViewModel {
    private final PerfilRepository repository;
    private final MutableLiveData<PerfilData> perfilLiveData = new MutableLiveData<>();

    public PerfilViewModel(PerfilRepository repository) {
        this.repository = repository;
    }

    public LiveData<PerfilData> getPerfilLiveData() {
        return perfilLiveData;
    }

    public void loadPerfilData() {
        PerfilData data = repository.fetchPerfilData();
        perfilLiveData.setValue(data);
    }
}
