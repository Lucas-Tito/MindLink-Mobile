package com.titolucas.mindlink.home.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.titolucas.mindlink.Psychologist;
import com.titolucas.mindlink.home.data.HomeData;
import com.titolucas.mindlink.home.repository.HomeRepository;

import java.util.List;

public class HomeViewModel extends ViewModel {
    private final MutableLiveData<HomeData> homeLiveData = new MutableLiveData<>();
    private final HomeRepository repository;

    public HomeViewModel(HomeRepository repository) {
        this.repository = repository;
    }

    public LiveData<HomeData> getHomeLiveData() {
        return homeLiveData;
    }

    public void loadHomeData() {
        // Obtém dados do repository
        List<Psychologist> psychologistList = repository.fetchPsychologists();
        homeLiveData.setValue(new HomeData(psychologistList));
    }
}
