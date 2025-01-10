package com.titolucas.mindlink.agenda.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.titolucas.mindlink.agenda.data.AgendaResult;
import com.titolucas.mindlink.agenda.repository.AgendaRepository;

public class AgendaViewModel extends ViewModel {
    private final AgendaRepository repository;
    private final MutableLiveData<AgendaResult> agendaLiveData = new MutableLiveData<>();

    public AgendaViewModel(AgendaRepository repository) {
        this.repository = repository;
    }

    public LiveData<AgendaResult> getAgendaLiveData() {
        return agendaLiveData;
    }

    public void loadAgendaData() {
        agendaLiveData.setValue(repository.fetchAgendaData());
    }
}
