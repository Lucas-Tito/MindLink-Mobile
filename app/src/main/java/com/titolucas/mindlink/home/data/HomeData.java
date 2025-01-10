package com.titolucas.mindlink.home.data;

import com.titolucas.mindlink.Psychologist;

import java.util.List;

public class HomeData {
    private final List<Psychologist> psychologistList;

    public HomeData(List<Psychologist> psychologistList) {
        this.psychologistList = psychologistList;
    }

    public List<Psychologist> getPsychologistList() {
        return psychologistList;
    }
}
