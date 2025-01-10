package com.titolucas.mindlink.agenda.repository;

import com.titolucas.mindlink.agenda.data.AgendaResult;

import java.util.Arrays;

public class AgendaRepository {
    public AgendaResult fetchAgendaData() {
        return new AgendaResult(Arrays.asList("Consulta 1", "Consulta 2", "Consulta 3"));
    }
}
