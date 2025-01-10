package com.titolucas.mindlink.home.repository;


import com.titolucas.mindlink.Psychologist;

import java.util.Arrays;
import java.util.List;

public class HomeRepository {

    public List<Psychologist> fetchPsychologists() {
        // Retorne uma lista de objetos Psychologist
        return Arrays.asList(
                new Psychologist("Ayrton Senna", "Psicólogo Infantil", 5.0),
                new Psychologist("Dr. João", "Psicólogo Clínico", 4.8),
                new Psychologist("Dra. Clara", "Psicóloga Organizacional", 4.9)
        );
    }
}
